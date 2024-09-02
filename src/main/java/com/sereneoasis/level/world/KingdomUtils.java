package com.sereneoasis.level.world;

import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;

public class KingdomUtils {

    public static final int WALL_HEIGHT = 40;

    public static boolean isInsideKingdom(int x, int z){
        double targetContinentalness = GenerationNoise.getNoise(NoiseCategories.CONTINENTALNESS, x, z) ;
//        return true;
        return (GenerationNoise.getNoise(NoiseCategories.KINGDOM_BORDERS, x,  z) > 0.7) && targetContinentalness >= -0.1 && targetContinentalness < 0.5;
//        return (GenerationNoise.getNoise(NoiseCategories.KINGDOM_BORDERS, x,  z) > 0.3);
    }

    public static boolean isKingdomWalls(int x, int z){
        return GenerationNoise.getNoise(NoiseCategories.KINGDOM_WALLS, x,  z) > 0.7;
    }

}
