package com.m1zark.pokedexevent;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Getter
@Plugin(id="pokedexevent",name ="PokeDexEvent",authors = "m1zark", description = "Enable or disable dex updates")
public class Pokedexevent {
    @Inject private Logger logger;
    @Inject private PluginContainer pluginContainer;
    private static Pokedexevent instance;
    @Inject @ConfigDir(sharedRoot = false) private Path configDir;
    private Config config;

    @Listener public void onInitialization(GamePreInitializationEvent e) {
        instance = this;
        config = new Config();
        Pixelmon.EVENT_BUS.register(new PixelmonListener());
    }

    @Listener public void onReload(GameReloadEvent e) {
        config.reload();
    }

    public static Pokedexevent getInstance() {
        return instance;
    }
}
