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

package com.heretere.hpwp.gui.elements;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.config.ConfigWorld;
import com.heretere.hpwp.gui.GuiPage;
import com.heretere.hpwp.gui.Items;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static com.heretere.hpwp.util.ChatUtils.translate;

public class WorldMenu implements GuiPage {
    private static final String[] ELEMENTS = {
        " tb r ed ",
        "         ",
        " ppppppp ",
        " ppppppp ",
        " ppppppp ",
        " fa   nl "
    };

    private final @NotNull PerWorldPlugins parent;

    private @Nullable InventoryGui gui;
    private @Nullable GuiElementGroup plugins;

    private final @NotNull ConfigWorld configWorld;
    private final @NotNull String name;

    public WorldMenu(final @NotNull PerWorldPlugins parent, final @NotNull World world) {
        this.parent = parent;
        this.configWorld = parent.getConfigManager().getConfigFromWorld(world);
        this.name = world.getName();

        this.load(parent);
    }

    private void saveConfig() {
        this.parent.getConfigManager()
            .getConfigHandler()
            .getConfigByRelativePath(this.name + ".yml")
            .ifPresent(
                config -> this.parent.getConfigManager()
                    .getConfigHandler()
                    .saveConfig(config, true)
            );
    }

    @Override
    public void load(final @NotNull PerWorldPlugins parent) {
        this.gui = new InventoryGui(parent, this.name, ELEMENTS);
        this.gui.setFiller(Items.FILLER.getItem());
        this.drawPlugins();

        GuiStateElement checkState = new GuiStateElement(
                't',
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setCheck(true);
                            this.saveConfig();
                        },
                        "enabled",
                        Items.ENABLED.getItem(),
                        translate("&aHPWP &eis currently &achecking this world."),
                        translate("&fClick to &edisable &fHPWP in this world.")
                ),
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setCheck(false);
                            this.saveConfig();
                        },
                        "disabled",
                        Items.DISABLED.getItem(),
                        translate("&cHPWP is &enot currently &cchecking this worlld."),
                        translate("&fClick to &eenable &fHPWP in this world.")
                )
        );

        checkState.setState(this.configWorld.isCheck() ? "enabled" : "disabled");
        this.gui.addElement(checkState);

        this.gui.addElement(
            new StaticGuiElement(
                    'r',
                    Items.ENABLED.getItem(),
                    click -> {
                        this.drawPlugins();
                        click.getGui().draw();
                        return true;
                    },
                    translate("&aRefresh"),
                    translate("&eIf you have added/removed plugins you can use"),
                    translate("&eThis button to refresh the list.")
            )
        );

        this.gui.addElement(
            new StaticGuiElement(
                    'e',
                    Items.ENABLED.getItem(),
                    click -> {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            if (this.configWorld.isWhitelist()) {
                                this.configWorld.getDisabledPlugins().add(plugin.getName());
                            } else {
                                this.configWorld.getDisabledPlugins().remove(plugin.getName());
                            }
                        }

                        this.saveConfig();

                        this.drawPlugins();
                        click.getGui().draw();

                        return true;
                    },
                    translate("&aEnable all"),
                    translate("&eSet all plugins to enabled in this world.")
            )
        );

        GuiStateElement blacklistState = new GuiStateElement(
                'b',
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setWhitelist(false);
                            this.saveConfig();

                            this.drawPlugins();
                            this.gui.draw();
                        },
                        "blacklist",
                        Items.BLACKLIST.getItem(),
                        translate("&fBlackList"),
                        translate("&fClick to change to a &ewhitelist&f.")
                ),
                new GuiStateElement.State(
                        change -> {
                            this.configWorld.setWhitelist(true);
                            this.saveConfig();

                            this.drawPlugins();
                            this.gui.draw();
                        },
                        "whitelist",
                        Items.WHITELIST.getItem(),
                        translate("&fWhitelist"),
                        translate("&fClick to change to a &eblacklist&f.")
                )
        );

        blacklistState.setState(this.configWorld.isWhitelist() ? "whitelist" : "blacklist");
        this.gui.addElement(blacklistState);

        this.gui.addElement(
            new StaticGuiElement(
                    'd',
                    Items.DISABLED.getItem(),
                    click -> {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            if (this.configWorld.isWhitelist()) {
                                this.configWorld.getDisabledPlugins().remove(plugin.getName());
                            } else {
                                this.configWorld.getDisabledPlugins().add(plugin.getName());
                            }
                        }

                        this.saveConfig();

                        this.drawPlugins();
                        click.getGui().draw();

                        return true;
                    },
                    translate("&cDisable all"),
                    translate("&eSet all plugins to disabled in this world.")
            )
        );

        this.gui.addElement(this.plugins);

        gui.addElement(
            new GuiPageElement(
                    'f',
                    Items.PAPER.getItem(),
                    GuiPageElement.PageAction.FIRST,
                    "Go to first page (current: %page%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'a',
                    Items.SIGN.getItem(),
                    GuiPageElement.PageAction.PREVIOUS,
                    "Go to previous page (%prevpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'n',
                    Items.SIGN.getItem(),
                    GuiPageElement.PageAction.NEXT,
                    "Go to next page (%nextpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'l',
                    Items.PAPER.getItem(),
                    GuiPageElement.PageAction.LAST,
                    "Go to last page (%pages%)"
            )
        );
    }

    private void drawPlugins() {
        this.plugins = this.plugins == null ? new GuiElementGroup('p') : this.plugins;
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

                                    this.saveConfig();
                                },
                                "pluginEnabled",
                                Items.ENABLED.getItem(),
                                translate("&a" + plugin.getName()),
                                translate("&cClick to disable &e" + plugin.getName() + " &cin this world.")
                        ),
                        new GuiStateElement.State(
                                change -> {
                                    if (this.configWorld.isWhitelist()) {
                                        this.configWorld.getDisabledPlugins().remove(plugin.getName());
                                    } else {
                                        this.configWorld.getDisabledPlugins().add(plugin.getName());
                                    }

                                    this.saveConfig();
                                },
                                "pluginDisabled",
                                Items.ENABLED.getItem(),
                                translate("&c" + plugin.getName()),
                                translate("&aClick to enable &e" + plugin.getName() + " &ain this world.")
                        )
                );

                pluginState.setState(this.configWorld.pluginEnabled(plugin) ? "pluginEnabled" : "pluginDisabled");

                this.plugins.addElement(pluginState);
            });
    }

    @Override
    public @NotNull InventoryGui getGUI() {
        return Objects.requireNonNull(this.gui);
    }
}
