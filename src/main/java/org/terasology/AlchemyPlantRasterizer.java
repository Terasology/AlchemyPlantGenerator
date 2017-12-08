package org.terasology;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RegisterPlugin
public class AlchemyPlantRasterizer implements WorldRasterizerPlugin {
    private Block antiPoison;
    private Block healing;
    private Block jumpSpeed;
    private Block poison;
    private Block rage;
    private Block regen;
    private Block swimSpeed;
    private Block walkSpeed;

    private Random rand;

    private Map<Integer, Block> block_map;

    @Override
    public void initialize() {
        antiPoison = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:AntiPoisonHerb");
        healing = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:HealingHerb");
        jumpSpeed = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:JumpSpeedHerb");
        poison = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:PoisonHerb");
        rage = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:RageHerb");
        regen = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:RegenHerb");
        swimSpeed = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:SwimSpeedHerb");
        walkSpeed = CoreRegistry.get(BlockManager.class).getBlock("Alchemy:WalkSpeedHerb");

        rand = new Random();

        block_map = new HashMap<Integer, Block>() {
            private static final long serialVersionUID = 1L;

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

        for (Vector3i block : alchemyPlantFacet.getWorldRegion()) {
            if (alchemyPlantFacet.getWorld(block)) {
                chunk.setBlock(ChunkMath.calcBlockPos(block), block_map.get(rand.nextInt(8)));
            }
        }
    }
}
