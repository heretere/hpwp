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

package com.heretere.hpwp.injector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;
import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.listener.HPWPListener;
import com.heretere.hpwp.listener.HPWPRegisteredListener;
import com.heretere.hpwp.listener.HPWPTimedRegisteredListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ListenerInjector extends BukkitRunnable implements Listener {
    private static final long TICKS = 20L * 60L * 5L;

    private final @NotNull PerWorldPlugins parent;
    private final @NotNull Map<@NotNull Plugin, @Nullable Set<@NotNull Class<? extends Event>>> events;
    private boolean loaded = true;

    public ListenerInjector(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;
        this.events = new MapMaker().weakKeys().makeMap();
    }

    public void registerEvent(
        final @NotNull Plugin plugin,
        final @NotNull Class<? extends Event> event
    ) {
        this.events
            .computeIfAbsent(plugin, p -> Collections.newSetFromMap(new MapMaker().weakKeys().makeMap()))
            .add(event);
    }

    @Override
    public void run() {
        if (!this.loaded) {
            return;
        }

        HandlerList.getHandlerLists().forEach(handlerList -> {
            for (RegisteredListener listener : handlerList.getRegisteredListeners()) {
                if (listener.getPlugin() == this.parent
                    || listener instanceof HPWPListener) {
                    continue;
                }

                handlerList.unregister(listener);

                try {
                    handlerList
                        .register(
                            listener instanceof TimedRegisteredListener
                                ?
                                new HPWPTimedRegisteredListener(
                                    this.parent,
                                    (TimedRegisteredListener) listener
                                )
                                : new HPWPRegisteredListener(
                                    this.parent,
                                    listener
                                )
                        );
                } catch (Exception e) {
                    handlerList.register(listener);
                    this.parent.getLogger()
                               .severe(() -> "Failed to inject handler into " + listener.getPlugin() + ".");
                }
            }
        });
    }

    public void load() {
        Bukkit.getPluginManager().registerEvents(this, this.parent);
        super.runTaskTimer(this.parent, 0, ListenerInjector.TICKS);
    }

    public @NotNull Optional<ImmutableSet<Class<? extends Event>>> getEventsForPlugin(final @NotNull Plugin plugin) {
        final Set<Class<? extends Event>> tmpEvents = this.events.get(plugin);

        if (tmpEvents == null) {
            return Optional.empty();
        } else {
            return Optional.of(ImmutableSet.copyOf(tmpEvents));
        }
    }

    public void unload() {
        this.loaded = false;
        HandlerList.getHandlerLists().forEach(handlerList -> {
            for (RegisteredListener listener : handlerList.getRegisteredListeners()) {
                if (!(listener instanceof HPWPListener)) {
                    continue;
                }

                handlerList.unregister(listener);

                try {
                    handlerList.register(((HPWPListener) listener).getDelegate());
                } catch (Exception e) {
                    this.parent.getLogger().severe("Failed to detach HPWP from listener pipeline.");
                }
            }
        });
    }

    @EventHandler
    public void onEnable(final @NotNull PluginEnableEvent e) {
        if (e.getPlugin() != this.parent) {
            this.run();
        }
    }

    @EventHandler
    public void onDisable(final @NotNull PluginDisableEvent e) {
        if (e.getPlugin() != this.parent) {
            this.run();
        }
    }
}
