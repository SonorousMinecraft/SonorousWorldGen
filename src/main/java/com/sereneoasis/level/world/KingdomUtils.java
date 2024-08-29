package com.sereneoasis.level.world;

import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;

public class KingdomUtils {

    public static boolean isInsideKingdom(int x, int z){
        return (GenerationNoise.getNoise(NoiseCategories.KINGDOM_BORDERS, x,  z) > 0.7);
    }

    public static boolean isKingdomBuilding(int x, int z){
        return  GenerationNoise.getNoise(NoiseCategories.KINGDOM_BUILDINGS,  x,  z) > 0.6;
    }

}
