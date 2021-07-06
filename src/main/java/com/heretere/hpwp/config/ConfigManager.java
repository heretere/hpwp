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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.config.pojos.ConfigWorld;
import com.heretere.hpwp.config.pojos.GlobalVariables;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelsConfig;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;
import com.heretere.hpwp.libs.hch.yaml.YamlParser;
import com.heretere.hpwp.tasks.Chain;

public class ConfigManager {
    private final @NotNull PerWorldPlugins parent;
    private final @NotNull MultiConfigHandler configHandler;
    private final @NotNull Map<@NotNull String, @NotNull ConfigWorld> worlds;
    private final @NotNull GlobalVariables globalVariables;
    private final @NotNull ChatTunnelsConfig chatTunnelsConfig;

    public ConfigManager(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;
        this.configHandler = new MultiConfigHandler(parent.getDataFolder().toPath());

        this.configHandler.registerFileExtensionHandler(new YamlParser(this.configHandler), "yml");

        Map<Class<?>, Object> typeAdapters = new HashMap<>();

        this.configHandler.registerTypeAdapters(typeAdapters);

        this.worlds = new HashMap<>();

        this.globalVariables = this.configHandler.loadPOJOClass(GlobalVariables.class)
            .orElseThrow(() -> {
                this.configHandler.getErrors()
                    .forEach(error -> this.parent.getLogger().log(Level.SEVERE, error.getMessage(), error));
                return new IllegalStateException("Couldn't load global yml.");
            });

        this.chatTunnelsConfig = this.configHandler.loadPOJOClass(ChatTunnelsConfig.class)
            .orElseThrow(() -> {
                this.configHandler.getErrors()
                    .forEach(error -> this.parent.getLogger().log(Level.SEVERE, error.getMessage(), error));
                return new IllegalStateException("Couldn't load chat tunnels yml.");
            });

        Chain.IO.newChain()
            .async(
                () -> this.configHandler.getConfigByRelativePath("global.yml")
                    .ifPresent(config -> this.configHandler.saveConfig(config, true))
            )
            .async(
                () -> this.configHandler.getConfigByRelativePath("chat_tunnels.yml")
                    .ifPresent(config -> this.configHandler.saveConfig(config, true))
            )
            .sync(() -> this.configHandler.getErrors().forEach(Throwable::printStackTrace))
            .execute();
    }

    public @NotNull ConfigWorld getConfigFromWorld(final @NotNull World world) {
        return this.worlds.computeIfAbsent(
            world.getName(),
            w -> {
                this.parent.getLogger().info(() -> "Loading config for " + world.getName() + ".");
                final ConfigWorld cfgWorld = this.configHandler
                    .loadPOJOClassAtPath(world.getName() + ".yml", "", ConfigWorld.class)
                    .orElseThrow(
                        () -> {
                            this.configHandler.getErrors()
                                .forEach(
                                    error -> this.parent.getLogger()
                                        .log(Level.SEVERE, error.getMessage(), error)
                                );
                            return new IllegalStateException(
                                    String.format(
                                        "Couldn't probably load config for world '%s'.",
                                        world.getName()
                                    )
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

    public MultiConfigHandler getConfigHandler() {
        return configHandler;
    }

    public ChatTunnelsConfig getChatTunnelsConfig() {
        return chatTunnelsConfig;
    }
}
