package org.terasology;

import org.terasology.world.block.BlockRegion;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.BaseBooleanFieldFacet3D;

public class AlchemyPlantFacet extends BaseBooleanFieldFacet3D {

    public AlchemyPlantFacet(BlockRegion targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}