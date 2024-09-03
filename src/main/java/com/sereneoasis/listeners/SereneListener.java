package com.sereneoasis.listeners;

import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.Schematics;
import com.sereneoasis.level.world.biome.BiomeRepresentation;
import com.sereneoasis.level.world.chunk.ChunkUtils;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;
import com.sereneoasis.level.world.noise.NoiseMaster;
import com.sereneoasis.level.world.tree.TreeGenerationUtils;
import com.sereneoasis.loader.ChunkLoader;
import com.sereneoasis.npc.random.types.BasicNPC;
import com.sereneoasis.utils.NPCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SereneListener implements Listener {


    private static final Random random = new Random();

    private static final List<String> buildings = List.of("/plains_armorer_house_1","/plains_big_house_1", "/plains_butcher_shop_1",
         "/plains_butcher_shop_2",   "/plains_cartographer_1", "/plains_fisher_cottage_1",   "/plains_fletcher_house_1",
            "/plains_library_1", "/plains_library_2", "/plains_masons_house_1", "/plains_medium_house_1", "/plains_medium_house_2",
            "/plains_shepherds_house_1", "/plains_small_house_1", "/plains_small_house_2", "/plains_small_house_3");


    private void genTrees(Chunk chunk, ChunkLoadEvent event){
        int snapshotX = 0;
        int y = 256;
//        int snapshotZ = random.nextInt(16);
        int snapshotZ = 0;

        int x = snapshotX + chunk.getX() * 16;
        int z = snapshotZ + chunk.getZ() * 16;

        if (random.nextDouble() < 0.002 && !KingdomUtils.isInsideKingdom(x, z)){

            while(event.getChunk().getChunkSnapshot(false, false, false).getBlockType(snapshotX, y, snapshotZ).isAir() && y > -64) {
                y--;
                if (y == 0 ){
                    return;
                }
            }

            Location loc = new Location(event.getWorld(), x, y, z);
            loc.setYaw(90 * random.nextInt(0, 4));

            TreeGenerationUtils.genRandomTree(loc, random);

//            Bukkit.getServer().getLogger().log(Level.INFO, loc.toString());
        }
    }

    Pair[] smallHouses = { new Pair<>("MedievalHouseSmall1",2) ,
            new Pair<>("MedievalHouseSmall2", 1),
                    new Pair<>("MedievalHouseSmall3", 1),
            new Pair<>("MedievalHouseSmall4", 2),
            new Pair<>("MedievalHouseSmall5",1),
            new Pair<>("Bakery-House", 1)};

    Pair[] mediumHouses =  { new Pair<>("MedievalHouseMedium1",2) ,
            new Pair<>("MedievalHouseMedium2", 1),
            new Pair<>("MedievalHouseMedium3", 1),
            new Pair<>("MedievalHouseFavorite", 2)};


    Pair[] largeHouses = { new Pair<>("MedievalHouseLarge1",2) ,
            new Pair<>("MedievalHouseLarge2", 2),
            new Pair<>("MedievalMainHouse", 2),
            new Pair<>("MedievalRiverHouse", 1),
            new Pair<>("MedievalFeastingHall",1)};

    private void pasteSchematic(Location loc, Pair[] pairArray, Chunk chunk){
        Random rand = new Random();
        Pair<String, Integer> pair = pairArray[rand.nextInt(pairArray.length) ];

        loadToPaste(pair, loc, chunk);
    }

    public interface PasteCommand {

        void paste();
    }

    public static final HashMap<Chunk, PasteCommand>pendingSchematics = new HashMap<>();

    private void loadToPaste(Pair<String, Integer> pair, Location loc, Chunk chunk){
//        Bukkit.getScheduler().runTaskLater(SereneWorldGen.plugin, () -> {
//
//            if (!chunk.getLoadLevel().equals(Chunk.LoadLevel.TICKING)) {
//                pasteWhenLoaded(pair, loc, chunk);
//            } else {
//                Schematics.pasteClipboard(pair.getA(), loc.clone().subtract(0, pair.getB(), 0));
//            }
//        }, 100L);
//        pendingSchematics.put(pair.getA(), loc.clone().subtract(0, pair.getB(), 0));
        pendingSchematics.put(chunk, () -> Schematics.pasteClipboard(pair.getA(), loc.clone().subtract(0, pair.getB(), 0)));
    }
    
    private static Set<Chunk> populatedChunks = new HashSet<>();
    
    private void cacheSquare(World world, int length, int x, int z){
        for (int newX = 0; newX<length; newX ++) {
            for (int newZ = 0; newZ < length; newZ++) {
                populatedChunks.add(world.getChunkAt(x + newX, z + newZ));
            }
        }
    }

    private boolean isPopulated(World world, int length, int x, int z){
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

    private void genKingdom(Chunk chunk, ChunkLoadEvent event){

        int x = chunk.getX() * 16;

        int z = chunk.getZ() * 16;

        Location loc = new Location(event.getWorld(), x, ChunkUtils.getCurrentY(x, z) +1, z);
//        loc.setYaw(90 * random.nextInt(0, 4));



        if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x ,z) < 0 ) {
                World world = event.getWorld();
                if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+48 ,z+48) < 0 ) {
                    if (!isPopulated(world, 3, x, z)) {
                        cacheSquare(world, 3, x, z);
                        pasteSchematic(loc, largeHouses, chunk);
                    }
                }
                else {
                    if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+32 ,z+32) < 0 ) {
                        if (!isPopulated(world, 2, x, z)) {

                            cacheSquare(world, 2, x, z);

                            pasteSchematic(loc, mediumHouses, chunk);
                        }
                    } else {

                        if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+16 ,z+16) < 0 ) {
                            if (!isPopulated(world, 1, x, z)) {
                                cacheSquare(world, 1, x, z);
                                pasteSchematic(loc, smallHouses, chunk);
                            }
                        }
                    }
                }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        if (!event.isNewChunk()) {
            return;
        }


        Chunk chunk = event.getChunk();
        chunk.addPluginChunkTicket(SereneWorldGen.plugin);

        genTrees(chunk, event);

        if (KingdomUtils.isInsideKingdom(chunk.getX() * 16, chunk.getZ() * 16)){
            genKingdom(chunk, event);
        }

