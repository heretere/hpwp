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

package com.heretere.hpwp.gui.main.worlds;

import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.config.pojos.ConfigWorld;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelsConfig;
import com.heretere.hpwp.gui.ConfigGui;
import com.heretere.hpwp.gui.util.items.Item;
import com.heretere.hpwp.gui.util.items.ItemFactory;
import com.heretere.hpwp.gui.util.items.Texture;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldConfigGui extends ConfigGui {
    private final ConfigWorld configWorld;
    private final GuiElementGroup plugins;

    protected WorldConfigGui(
            JavaPlugin parent,
            MultiConfigHandler handler,
            ConfigWorld configWorld,
            World world,
            ChatTunnelsConfig chatTunnelsConfig,
            ConfigGui backGui
    ) {
        super(
            parent,
            handler,
            handler.getConfigByRelativePath(world.getName() + ".yml").orElseThrow(IllegalArgumentException::new),
            "&r&0" + world.getName(),
            new String[] {
                " tb r ei ",
                "    c    ",
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.PAGINATION_LINE
            },
            backGui
        );

        this.configWorld = configWorld;

        this.plugins = new GuiElementGroup('d');
        super.getGui().addElement(this.plugins);

        GuiStateElement checkState = new GuiStateElement(
                't',
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setCheck(true);
                            super.save();
                        },
                        ConfigGui.STATE_ENABLED,
                        Item.ENABLED.getBuilder()
                            .name("&aPWP is currently enabled in this world.")
                            .loreLine("&7Click to &cdisable &7PWP in this world.")
                            .build()
                            .create()
                ),
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setCheck(false);
                            super.save();
                        },
                        ConfigGui.STATE_DISABLED,
                        Item.DISABLED.getBuilder()
                            .name("&cPWP is currently disabled in this world.")
                            .loreLine("&7Click to &aenable &7PWP in this world.")
                            .build()
                            .create()
                )
        );

        checkState.setState(this.configWorld.getCheck() ? ConfigGui.STATE_ENABLED : ConfigGui.STATE_DISABLED);
        super.getGui().addElement(checkState);

        GuiStateElement blacklistState = new GuiStateElement(
                'b',
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setWhitelist(false);
                            super.save();
                            this.refresh((Player) change.getWhoClicked());
                        },
                        "blacklist",
                        Item.BLACKLIST.getBuilder()
                            .name("&fBlacklist")
                            .loreLine("&7Click to change to a &ewhitelist&7.")
                            .build()
                            .create()
                ),
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setWhitelist(true);
                            super.save();
                            this.refresh((Player) change.getWhoClicked());
                        },
                        "whitelist",
                        Item.WHITELIST.getBuilder()
                            .name("&fWhitelist")
                            .loreLine("&7Click to change to a &eblacklist&7.")
                            .build()
                            .create()
                )
        );

        blacklistState.setState(this.configWorld.isWhitelist() ? "whitelist" : "blacklist");
        super.getGui().addElement(blacklistState);

        super.getGui().addElement(
            new StaticGuiElement(
                    'r',
                    Item.ENABLED.getBuilder()
                        .name("&aRefresh")
                        .loreLine("&7If you have added/removed plugins you can use")
                        .loreLine("&7This button to refresh the list.")
                        .build()
                        .create(),
                    click -> {
                        this.refresh((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        super.getGui().addElement(
            new StaticGuiElement(
                    'e',
                    Item.ENABLED.getBuilder()
                        .name("&aEnable all")
                        .loreLine("&7Set all plugins to &aenabled &7in this world.")
                        .build()
                        .create(),
                    click -> {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            if (this.configWorld.isWhitelist()) {
                                this.configWorld.getDisabledPlugins().add(plugin.getName());
                            } else {
                                this.configWorld.getDisabledPlugins().remove(plugin.getName());
                            }
                        }

                        super.save();
                        this.refresh((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        super.getGui().addElement(
            new StaticGuiElement(
                    'i',
                    Item.DISABLED.getBuilder()
                        .name("&cDisable all")
                        .loreLine("&7Set all plugins to &cdisabled &7in this world.")
                        .build()
                        .create(),
                    click -> {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            if (this.configWorld.isWhitelist()) {
                                this.configWorld.getDisabledPlugins().remove(plugin.getName());
                            } else {
                                this.configWorld.getDisabledPlugins().add(plugin.getName());
                            }
                        }

                        super.save();
                        this.refresh((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        super.getGui().addElement(
            new DynamicGuiElement(
                    'c',
                    viewer -> new StaticGuiElement(
                            'c',
                            ItemFactory.builder()
                                .base(XMaterial.PLAYER_HEAD)
                                .texture(Texture.MAILBOX)
                                .name("Set Chat Tunnel")
                                .loreLine("&7Click here to set the chat tunnel for this world.")
                                .loreLine(" ")
                                .loreLine("&eCurrent Tunnel: &r&f" + configWorld.getChatTunnelId())
                                .build()
                                .create(),
                            click -> {
                                new ChatTunnelSelectorConfigGui(
                                        parent,
                                        handler,
                                        chatTunnelsConfig,
                                        configWorld,
                                        world,
                                        this
                                )
                                    .open(
                                        (Player) click.getWhoClicked()
                                    );
                                return true;
                            }
                    )
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
        this.plugins.clearElements();

        Arrays.stream(Bukkit.getPluginManager().getPlugins())
            .sorted(Comparator.comparing(Plugin::getName))
            .forEach(plugin -> {
                if (plugin.getName().equals("HPWP")) {
                    return;
                }

                GuiStateElement pluginState = new GuiStateElement(
                        'p',
                        new GuiStateElement.State(
                                change -> {
                                    if (this.configWorld.isWhitelist()) {
                                        this.configWorld.getDisabledPlugins().add(plugin.getName());
                                    } else {
                                        this.configWorld.getDisabledPlugins().remove(plugin.getName());
                                    }

                                    super.save();
                                },
                                "enabled",
                                Item.ENABLED.getBuilder()
                                    .name("&a" + plugin.getName())
                                    .loreLine("&7Click to &cDisable &e" + plugin.getName() + " &7in this world.")
                                    .build()
                                    .create()
                        ),
                        new GuiStateElement.State(
                                change -> {
                                    if (this.configWorld.isWhitelist()) {
                                        this.configWorld.getDisabledPlugins().remove(plugin.getName());
                                    } else {
                                        this.configWorld.getDisabledPlugins().add(plugin.getName());
                                    }

                                    super.save();
                                },
                                "disabled",
                                Item.DISABLED.getBuilder()
                                    .name("&c" + plugin.getName())
                                    .loreLine("&7Click to &aEnable &e" + plugin.getName() + " &7in this world.")
                                    .build()
                                    .create()
                        )
                );

                pluginState.setState(
                    this.configWorld.pluginEnabled(plugin) ? ConfigGui.STATE_ENABLED : ConfigGui.STATE_DISABLED
                );

                this.plugins.addElement(pluginState);
            });
    }

}
