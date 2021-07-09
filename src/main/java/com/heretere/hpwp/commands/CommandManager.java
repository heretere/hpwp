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

package com.heretere.hpwp.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.gui.main.MainMenu;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;

public final class CommandManager {
    private final PerWorldPlugins parent;
    private final PaperCommandManager manager;

    public CommandManager(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;

        this.manager = new PaperCommandManager(parent);
        this.manager.enableUnstableAPI("help");

        this.registerCommandContexts();
        this.registerCommandCompletions();

        this.manager.registerCommand(new HPWPCommands());
    }

    private void registerCommandContexts() {
        this.manager
            .getCommandContexts()
            .registerIssuerOnlyContext(PerWorldPlugins.class, c -> this.parent);

        this.manager
            .getCommandContexts()
            .registerIssuerOnlyContext(MainMenu.class, c -> this.parent.getGui());

        this.manager
            .getCommandContexts()
            .registerContext(Plugin.class, c -> {
                final String pluginName = c.popFirstArg();

                return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(plugin -> StringUtils.equalsIgnoreCase(plugin.getName(), pluginName))
                    .findAny()
                    .orElseThrow(() -> new InvalidCommandArgument("Plugin with that name not found.", false));
            });
    }

    private void registerCommandCompletions() {
        this.manager
            .getCommandCompletions()
            .registerAsyncCompletion(
                "plugins",
                c -> Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .map(Plugin::getName)
                    .collect(Collectors.toList())
            );
    }
}
