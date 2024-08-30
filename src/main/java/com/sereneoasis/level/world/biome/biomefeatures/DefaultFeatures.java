package com.sereneoasis.level.world.biome.biomefeatures;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

/***
 * Contains features which will be created by default.
 * Currently unused - below is an example
 */
public class DefaultFeatures {

    // Creates a 2x2x2 rock of stone
    public static final Supplier<Feature> ROCK = () -> {
        HashMap< Vector, Material> rockMap = new HashMap<>();
        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                for (int y = 0; y < 2; y++) {
                    rockMap.put(new Vector(x, y, z), Material.COBBLESTONE);
                }
            }
        }
        return new Feature(rockMap);
    } ;


    public static final Supplier<Feature> GOLD_ORE_CLUMP = () -> {
        HashMap< Vector, Material> rockMap = new HashMap<>();
        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                    rockMap.put(new Vector(x, new Random().nextInt(0,2), z), Material.GOLD_ORE);
            }
        }
        return new Feature(rockMap);
    } ;

//    public static final Supplier<Feature> COAL_ORE_CLUMP = () -> {
//        HashMap< Vector, Material> rockMap = new HashMap<>();
//        for (int x = -2; x < 2; x++) {
//            for (int z = -2; z < 2; z++) {
//                rockMap.put(new Vector(x, rand.nextInt(0,2), z), Material.COAL_ORE);
//            }
//        }
//        return new Feature(rockMap);
//    } ;
//
//    public static final Supplier<Feature> REDSTONE_ORE_CLUMP = () -> {
//        HashMap< Vector, Material> rockMap = new HashMap<>();
//        for (int x = -2; x < 2; x++) {
//            for (int z = -2; z < 2; z++) {
//                rockMap.put(new Vector(x, rand.nextInt(0,2), z), Material.REDSTONE_ORE);
//            }
//        }
//        return new Feature(rockMap);
//    } ;
    
}
