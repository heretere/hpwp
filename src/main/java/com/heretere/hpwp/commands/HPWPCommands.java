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

import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.gui.main.MainMenu;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

@CommandAlias("hpwp|pwp")
public final class HPWPCommands extends BaseCommand {
    @HelpCommand
    public static void onHelper(
            final @NotNull CommandHelp helper
    ) {
        helper.showHelp();
    }

    @Subcommand("events")
    @CommandPermission("hpwp.events")
    @Syntax("[plugin_name]")
    @CommandCompletion("@plugins")
    @Description("Lists all the events that have been ran by the specified plugin.")
    public static void onShowEvents(
            final @NotNull PerWorldPlugins parent,
            final @NotNull CommandSender sender,
            final @NotNull Plugin plugin
    ) {
        final Optional<ImmutableSet<Class<? extends Event>>> events = parent.getInjector().getEventsForPlugin(plugin);

        if (events.isPresent()) {
            sender.sendMessage(
                "Events registered for that plugin: "
                    + events.get()
                        .stream()
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "))
            );
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "No events found for that plugin."));
        }
    }

    @Subcommand("gui")
    @CommandPermission("hpwp.gui")
    @Syntax("")
    @Description("Configure HPWP through the GUI")
    public static void onShowGUI(MainMenu gui, Player sender) {
        gui.open(sender);
    }
}
