package com.sereneoasis.utils;

import com.sereneoasis.SereneWorldGen;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;

import java.util.Random;

public class StructureUtils {

    // https://minecraft.fandom.com/wiki/Ancient_City
    public static void spawnStructure(Location loc, String name){
        SereneWorldGen.plugin.getServer().getStructureManager().loadStructure(NamespacedKey.minecraft(name)).place(loc, false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
    }

    public static void spawnStructure(Location loc) {
        SereneWorldGen.plugin.getServer().getStructureManager().getStructures().values().stream().findAny().get().place(loc, false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
//        SereneWorldGen.plugin.getServer().getStructureManager().getStructures().values().stream().findAny().get().place(loc, false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
    }

}
