/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hpwp.config;

import java.util.Map;
import java.util.logging.Level;

import com.heretere.hch.yaml.YamlParser;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.MapMaker;
import com.heretere.hch.core.MultiConfigHandler;
import com.heretere.hpwp.PerWorldPlugins;

public class ConfigManager {
    private final @NotNull PerWorldPlugins parent;
    private final @NotNull MultiConfigHandler configHandler;
    private final @NotNull Map<@NotNull String, @NotNull ConfigWorld> worlds;
    private final @NotNull GlobalVariables globalVariables;

    public ConfigManager(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;
        this.configHandler = new MultiConfigHandler(parent.getDataFolder().toPath());

        this.configHandler.registerFileExtensionHandler(new YamlParser(this.configHandler), "yml");

        this.worlds = new MapMaker()
            .initialCapacity(Bukkit.getWorlds().size())
            .weakKeys()
            .makeMap();

        this.globalVariables = this.configHandler.loadPOJOClass(GlobalVariables.class)
            .orElseThrow(() -> {
                this.configHandler.getErrors()
                    .forEach(error -> this.parent.getLogger().log(Level.SEVERE, error.getMessage(), error));
                return new IllegalStateException("Couldn't load global yml.");
            });

        this.configHandler.getConfigByRelativePath("global.yml")
            .ifPresent(config -> this.configHandler.saveConfig(config, true));
    }

    public void init() {
        Bukkit.getWorlds().forEach(this::getConfigFromWorld);
        this.save();
    }

    public void save() {
        if (!this.configHandler.saveAllConfigs(false)) {
            this.parent.getLogger().severe("Failed to save config files.");

            this.configHandler.getErrors()
                .forEach(error -> this.parent.getLogger().log(Level.SEVERE, error.getMessage(), error));
        }
    }

    public @NotNull ConfigWorld getConfigFromWorld(final @NotNull World world) {
        return this.worlds.computeIfAbsent(
            world.getName(),
            w -> {
                final ConfigWorld cfgWorld = this.configHandler
                    .loadPOJOClassAtPath(world.getName() + ".yml", "", ConfigWorld.class)
                    .orElseThrow(
                        () -> {
                            this.configHandler.getErrors()
                                .forEach(error -> this.parent.getLogger().log(Level.SEVERE, error.getMessage(), error));
                            return new IllegalStateException(
                                    String.format("Couldn't probably load config for world '%s'.", world.getName())
                            );
                        }
                    );

                this.configHandler.getConfigByRelativePath(world.getName() + ".yml")
                    .ifPresent(config -> this.configHandler.saveConfig(config, true));

                return cfgWorld;
            }
        );
    }

    public @NotNull GlobalVariables getGlobalVariables() {
        return this.globalVariables;
    }
}
