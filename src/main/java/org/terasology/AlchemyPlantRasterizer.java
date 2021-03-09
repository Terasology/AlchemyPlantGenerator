// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology;

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.Chunks;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizerPlugin;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;

import java.util.ArrayList;
import java.util.List;
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

    private List<Block> herbList;

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

        herbList = new ArrayList<>();
        herbList.add(antiPoison);
        herbList.add(healing);
        herbList.add(jumpSpeed);
        herbList.add(poison);
        herbList.add(rage);
        herbList.add(regen);
        herbList.add(swimSpeed);
        herbList.add(walkSpeed);
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        AlchemyPlantFacet alchemyPlantFacet = chunkRegion.getFacet(AlchemyPlantFacet.class);

        for (Vector3ic block : alchemyPlantFacet.getWorldRegion()) {
            if (alchemyPlantFacet.getWorld(block)) {
                chunk.setBlock(Chunks.toRelative(block, new Vector3i()), herbList.get(rand.nextInt(herbList.size())));
            }
        }
    }
}
