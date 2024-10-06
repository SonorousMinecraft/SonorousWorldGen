package com.sereneoasis.loader;

import com.sereneoasis.SereneWorldGen;
import com.sereneoasis.level.world.KingdomUtils;
import com.sereneoasis.level.world.chunk.ChunkUtils;
import com.sereneoasis.level.world.noise.GenerationNoise;
import com.sereneoasis.level.world.noise.NoiseCategories;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;

public class Kingdom extends Civilisation{
    public Kingdom(World world, int squareLength) {
        super(world, squareLength, (x,z) -> KingdomUtils.isInsideKingdomExclWalls(x, z ));
    }

    @Override
    void generateCivilisation(Chunk chunk) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int x = chunk.getX() * 16;

        int z = chunk.getZ() * 16;

        Location loc = new Location(world, x, ChunkUtils.getCurrentY(x, z) +1, z);

        if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x ,z) < 0 ) {
            if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+48 ,z+48) < 0 ) {
                if (!isPopulated(3, chunk.getX(), chunk.getZ())) {
                    cacheSquare(world, 3, chunk.getX(), chunk.getZ());
                    pasteSchematic(loc, SereneWorldGen.getFileManager().getBigHouseSchematics());
                }
            }
            else {
                if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+32 ,z+32) < 0 ) {
                    if (!isPopulated( 2, chunk.getX(), chunk.getZ())) {

                        cacheSquare(world, 2, chunk.getX(), chunk.getZ());

                        pasteSchematic(loc, SereneWorldGen.getFileManager().getMediumHouseSchematics());
                    }
                } else {

                    if (GenerationNoise.getNoise(NoiseCategories.KINGDOM_PATHS, x+16 ,z+16) < 0 ) {
                        if (!isPopulated(1, chunk.getX(), chunk.getZ())) {
                            cacheSquare(world, 1, chunk.getX(), chunk.getZ());
                            pasteSchematic(loc, SereneWorldGen.getFileManager().getSmallHouseSchematics());
                        }
                    }
                }
            }
        }
    }
}
