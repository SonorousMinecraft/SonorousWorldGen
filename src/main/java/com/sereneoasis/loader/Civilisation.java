package com.sereneoasis.loader;

import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.Schematics;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Civilisation {
    
    protected World world;

    public Civilisation(World world, int squareLength, BiPredicate<Integer, Integer> shouldGenerate){
        int currentX = -squareLength/2, currentZ = -squareLength/2;
        this.world = world;
        Thread chunkLoadingThread = new Thread(() -> {
            try {
                for (int x = currentX; x < squareLength / 2; x++) {
                    for (int z = currentZ; z < squareLength / 2; z++) {
                        if (shouldGenerate.test(x*16, z*16)) {
                            Chunk currentChunk = world.getChunkAt(x, z, true);
                            Bukkit.getScheduler().runTask(SereneWorldGen.plugin, () -> currentChunk.addPluginChunkTicket(SereneWorldGen.plugin));
                            schematicChunks.add(currentChunk);
                            Thread.sleep(50);

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
                    int x = chunk.getX() * 16;
                    int z = chunk.getZ() * 16;
                    generateCivilisation(chunk);
                }
            }
            schematicChunks.removeIf(chunk -> chunk.getLoadLevel().equals(Chunk.LoadLevel.ENTITY_TICKING));
        }, 1000L, 100L);
    }

    abstract void generateCivilisation(Chunk chunk);

    protected static Set<Chunk> schematicChunks = new HashSet<>();


    protected void pasteSchematic(Location loc, File[] schems){
        Bukkit.broadcastMessage("pasted");
        Random rand = new Random();
        File schem = schems[rand.nextInt(schems.length) ];

        Schematics.pasteClipboard(schem.getName(), loc.clone());
    }

    protected static Set<Chunk> populatedChunks = new HashSet<>();

    protected void cacheSquare(World world, int length, int x, int z){
        for (int newX = 0; newX<length; newX ++) {
            for (int newZ = 0; newZ < length; newZ++) {
                populatedChunks.add(world.getChunkAt(x + newX, z + newZ));
            }
        }
    }

    protected boolean isPopulated(int length, int x, int z){
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
}