//            while (event.getChunk().getChunkSnapshot(false, false, false).getBlockType(snapshotX, y, snapshotZ).isAir() && y > -64) {
//                    y--;
//                    if (y == 0) {
//                        return;
//                    }
//            }
//            y+=1;
//            int finalY = y;
//
//            Location loc = new Location(event.getWorld(), x, finalY, z);
//            loc.setYaw(90 * random.nextInt(0, 4));
//                StructureUtils.spawnStructure(loc, "village/plains/houses" + buildings.get(random.nextInt(buildings.size())));
//                BasicNPC npc = NPCUtils.spawnNPC(loc.clone(), Bukkit.getPlayer("Sakrajin"), "Villager");
//                npcs.put(chunk, npc);


    }


//    @EventHandler
//    public void onWorldLoad(WorldLoadEvent event){
//        if (event.getWorld().getName().equals("test")) {
//            World world = event.getWorld();
//            SereneWorldGen.chunkLoader = new ChunkLoader(world);
//        }
//    }

    private static final ConcurrentHashMap<Chunk, BasicNPC > npcs = new ConcurrentHashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
    }

    private static final ConcurrentHashMap<UUID, Biome> biomeTracker = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<UUID, Chunk> chunkTracker = new ConcurrentHashMap<>();



    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();

        if (player.getName().equals("Sakrajin")) {
            World world = player.getWorld();
            Biome newBiome = world.getBiome(playerMoveEvent.getTo());
            Biome previousBiome = biomeTracker.get(player.getUniqueId());

            if (previousBiome != newBiome) {
                player.sendTitle(newBiome.getKey().getKey().toString(), "Welcome!", 10, 40, 20);
                biomeTracker.put(player.getUniqueId(), newBiome);
            }
            Chunk previousChunk = chunkTracker.get(player.getUniqueId());

            Chunk newChunk = playerMoveEvent.getTo().getChunk();
            if (previousChunk == null ||  previousChunk.getX() != newChunk.getX() || previousChunk.getZ() != newChunk.getZ() ) {
//                Bukkit.broadcastMessage("new chunk");
                chunkTracker.put(player.getUniqueId(), newChunk);
                BasicNPC serverPlayer = npcs.get(newChunk);
                if (serverPlayer != null) {
//                    Bukkit.broadcastMessage("robot on");
                    NPCUtils.addNPC(player, serverPlayer);
                    NPCUtils.toggleOn(serverPlayer);
                }
//                npcs.forEach((chunk, npcMaster) -> {
//                    if (chunk.getX() != chunk.getX() || chunk.getZ() != chunk.getZ()) {
//                        NPCUtils.toggleOff(npcMaster);
//                    }
//                });
            }

        }
    }



}
