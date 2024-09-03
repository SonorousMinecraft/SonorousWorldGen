package com.sereneoasis;

import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.config.FileManager;
import com.sereneoasis.level.world.biome.BiomeRepresentation;
import com.sereneoasis.level.world.chunk.CustomChunkGenerator;
import com.sereneoasis.level.world.noise.NoiseMaster;
import com.sereneoasis.listeners.SereneListener;
import com.sereneoasis.utils.NPCUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/***
 * The main class of the plugin
 */
public class SereneWorldGen extends JavaPlugin {

    public static SereneWorldGen plugin;

    private static FileManager fileManager;

    /***
     * Returns the class used to manage files
     * @return the file manager
     */
    public static FileManager getFileManager() {
        return fileManager;
    }


    @Override
    public void onEnable() {

        getLogger().log(Level.INFO, "WorldGenerator was enabled successfully.");
        plugin = this;
        fileManager = new FileManager();
        this.getServer().getPluginManager().registerEvents(new SereneListener(), this);
        this.getCommand("SereneWorldGen").setExecutor(new SerenityCommand());

        NPCUtils.initUUID(0, this);
        BiomeRepresentation.initBiomes();
        NoiseMaster.initNoise();
    }


    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "WorldGenerator was disabled successfully.");
    }

//    public static ChunkLoader chunkLoader;

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {


        getLogger().log(Level.WARNING, "CustomChunkGenerator is used!");

        return new CustomChunkGenerator();
    }

}