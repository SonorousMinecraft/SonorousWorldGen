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

    private final World world;

    private int squareLength = 1000;

    private int currentX = -squareLength/2, currentZ = -squareLength/2;

    private static Set<Chunk> schematicChunks = new HashSet<>();


    private void pasteSchematic(Location loc, File[] schems, Chunk chunk){
        Bukkit.broadcastMessage("pasted");
        Random rand = new Random();
        File schem = schems[rand.nextInt(schems.length) ];

        Schematics.pasteClipboard(schem.getName(), loc.clone());
    }


    private static Set<Chunk> populatedChunks = new HashSet<>();

    private void cacheSquare(World world, int length, int x, int z){
        for (int newX = 0; newX<length; newX ++) {
            for (int newZ = 0; newZ < length; newZ++) {
                populatedChunks.add(world.getChunkAt(x + newX, z + newZ));
            }
        }
    }

    private boolean isPopulated(int length, int x, int z){
        for (int newX = 0; newX<length; newX ++) {
            for (int newZ = 0; newZ < length; newZ++) {
                int chunkX = x + newX;
                int chunkZ = z + newZ;
                if (populatedChunks.stream().anyMatch(chunk -> (chunk.getX() == chunkX ) && (chunk.getZ() == chunkZ ) )){
                    return true;
                }
            }
        }
        return false;
    }


    public ChunkHandler(World world) {
        this.world = world;

        Thread chunkLoadingThread = new Thread(() -> {
            try {
                for (int x = currentX; x < squareLength / 2; x++) {
                    for (int z = currentZ; z < squareLength / 2; z++) {
                        if (KingdomUtils.isInsideKingdomExclWalls(x * 16, z * 16)) {
                            Bukkit.broadcastMessage("chunk loaded " + x + ", " + z);
                            Chunk currentChunk = world.getChunkAt(x, z, true);
                            Bukkit.getScheduler().runTask(SereneWorldGen.plugin, () -> currentChunk.addPluginChunkTicket(SereneWorldGen.plugin));

                            schematicChunks.add(currentChunk);
                            Thread.sleep(200);

                        }


                    }
                }

            } catch (InterruptedException v) {
                System.out.println(v);
            }
        });
        chunkLoadingThread.start();


        Bukkit.getScheduler().scheduleSyncRepeatingTask(SereneWorldGen.plugin, () -> {
            for (Chunk chunk : schematicChunks) {
                if (chunk.getLoadLevel().equals(Chunk.LoadLevel.ENTITY_TICKING)) {
                    genKingdom(chunk);
                }
            }
            schematicChunks.removeIf(chunk -> chunk.getLoadLevel().equals(Chunk.LoadLevel.ENTITY_TICKING));
        }, 1000L, 100L);
    }


    private void genKingdom(Chunk chunk){

        int x = chunk.getX() * 16;

        int z = chunk.getZ() * 16;

        Location loc = new Location(world, x, ChunkUtils.getCurrentY(x, z) +1, z);
//        loc.setYaw(90 * random.nextInt(0, 4));



        if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x ,z) < 0 ) {
            if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+48 ,z+48) < 0 ) {
                if (!isPopulated(3, x, z)) {
                    cacheSquare(world, 3, x, z);
                    pasteSchematic(loc, SereneWorldGen.getFileManager().getBigHouseSchematics(), chunk);
                }
            }
            else {
                if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+32 ,z+32) < 0 ) {
                    if (!isPopulated( 2, x, z)) {

                        cacheSquare(world, 2, x, z);

                        pasteSchematic(loc, SereneWorldGen.getFileManager().getMediumHouseSchematics(), chunk);
                    }
                } else {

                    if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+16 ,z+16) < 0 ) {
                        if (!isPopulated(1, x, z)) {
                            cacheSquare(world, 1, x, z);
                            pasteSchematic(loc, SereneWorldGen.getFileManager().getSmallHouseSchematics(), chunk);
                        }
                    }
                }
            }
        }
    }



}