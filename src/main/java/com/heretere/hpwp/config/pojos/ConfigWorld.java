/*
 * Project hpwp, 2021-07-13T19:01-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.config.pojos;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.heretere.hpwp.libs.hch.core.annotation.Comment;

import lombok.Data;

@Data
public final class ConfigWorld {

    @SerializedName("check_world")
    @Comment("Check for disabled plugins in this world?")
    private boolean check = true;

    @SerializedName("chat_tunnel_id")
    @Comment("The id of the chat tunnel this world is in.")
    @Comment("Used to show chat messages depending on the world.")
    private String chatTunnelId = "tunnel-1";

    @SerializedName("disabled_plugins_use_whitelist")
    @Comment("Set this to true if you want disabled_plugins to act as a whitelist.")
    private boolean whitelist = false;

    @SerializedName("whitelisted_events")
    @Comment("These events will run no matter what even if a plugin is disabled in a world.")
    @Comment("The default ones are used for compatibility reasons only remove these if you are sure it's safe.")
    private @NotNull Set<@NotNull String> whitelistedEvents;

    @SerializedName("disabled_plugins")
    @Comment("Define which plugins should be disabled.")
    @Comment("The name should be the name of the plugin you see when it starts up.")
    @Comment("or by using /plugins.")
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

    public boolean getCheck() {
        return this.check;
    }

    public boolean pluginEnabled(final @NotNull Plugin plugin) {
        return StringUtils.equals(plugin.getName(), "HPWP")
            || this.whitelist == this.disabledPlugins.contains(plugin.getName());
    }

    public boolean eventEnabledForPlugin(
            final @NotNull Plugin plugin,
            final @NotNull Class<? extends Event> event
    ) {
        if (!Boolean.TRUE.equals(this.check)) {
            return true;
        }

        return this.whitelistedEvents.contains(event.getSimpleName()) || this.pluginEnabled(plugin);
    }

    public boolean commandEnabledForPlugin(
            final @NotNull Plugin plugin
    ) {
        if (!Boolean.TRUE.equals(this.check)) {
            return true;
        }

        return this.pluginEnabled(plugin);
    }
}
