package com.heretere.hpwp.config;

import com.google.common.collect.MapMaker;
import com.heretere.hch.MultiConfigHandler;
import com.heretere.hpwp.PerWorldPlugins;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class ConfigManager {
    private final @NotNull MultiConfigHandler configHandler;
    private final Map<World, ConfigWorld> worlds;

    public ConfigManager(final @NotNull PerWorldPlugins pwp) {
        this.configHandler = new MultiConfigHandler(pwp.getDataFolder().toPath());

        this.worlds = new MapMaker()
            .initialCapacity(Bukkit.getWorlds().size())
            .weakKeys()
            .makeMap();

        Bukkit.getWorlds().forEach(world -> {
            this.worlds.put(world, new ConfigWorld(this.configHandler, world));
        });

        try {
            this.configHandler.unload();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
