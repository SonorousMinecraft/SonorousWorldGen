//package com.sereneoasis.loader;
//
//import com.sereneoasis.SereneWorldGen;
//import com.sereneoasis.listeners.SereneListener;
//import org.bukkit.Bukkit;
//import org.bukkit.Chunk;
//import org.bukkit.World;
//
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//
//public class ChunkLoader {
//
//    private final World world;
//
//    private int squareLength = 20;
//
//    private int currentX = -squareLength/2, currentZ = -squareLength/2;
//
//    private Set<Chunk>nonTickingChunks = new HashSet<>();
//
//    public ChunkLoader(World world){
//        this.world = world;
//
//        Thread chunkLoadingThread = new Thread(() -> {
//            try {
//                for (int x = currentX; x <squareLength/2; x++){
//                    for (int z = currentZ; z <squareLength/2; z++) {
//                        Bukkit.broadcastMessage("chunk loaded " + x + ", " + z);
//                        Chunk currentChunk = world.getChunkAt(x, z, true);
////                    currentChunk.addPluginChunkTicket(SereneWorldGen.plugin);
//                        nonTickingChunks.add(currentChunk);
//                        Thread.sleep(50);
//
//                    }
//                }
//
//            } catch(InterruptedException v) {
//                System.out.println(v);
//            }
//        });
//
//        chunkLoadingThread.start();
////
////        Bukkit.getScheduler().scheduleSyncRepeatingTask(SereneWorldGen.plugin, () -> {
////            Iterator<Chunk>it = nonTickingChunks.iterator();
////            while(it.hasNext()){
////                Chunk chunk = it.next();
////                if (chunk.getLoadLevel().equals(Chunk.LoadLevel.TICKING)){
////                    SereneListener.pendingSchematics.forEach((key, value) -> {
////                        if ((key.getX() == chunk.getX()) &&
////                                (key.getZ() == chunk.getZ())) {
////                            value.paste();
////                        }
////                    });
////                    it.remove();
////                }
////            };
////        }, 1000L, 20L );
//    }
//
//
//}
