// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology;

import org.terasology.engine.world.block.BlockRegion;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.facets.base.BaseBooleanFieldFacet3D;

public class AlchemyPlantFacet extends BaseBooleanFieldFacet3D {

    public AlchemyPlantFacet(BlockRegion targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}
