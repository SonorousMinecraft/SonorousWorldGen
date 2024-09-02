package com.sereneoasis.level.world.chunk.populator;

import com.sereneoasis.level.world.biome.BiomeLayers;
import com.sereneoasis.level.world.biome.BiomeRepresentation;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class PopulatorUtils {

    public static boolean isSurface(Biome biome, Material type){
        return BiomeRepresentation.getBiomeRepresentation(biome).getLayers().get(BiomeLayers.SURFACE).contains(type);
    }
}
