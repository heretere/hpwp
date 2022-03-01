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

package com.heretere.hpwp;

import java.util.Objects;
import java.util.logging.Level;

import com.heretere.hpwp.hooks.ChatTunnelExpansion;
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

import com.heretere.hpwp.chat.ChatTunnelListener;
import com.heretere.hpwp.commands.CommandManager;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.gui.main.MainMenu;
import com.heretere.hpwp.injector.ListenerInjector;
import com.heretere.hpwp.injector.listener.CommandPreProcessListener;
import com.heretere.hpwp.update.UpdaterRunnable;

@Plugin(name = "HPWP", version = "VERSION")
@ApiVersion(ApiVersion.Target.v1_13)
@LogPrefix("HPWP")
@Author("Heretere")
@Website("heretere.com")
@LoadOrder(PluginLoadOrder.STARTUP)
@SoftDependency("PlaceholderAPI")
@Permission(name = "hpwp.events", desc = "Allows /pwp events", defaultValue = PermissionDefault.OP)
@Permission(name = "hpwp.gui", desc = "Allows use of /pwp gui", defaultValue = PermissionDefault.OP)
@Permission(name = "hpwp.notify", desc = "Receive update notifications", defaultValue = PermissionDefault.OP)
@Permission(
    name = "hpwp.bypass.chat",
    desc = "Send and see all chat tunnels",
    defaultValue = PermissionDefault.FALSE
)
@Permission(
    name = "hpwp.*",
    desc = "Wildcard hpwp permission",
    defaultValue = PermissionDefault.OP,
    children = {
        @ChildPermission(name = "hpwp.events"),
        @ChildPermission(name = "hpwp.gui"),
        @ChildPermission(name = "hpwp.notify") }
)
@Permission(
    name = "hpwp.toggle.chat",
    desc = "Enables the ability to run /pwp togglechat",
    defaultValue = PermissionDefault.OP
)
@Permission(
    name = "hpwp.bypass.*",
    desc = "Wildcard hpwp bypass permission",
    defaultValue = PermissionDefault.FALSE,
    children = {
        @ChildPermission(name = "hpwp.bypass.chat")
    }
)
public final class PerWorldPlugins extends JavaPlugin {
    private @Nullable MainMenu gui;
    private @Nullable ConfigManager configManager;
    private @Nullable ListenerInjector injector;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.injector = new ListenerInjector(this);
        this.gui = new MainMenu(this, this.configManager);

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

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatTunnelExpansion(this.configManager).register();
        }

        new UpdaterRunnable(this, this.configManager.getGlobalVariables()).load();
    }

    @Override
    public void onDisable() {
        if (this.gui == null || this.configManager == null || this.injector == null) {
            return;
        }

        this.injector.unload();
    }

    public @NotNull MainMenu getGui() {
        return Objects.requireNonNull(this.gui);
    }

    public @NotNull ConfigManager getConfigManager() {
        return Objects.requireNonNull(this.configManager);
    }

    public @NotNull ListenerInjector getInjector() {
        return Objects.requireNonNull(this.injector);
    }
}
