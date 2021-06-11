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

package com.heretere.hpwp;

import com.heretere.hpwp.chat.ChatTunnelListener;
import com.heretere.hpwp.commands.CommandManager;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.gui.GUI;
import com.heretere.hpwp.injector.ListenerInjector;
import com.heretere.hpwp.injector.listener.CommandPreProcessListener;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Level;

@Plugin(name = "HPWP", version = "VERSION")
@ApiVersion(ApiVersion.Target.v1_13)
@LogPrefix("HPWP")
@Author("Heretere")
@Website("heretere.com")
@LoadOrder(PluginLoadOrder.STARTUP)
@Permission(name = "hpwp.events", desc = "Allows /pwp events", defaultValue = PermissionDefault.OP)
@Permission(name = "hpwp.gui", desc = "Allows use of /pwp gui", defaultValue = PermissionDefault.OP)
@Permission(
    name = "hpwp.chat.bypass",
    desc = "See all chat tunnels no matter what",
    defaultValue = PermissionDefault.FALSE
)
@Permission(
    name = "hpwp.*",
    desc = "Wildcard hpwp permission",
    defaultValue = PermissionDefault.OP,
    children = { @ChildPermission(name = "hpwp.events"), @ChildPermission(name = "hpwp.gui") }
)
@SoftDependency("ProtocolLib")
public final class PerWorldPlugins extends JavaPlugin {
    private @Nullable GUI gui;
    private @Nullable ConfigManager configManager;
    private @Nullable ListenerInjector injector;

    @Override
    public void onLoad() {
        this.gui = new GUI(this);
        this.configManager = new ConfigManager(this);
        this.injector = new ListenerInjector(this);
    }

    @Override
    public void onEnable() {
        if (this.gui == null || this.configManager == null || this.injector == null) {
            super.getLogger()
                .severe("HPWP Failed to start correctly.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.gui.load();

        try {
            new CommandManager(this);
            Bukkit.getPluginManager().registerEvents(new CommandPreProcessListener(this), this);
            Bukkit.getPluginManager().registerEvents(new ChatTunnelListener(this.configManager), this);
        } catch (IllegalAccessException e) {
            super.getLogger().log(Level.SEVERE, "Could not load command processor", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.injector.load();
    }

    @Override
    public void onDisable() {
        if (this.gui == null || this.configManager == null || this.injector == null) {
            return;
        }

        this.injector.unload();
    }

    public @NotNull GUI getGui() {
        return Objects.requireNonNull(this.gui);
    }

    public @NotNull ConfigManager getConfigManager() {
        return Objects.requireNonNull(this.configManager);
    }

    public @NotNull ListenerInjector getInjector() {
        return Objects.requireNonNull(this.injector);
    }
}
