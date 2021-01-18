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

import com.google.common.collect.MapMaker;
import com.heretere.hch.MultiConfigHandler;
import com.heretere.hch.ProcessorType;
import com.heretere.hch.processor.exception.InvalidTypeException;
import com.heretere.hch.structure.annotation.Comment;
import com.heretere.hch.structure.annotation.ConfigFile;
import com.heretere.hch.structure.annotation.Key;
import com.heretere.hpwp.PerWorldPlugins;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

@ConfigFile("global.toml")
public class ConfigManager {
    @Key("messages.command_disabled_message")
    @Comment("The message that is sent when a plugin's command is disabled in a world")
    @Comment("Use '&' for color codes")
    private String commandDisabledMessage = "&cSorry, that command is disabled.";

    private final @NotNull PerWorldPlugins parent;
    private final @NotNull MultiConfigHandler configHandler;
    private final @NotNull Map<@NotNull String, @NotNull ConfigWorld> worlds;

    public ConfigManager(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;
        this.configHandler = new MultiConfigHandler(parent.getDataFolder().toPath());

        this.worlds = new MapMaker()
            .initialCapacity(Bukkit.getWorlds().size())
            .weakKeys()
            .makeMap();
    }

    public void init() {
        try {
            this.configHandler.loadConfigClass(this, ProcessorType.TOML);
        } catch (IllegalAccessException | InvalidTypeException | IOException e) {
            this.parent.getLogger().log(Level.SEVERE, "Could not properly load config.", e);
            Bukkit.getPluginManager().disablePlugin(this.parent);
            return;
        }
        Bukkit.getWorlds().forEach(this::getConfigFromWorld);
        this.save();
    }

    public void load() {
        try {
            this.configHandler.load();
        } catch (IllegalAccessException | InvalidTypeException | IOException e) {
            this.parent.getLogger().log(Level.SEVERE, "Could not properly load config.", e);
            Bukkit.getPluginManager().disablePlugin(this.parent);
        }
    }

    public void save() {
        this.load();
        try {
            this.configHandler.unload();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public @NotNull ConfigWorld getConfigFromWorld(final @NotNull World world) {
        return this.worlds.computeIfAbsent(
            world.getName(),
            w -> new ConfigWorld(this.parent, this.configHandler, world)
        );
    }

    public String getCommandDisabledMessage() {
        return this.commandDisabledMessage;
    }
}
