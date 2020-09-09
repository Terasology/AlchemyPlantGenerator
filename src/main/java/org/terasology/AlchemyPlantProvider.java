// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.WhiteNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.ConfigurableFacetProvider;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetProviderPlugin;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.engine.world.generation.Requires;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.nui.properties.Range;

import java.util.HashMap;
import java.util.Map;

@RegisterPlugin
@Requires(@Facet(SurfaceHeightFacet.class))
@Produces(AlchemyPlantFacet.class)
public class AlchemyPlantProvider implements FacetProviderPlugin, ConfigurableFacetProvider {

    private final Map<Float, Float> configMap = new HashMap<Float, Float>() {
        private static final long serialVersionUID = 1L;

        {
            put(0.0f, 1.0f);
            put(25.0f, 0.991f);
            put(50.0f, 0.99f);
            put(75.0f, 0.98f);
            put(100.0f, 0.97f);
        }
    };
    private AlchemyPlantConfiguration configuration = new AlchemyPlantConfiguration();
    private Noise noise;

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
                    && noise.noise(position.getX(), position.getY()) > configMap.get(configuration.plantRarity)) {
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
        private final float plantRarity = 50f;
    }
}
