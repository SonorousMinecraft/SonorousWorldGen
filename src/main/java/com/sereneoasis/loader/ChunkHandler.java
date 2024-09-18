package com.sereneoasis.loader;

import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.Schematics;
import com.sereneoasis.level.world.chunk.ChunkUtils;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.world.ChunkLoadEvent;
import oshi.util.tuples.Pair;

import java.io.File;
import java.util.*;

public class ChunkHandler {


    private int squareLength = 200;


    public ChunkHandler(World world) {

        new Kingdom(world, squareLength);
//        new Town(world, squareLength);
    }



}