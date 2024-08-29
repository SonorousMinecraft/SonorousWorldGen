package com.sereneoasis.listeners;

import com.sereneoasis.SereneNPCs;
import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseTypes;
import com.sereneoasis.level.world.tree.TreeGenerationUtils;
import com.sereneoasis.npc.random.types.BasicNPC;
import com.sereneoasis.utils.ClientboundPlayerInfoUpdatePacketWrapper;
import com.sereneoasis.utils.NPCUtils;
import com.sereneoasis.utils.PacketUtils;
import com.sereneoasis.utils.StructureUtils;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class SereneListener implements Listener {


    private static final Random random = new Random();

    private static final List<String> buildings = List.of("/plains_armorer_house_1","/plains_big_house_1", "/plains_butcher_shop_1",
         "/plains_butcher_shop_2",   "/plains_cartographer_1", "/plains_fisher_cottage_1",   "/plains_fletcher_house_1",
            "/plains_library_1", "/plains_library_2", "/plains_masons_house_1", "/plains_medium_house_1", "/plains_medium_house_2",
            "/plains_shepherds_house_1", "/plains_small_house_1", "/plains_small_house_2", "/plains_small_house_3");

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){

        if (!event.isNewChunk()) {
            return;
        }
        Chunk chunk = event.getChunk();

        int snapshotX = random.nextInt(16);
        int y = 256;
        int snapshotZ = random.nextInt(16);

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

//            Schematics.pasteClipboard("fort1", loc);

//            Bukkit.getServer().getLogger().log(Level.INFO, loc.toString());
        }



//        if (KingdomUtils.isKingdomBuilding(x, z) && KingdomUtils.isInsideKingdom(x, z)) {
        if (false) {

//            int snapshotXLower = Math.max(0, snapshotX-5);
//            int snapshotXHigher = Math.min(15, snapshotX+5);
//
//
//            int snapshotZLower = Math.max(0, snapshotZ-5);
//            int snapshotZHigher = Math.min(15, snapshotZ+5);
//
//
//            if  ( (GenerationNoise.getNoise(NoiseTypes.KINGDOM_BUILDINGS,  snapshotXLower + chunk.getX() * 16 , snapshotZLower + chunk.getZ() * 16) > 0.7) &&
//                    GenerationNoise.getNoise(NoiseTypes.KINGDOM_BUILDINGS,   snapshotXHigher + chunk.getX() * 16 ,  snapshotZHigher  + chunk.getZ() * 16) > 0.7 ) {


            while (event.getChunk().getChunkSnapshot(false, false, false).getBlockType(snapshotX, y, snapshotZ).isAir() && y > -64) {
                    y--;
                    if (y == 0) {
                        return;
                    }
            }
            y+=1;
            int finalY = y;

            Location loc = new Location(event.getWorld(), x, finalY, z);
            loc.setYaw(90 * random.nextInt(0, 4));
                StructureUtils.spawnStructure(loc, "village/plains/houses" + buildings.get(random.nextInt(buildings.size())));
                BasicNPC npc = NPCUtils.spawnNPC(loc.clone(), Bukkit.getPlayer("Sakrajin"), "Villager");
                npcs.put(chunk, npc);
        }
//        }
    }

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
