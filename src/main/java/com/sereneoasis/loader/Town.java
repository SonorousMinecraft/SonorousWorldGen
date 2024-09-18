package com.sereneoasis.loader;

import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.TownUtils;
import com.sereneoasis.level.world.chunk.ChunkUtils;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class Town extends Civilisation{
    public Town(World world, int squareLength) {
        super(world, squareLength, (x,z) -> TownUtils.isInsideTownExclWalls(x, z ) && !KingdomUtils.isInsideKingdomExclWalls(x, z));
    }

    @Override
    void generateCivilisation(Chunk chunk) {
        int x = chunk.getX() * 16;

        int z = chunk.getZ() * 16;

        Location loc = new Location(world, x, ChunkUtils.getCurrentY(x, z) +1, z);

        if (GenerationNoise.getNoise(NoiseCategories.TOWN_PATHS, x ,z) < 0 ) {
                if (GenerationNoise.getNoise(NoiseCategories.TOWN_PATHS, x+32 ,z+32) < 0 ) {
                    if (!isPopulated( 2, x, z)) {

                        cacheSquare(world, 2, x, z);

                        pasteSchematic(loc, SereneWorldGen.getFileManager().getMediumHouseSchematics());
                    }
                } else {

                    if (GenerationNoise.getNoise(NoiseCategories.TOWN_PATHS, x+16 ,z+16) < 0 ) {
                        if (!isPopulated(1, x, z)) {
                            cacheSquare(world, 1, x, z);
                            pasteSchematic(loc, SereneWorldGen.getFileManager().getSmallHouseSchematics());
                        }
                    }
                }
            }
    }
}
