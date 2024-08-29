package com.sereneoasis.level.world.chunk;

import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;

public class ChunkUtils {

    public static final int Y_LIMIT = 240;
    public static final int SEA_LEVEL = 50;
    public static final int LAYER_1_HEIGHT = 10;
    public static final int AVERAGE_HEIGHT = 100;
    public static final int CONTINENTALNESS_DEVIATION = 100;
    public static final int TERRAIN_DEVIATION = 20;

    public static float getCurrentY(int x, int z){
        float continentalness = GenerationNoise.getNoise(NoiseCategories.CONTINENTALNESS, x ,z);
        float detail = GenerationNoise.getNoise(NoiseCategories.DETAIl, x ,z);
        float terrain = GenerationNoise.getNoise(NoiseCategories.TERRAIN, x ,z);

        float currentY = AVERAGE_HEIGHT +
                (continentalness * CONTINENTALNESS_DEVIATION) +
                (terrain*TERRAIN_DEVIATION);
        return currentY;
    }


    public static float getCurrentY(int x, int chunkX, int z, int chunkZ){
        return getCurrentY(chunkX * 16 + x, chunkZ * 16 + z);
    }
}
