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

import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.heretere.hdl.dependency.maven.annotation.MavenDependency;
import com.heretere.hdl.dependency.maven.annotation.MavenRepository;
import com.heretere.hdl.exception.DependencyLoadException;
import com.heretere.hdl.relocation.annotation.Relocation;
import com.heretere.hdl.spigot.DependencyPlugin;
import com.heretere.hpwp.commands.CommandManager;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.injector.ListenerInjector;
import com.heretere.hpwp.listener.CommandPreProcessListener;

@Plugin(name = "HPWP", version = "VERSION")
@ApiVersion(ApiVersion.Target.v1_13)
@LogPrefix("HPWP")

@MavenRepository("https://jitpack.io")
@MavenDependency("com|github|heretere:hch:v1.1.0")
@MavenDependency("org|tomlj:tomlj:1.0.0")
@MavenDependency("org|antlr:antlr4-runtime:4.7.2")
@Relocation(from = "org|tomlj", to = "com|heretere|hpwp|libs|hdl|tomlj")
@Relocation(from = "org|antlr", to = "com|heretere|hpwp|libs|hdl|antlr")
@Relocation(from = "com|heretere|hch", to = "com|heretere|hpwp|libs|hch")
public final class PerWorldPlugins extends DependencyPlugin {
    private @Nullable ConfigManager configManager;
    private @Nullable ListenerInjector injector;

    @Override
    protected void fail(
            final @NotNull Set<@NotNull Throwable> set,
            final @NotNull Set<@NotNull DependencyLoadException> set1
    ) {
        if (!set1.isEmpty()) {
            super.getLogger()
                .severe(
                    "HPWP failed to download dependencies please download them from the link provided"
                        +
                        "below and place them in plugins/HPWP/dependencies/maven"
                );

            set1.forEach(
                load -> super.getLogger()
                    .severe(() -> {
                        try {
                            return String.format(
                                "Failed to download '%s'. Please download from '%s' and place it in"
                                    + "plugins/HPWP/dependencies/maven/",
                                load.getDependency().getName(),
                                "https://repo1.maven.org/maven2/" + load.getDependency().getManualDownloadURL()
                            );
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return "";
                        }
                    })
            );
        } else if (!set.isEmpty()) {
            super.getLogger().severe(
                "HPWP was unable to load dependencies. Please look at the errors below to determine the issue."
            );
            set.forEach(throwable -> super.getLogger().log(Level.SEVERE, throwable.getMessage(), throwable));
        }
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void load() {
        this.configManager = new ConfigManager(this);
        this.injector = new ListenerInjector(this);
    }

    @Override
    public void enable() {
        if (this.configManager == null || this.injector == null) {
            super.getLogger()
                .severe("HPWP Failed to start correctly.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            new CommandManager(this);
            Bukkit.getPluginManager().registerEvents(new CommandPreProcessListener(this), this);
        } catch (IllegalAccessException e) {
            super.getLogger().log(Level.SEVERE, "Could not load command processor", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.configManager.init();
        this.injector.load();
    }

    @Override
    public void disable() {
        if (this.configManager == null || this.injector == null) {
            return;
        }

        this.configManager.save();
        this.injector.unload();
    }

    public @NotNull ConfigManager getConfigManager() {
        return Objects.requireNonNull(this.configManager);
    }

    public @NotNull ListenerInjector getInjector() {
        return Objects.requireNonNull(this.injector);
    }
}
