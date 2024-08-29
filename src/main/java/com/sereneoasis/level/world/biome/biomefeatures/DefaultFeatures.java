package com.sereneoasis.level.world.biome.biomefeatures;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.function.Supplier;

/***
 * Contains features which will be created by default.
 * Currently unused - below is an example
 */
public class DefaultFeatures {

    // Creates a 6x6x6 rock of stone
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

}
