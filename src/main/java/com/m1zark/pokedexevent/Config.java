package com.m1zark.pokedexevent;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.pixelmonmod.pixelmon.config.PixelmonItemsFossils;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode main;

    public static boolean catching, hatching, evolution, playerTrade, npcTrade, storage, ot;
    public static boolean legendaries;
    public static List<String> whitelist;

    public Config() {
        this.loadConfig();
    }

    private void loadConfig(){
        Path configFile = Paths.get(Pokedexevent.getInstance().getConfigDir() + "/settings.conf");

        loader = HoconConfigurationLoader.builder().setPath(configFile).build();

        try {
            if (!Files.exists(Pokedexevent.getInstance().getConfigDir())) Files.createDirectory(Pokedexevent.getInstance().getConfigDir());

            if (!Files.exists(configFile)) Files.createFile(configFile);

            if (main == null) {
                main = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
            }

            CommentedConfigurationNode settings = main.getNode("Settings");
            settings.getNode("enable-catching").getBoolean(true);
            settings.getNode("hatching","enable-hatching").getBoolean(true);
            settings.getNode("enable-evolution").getBoolean(true);
            settings.getNode("enable-npc-trade").getBoolean(false);
            settings.getNode("enable-player-trade").getBoolean(false);
            settings.getNode("enable-storage-update").getBoolean(false);
            settings.getNode("legendary-override").getBoolean(true);
            settings.getNode("hatching","enable-ot-for-hatching").getBoolean(true);

            settings.getNode("whitelist").getList(TypeToken.of(String.class), Lists.newArrayList("Omanyte","Kabuto","Aerodactyl","Lileep","Anorith","Cranidos","Shieldon","Tirtouga","Archen","Tyrunt","Amaura"));

            loader.save(main);
        } catch (ObjectMappingException | IOException e) {
            e.printStackTrace();
            return;
        }

        loadRules();
    }

    private static void saveConfig() {
        try {
            loader.save(main);
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }

    public void reload() {
        try {
            main = loader.load();
            loadRules();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    private void loadRules() {
        try {
            catching = main.getNode("Settings","enable-catching").getBoolean();
            hatching = main.getNode("Settings","hatching","enable-hatching").getBoolean();
            ot = main.getNode("Settings","hatching","enable-ot-for-hatching").getBoolean();
            evolution = main.getNode("Settings","enable-evolution").getBoolean();
            npcTrade= main.getNode("Settings","enable-npc-trade").getBoolean();
            playerTrade = main.getNode("Settings","enable-player-trade").getBoolean();
            storage = main.getNode("Settings","enable-storage-update").getBoolean();
            legendaries = main.getNode("Settings","legendary-override").getBoolean();

            whitelist = main.getNode("Settings","whitelist").getList(TypeToken.of(String.class)).stream().filter(EnumSpecies::hasPokemonAnyCase).collect(Collectors.toList());
        } catch(ObjectMappingException e) {
            e.printStackTrace();
        }
    }
}
