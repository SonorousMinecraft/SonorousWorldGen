package com.sereneoasis.config;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;

public class FileManager {

    private final File mainDir, schemDir, chatDir, smallHouseDir, mediumHouseDir, bigHouseDir;

    public File getChatDir() {
        return chatDir;
    }

    public FileManager(){
        mainDir = getOrCreateDir("SereneWorldGen");
        schemDir = getOrCreateDir("Schematics", mainDir);
        smallHouseDir = getOrCreateDir("SmallHouses", schemDir);
        mediumHouseDir = getOrCreateDir("MediumHouses", schemDir);
        bigHouseDir = getOrCreateDir("BigHouses", schemDir);
        chatDir = getOrCreateDir("Chats", mainDir);
    }

    public File[] getSmallHouseSchematics(){
        return smallHouseDir.listFiles();
    }

    public File[] getMediumHouseSchematics(){
        return mediumHouseDir.listFiles();
    }
    public File[] getBigHouseSchematics(){
        return bigHouseDir.listFiles();
    }

    public File[] getSchematics(){

        return (File[]) ArrayUtils.addAll(getBigHouseSchematics(),ArrayUtils.addAll(getSmallHouseSchematics(), getMediumHouseSchematics()));
    }


    private static File getOrCreateDir(String name){
        File file = new File(name);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }

    private static File getOrCreateDir(String name, File parent){
        File file = new File(parent, name);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }
}
