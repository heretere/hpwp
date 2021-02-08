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

package com.heretere.hpwp.listener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.config.ConfigWorld;

public class CommandPreProcessListener implements Listener {
    private static final Field COMMAND_MAP = FieldUtils.getField(Bukkit.getServer().getClass(), "commandMap", true);
    private static final Command EMPTY_COMMAND = new Command("HPWP_EMPTY") {
        @Override
        public boolean execute(
                final @NotNull CommandSender sender,
                final @NotNull String commandLabel,
                final @NotNull String[] args
        ) {
            return true;
        }
    };

    private final @NotNull PerWorldPlugins parent;
    private final @NotNull CommandMap commandMap;
    private final @NotNull Map<@NotNull String, @Nullable Command> commands;

    public CommandPreProcessListener(final @NotNull PerWorldPlugins parent) throws IllegalAccessException {
        this.parent = parent;
        this.commandMap = (CommandMap) COMMAND_MAP.get(Bukkit.getServer());
        this.commands = Maps.newHashMap();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreProcess(final @NotNull PlayerCommandPreprocessEvent e) {
        final ConfigWorld configWorld = this.parent.getConfigManager().getConfigFromWorld(e.getPlayer().getWorld());

        if (!configWorld.getCheck()) {
            return;
        }

        final String commandName = StringUtils.replaceOnce(StringUtils.split(e.getMessage(), " ", 2)[0], "/", "");

        final Command command = this.commands.computeIfAbsent(commandName, name -> {
            Command tmpCommand = this.commandMap.getCommand(name);

            if (tmpCommand == null) {
                for (Map.Entry<String, String[]> aliasMap : Bukkit.getCommandAliases().entrySet()) {

                    if (
                        Arrays.stream(aliasMap.getValue())
                            .filter(index -> index.equalsIgnoreCase(commandName))
                            .findAny()
                            .orElse(null) != null
                    ) {
                        tmpCommand = this.commandMap.getCommand(aliasMap.getKey());
                        break;
                    }
                }
            }

            return tmpCommand == null ? CommandPreProcessListener.EMPTY_COMMAND : tmpCommand;
        });

        if (!(command instanceof PluginIdentifiableCommand)) {
            return;
        }

        final PluginIdentifiableCommand pluginCommand = (PluginIdentifiableCommand) command;

        if (!configWorld.commandEnabledForPlugin(pluginCommand.getPlugin())) {
            e.setCancelled(true);
            e.getPlayer()
                .sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        this.parent.getConfigManager().getGlobalVariables().getCommandDisabledMessage()
                    )
                );
        }
    }
}
