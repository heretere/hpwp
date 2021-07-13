/*
 * Project hpwp, 2021-07-13T19:01-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.gui.main.worlds;

import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.gui.ConfigGui;
import com.heretere.hpwp.gui.main.MainMenu;
import com.heretere.hpwp.gui.util.items.Item;
import com.heretere.hpwp.gui.util.items.WorldItems;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldSelectorConfigGui extends ConfigGui {
    private final JavaPlugin parent;
    private final ConfigManager configManager;
    private final MultiConfigHandler handler;
    private final GuiElementGroup worlds;

    public WorldSelectorConfigGui(
            JavaPlugin parent,
            ConfigManager configManager,
            MultiConfigHandler handler,
            MainMenu backGui
    ) {
        super(
            parent,
            handler,
            null,
            "&r&0Select World",
            new String[] {
                "    r    ",
                ConfigGui.GROUP_LINE,
                ConfigGui.PAGINATION_LINE
            },
            backGui
        );

        this.parent = parent;
        this.configManager = configManager;
        this.handler = handler;

        this.worlds = new GuiElementGroup('d');
        super.getGui().addElement(this.worlds);

        super.getGui().addElement(
            new StaticGuiElement(
                    'r',
                    Item.ENABLED.getBuilder()
                        .name("&aRefresh")
                        .loreLine("&7If you have added/removed worlds you can use")
                        .loreLine("&7This button to refresh the list.")
                        .build()
                        .create(),
                    click -> {
                        this.refresh((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        super.attachPagination();
        this.refreshElements();
    }

    @Override
    public void refresh(Player player) {
        this.refreshElements();
        super.refresh(player);
    }

    private void refreshElements() {
        this.worlds.clearElements();
        Bukkit.getWorlds()
            .stream()
            .sorted(Comparator.comparing(World::getEnvironment).thenComparing(World::getName))
            .forEach(
                world -> this.worlds.addElement(
                    new StaticGuiElement(
                            'd',
                            WorldItems.getWorldForEnvironment(world.getEnvironment())
                                .selectRandom()
                                .name(world.getName())
                                .loreLine("&7Click to configure settings for this world.")
                                .build()
                                .create(),
                            click -> {
                                new WorldConfigGui(
                                        this.parent,
                                        this.handler,
                                        this.configManager.getConfigFromWorld(world),
                                        world,
                                        this.configManager.getChatTunnelsConfig(),
                                        this
                                )
                                    .open((Player) click.getWhoClicked());
                                return true;
                            }
                    )
                )
            );
    }
}
