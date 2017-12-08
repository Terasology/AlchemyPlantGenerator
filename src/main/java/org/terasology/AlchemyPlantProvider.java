package org.terasology;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.rendering.nui.properties.Range;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.ConfigurableFacetProvider;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.HashMap;
import java.util.Map;

@RegisterPlugin
@Requires(@Facet(SurfaceHeightFacet.class))
@Produces(AlchemyPlantFacet.class)
public class AlchemyPlantProvider implements FacetProviderPlugin, ConfigurableFacetProvider {

    private AlchemyPlantConfiguration configuration = new AlchemyPlantConfiguration();

    private Noise noise;

    private Map<Float, Float> config_map = new HashMap<Float, Float>() {
        private static final long serialVersionUID = 1L;
        
        {
            put(0.0f, 1.0f);
            put(25.0f, 0.991f);
            put(50.0f, 0.99f);
            put(75.0f, 0.98f);
            put(100.0f, 0.97f);
        }
    };

    @Override
    public void setSeed(long seed) {
        noise = new WhiteNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        //Don't forget you sometimes have to extend the borders.
        //extendBy(top, bottom, sides) is the method used for this.

        Border3D border = region.getBorderForFacet(AlchemyPlantFacet.class);
        AlchemyPlantFacet facet = new AlchemyPlantFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for (BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()) {
            int surfaceHeight = (int) surfaceHeightFacet.getWorld(position);

            if (facet.getWorldRegion().encompasses(position.getX(), surfaceHeight, position.getY())
                && noise.noise(position.getX(), position.getY()) > config_map.get(configuration.plantRarity)) {
                facet.setWorld(position.getX(), surfaceHeight, position.getY(), true);
            }
        }

        region.setRegionFacet(AlchemyPlantFacet.class, facet);

    }

    @Override
    public String getConfigurationName() {
        return "Alchemy Plants";
    }

    @Override
    public Component getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Component configuration) {
        this.configuration = (AlchemyPlantConfiguration) configuration;
    }

    private static class AlchemyPlantConfiguration implements Component {
        @Range(min = 0.0f, max = 100f, increment = 25f, precision = 1, description = "Plant Rarity")
        private float plantRarity = 50f;
    }

}