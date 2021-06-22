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

import static com.heretere.hpwp.util.ChatUtils.translate;

import java.util.Map;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.MapMaker;
import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.gui.GUIUtils;
import com.heretere.hpwp.gui.GuiPage;
import com.heretere.hpwp.gui.Items;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldSelectorMenu implements GuiPage {
    /* Static */

    private static final String[] ELEMENTS = {
        "    r    ",
        " wwwwwww ",
        " fa   nl "
    };

    /* Dynamic */

    private @Nullable InventoryGui gui;
    private @Nullable GuiElementGroup worlds;
    private final @NotNull Map<World, WorldMenu> worldMenus;

    public WorldSelectorMenu() {
        this.worldMenus =
            new MapMaker()
                .weakKeys()
                .makeMap();
    }

    @Override
    public void load(@NotNull PerWorldPlugins parent) {
        this.gui = new InventoryGui(parent, "Main Menu", ELEMENTS);
        this.gui.setCloseAction(close -> false);
        this.gui.setFiller(Items.FILLER.getItem());
        this.drawWorlds(parent);

        this.gui.addElement(
            new StaticGuiElement(
                    'r',
                    Items.ENABLED.getItem(),
                    click -> {
                        this.drawWorlds(parent);
                        click.getGui().draw();
                        return true;
                    },
                    translate("&aRefresh"),
                    translate("&fIf you have added/removed worlds you can use"),
                    translate("&fThis button to refresh the list.")
            )
        );

        this.gui.addElement(this.worlds);
        GUIUtils.attachPaginationToGUI(this.gui);
    }

    @Override
    public @NotNull InventoryGui getGUI() {
        return Objects.requireNonNull(this.gui);
    }

    public void drawWorlds(final @NotNull PerWorldPlugins parent) {
        this.worlds = this.worlds == null ? new GuiElementGroup('w') : this.worlds;
        this.worlds.clearElements();

        Bukkit.getWorlds()
            .forEach(
                world -> this.worlds.addElement(
                    new StaticGuiElement(
                            'e',
                            Items.WorldMaterials.getWorldForEnvironment(world.getEnvironment())
                                .selectRandom(),
                            click -> {
                                this.worldMenus.computeIfAbsent(world, w -> new WorldMenu(parent, world))
                                    .getGUI()
                                    .show(click.getWhoClicked());
                                return true;
                            },
                            translate("&r&f" + world.getName()),
                            translate("&eConfigure the plugins for this world.")
                    )
                )
            );
    }
}
