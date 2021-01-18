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

import com.google.common.collect.Maps;
import com.heretere.hch.MultiConfigHandler;
import com.heretere.hch.ProcessorType;
import com.heretere.hch.collection.ConfigSet;
import com.heretere.hch.structure.builder.ConfigBuilder;
import com.heretere.hch.structure.builder.ConfigPathBuilder;
import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.util.YamlEscapeUtils;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.logging.Level;

public final class ConfigWorld {
    private @NotNull Boolean check = true;
    private @NotNull ConfigSet<@NotNull String> whitelistedEvents;
    private @NotNull ConfigSet<@NotNull String> disabledPlugins;

    ConfigWorld(
        final @NotNull PerWorldPlugins parent,
        final @NotNull MultiConfigHandler handler,
        final @NotNull World world
    ) {
        this.whitelistedEvents = ConfigSet.newInstance(
            String.class,
            Collections.newSetFromMap(Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER))
        );

        this.whitelistedEvents.add("PlayerJoinEvent");
        this.whitelistedEvents.add("PlayerQuitEvent");
        this.whitelistedEvents.add("PlayerKickEvent");

        this.disabledPlugins = ConfigSet.newInstance(
            String.class,
            Collections.newSetFromMap(Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER))
        );

        this.disabledPlugins.add("Example Plugin 1");
        this.disabledPlugins.add("Example Plugin 2");
        this.disabledPlugins.add("Example Plugin 3");

        this.createConfigSection(parent, handler, world);
    }

    @SuppressWarnings("unchecked")
    private void createConfigSection(
        final @NotNull PerWorldPlugins parent,
        final @NotNull MultiConfigHandler handler,
        final @NotNull World world
    ) {
        try {
            final String worldKey = YamlEscapeUtils.escape(world.getName());
            ConfigBuilder.builder()
                         .setRelativePath(world.getName() + ".toml")
                         .addConfigPath(
                             ConfigPathBuilder
                                 .sectionBuilder()
                                 .addComment("Configuration for " + world.getName())
                                 .setKey(worldKey)
                                 .build()
                         )
                         .addConfigPath(
                             ConfigPathBuilder
                                 .fieldBuilder(Boolean.class)
                                 .addComment("Enable per world plugins for this world?")
                                 .setKey(worldKey + ".check_world")
                                 .setGetterSupplier(() -> this.check)
                                 .setSetterConsumer(check -> this.check = check)
                                 .build()
                         )
                         .addConfigPath(
                             ConfigPathBuilder
                                 .fieldBuilder(ConfigSet.class)
                                 .addComment("The events that run no matter what.")
                                 .setKey(worldKey + ".whitelisted_events")
                                 .setGetterSupplier(() -> this.whitelistedEvents)
                                 .setSetterConsumer(whitelist -> this.whitelistedEvents = whitelist)
                                 .build()
                         )
                         .addConfigPath(
                             ConfigPathBuilder
                                 .fieldBuilder(ConfigSet.class)
                                 .addComment("The plugin's disabled in this world.")
                                 .setKey(worldKey + ".disabled_plugins")
                                 .setGetterSupplier(() -> this.disabledPlugins)
                                 .setSetterConsumer(disabled -> this.disabledPlugins = disabled)
                                 .build()
                         )
                         .build(handler, ProcessorType.TOML);
        } catch (Exception e) {
            parent.getLogger().log(Level.SEVERE, "Could not load config world", e);

        }
    }

    public @NotNull Boolean getCheck() {
        return this.check;
    }

    public boolean eventEnabledForPlugin(
        final @NotNull Plugin plugin,
        final @NotNull Class<? extends Event> event
    ) {
        if (!Boolean.TRUE.equals(this.check)) {
            return true;
        }

        return !this.disabledPlugins.getBackingCollection().contains(plugin.getName())
            || this.whitelistedEvents.getBackingCollection().contains(event.getSimpleName());
    }

    public boolean commandEnabledForPlugin(
        final @NotNull Plugin plugin
    ) {
        if (!Boolean.TRUE.equals(this.check)) {
            return true;
        }

        return !this.disabledPlugins.getBackingCollection().contains(plugin.getName());
    }

}
