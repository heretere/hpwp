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

import java.util.Collections;
import java.util.Set;

import com.google.gson.annotations.SerializedName;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Maps;

public final class ConfigWorld {
    @SerializedName("check_world")
    private @NotNull boolean check = true;
    @SerializedName("whitelisted_events")
    private @NotNull Set<@NotNull String> whitelistedEvents;
    @SerializedName("disabled_plugins")
    private @NotNull Set<@NotNull String> disabledPlugins;

    ConfigWorld() {
        this.whitelistedEvents = Collections.newSetFromMap(Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER));

        this.whitelistedEvents.add("PlayerJoinEvent");
        this.whitelistedEvents.add("PlayerQuitEvent");
        this.whitelistedEvents.add("PlayerKickEvent");

        this.disabledPlugins = Collections.newSetFromMap(Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER));
        this.disabledPlugins.add("Example Plugin 1");
        this.disabledPlugins.add("Example Plugin 2");
        this.disabledPlugins.add("Example Plugin 3");
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

        return !this.disabledPlugins.contains(plugin.getName())
            || this.whitelistedEvents.contains(event.getSimpleName());
    }

    public boolean commandEnabledForPlugin(
            final @NotNull Plugin plugin
    ) {
        if (!Boolean.TRUE.equals(this.check)) {
            return true;
        }

        return !this.disabledPlugins.contains(plugin.getName());
    }

}
