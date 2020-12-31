// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology;

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.registry.In;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.Chunks;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RegisterPlugin
public class AlchemyPlantRasterizer implements WorldRasterizerPlugin {
    @In
    private BlockManager blockManager;

    private Block antiPoison;
    private Block healing;
    private Block jumpSpeed;
    private Block poison;
    private Block rage;
    private Block regen;
    private Block swimSpeed;
    private Block walkSpeed;

    private Random rand;

    private Map<Integer, Block> blockMap;

    @Override
    public void initialize() {
        antiPoison = blockManager.getBlock("Alchemy:AntiPoisonHerb");
        healing = blockManager.getBlock("Alchemy:HealingHerb");
        jumpSpeed = blockManager.getBlock("Alchemy:JumpSpeedHerb");
        poison = blockManager.getBlock("Alchemy:PoisonHerb");
        rage = blockManager.getBlock("Alchemy:RageHerb");
        regen = blockManager.getBlock("Alchemy:RegenHerb");
        swimSpeed = blockManager.getBlock("Alchemy:SwimSpeedHerb");
        walkSpeed = blockManager.getBlock("Alchemy:WalkSpeedHerb");

        rand = new Random();

        blockMap = new HashMap<>() {
            {
                put(0, antiPoison);
                put(1, healing);
                put(2, jumpSpeed);
                put(3, poison);
                put(4, rage);
                put(5, regen);
                put(6, swimSpeed);
                put(7, walkSpeed);
            }
        };
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        AlchemyPlantFacet alchemyPlantFacet = chunkRegion.getFacet(AlchemyPlantFacet.class);

        for (Vector3ic block : alchemyPlantFacet.getWorldRegion()) {
            if (alchemyPlantFacet.getWorld(block)) {
                chunk.setBlock(Chunks.toRelative(block, new Vector3i()), blockMap.get(rand.nextInt(8)));
            }
        }
    }
}
