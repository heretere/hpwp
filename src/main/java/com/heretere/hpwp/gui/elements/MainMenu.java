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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.MapMaker;
import com.heretere.hpwp.gui.elements.collection.RandomSelector;
import de.themoep.inventorygui.GuiPageElement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.gui.GuiPage;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

import static com.heretere.hpwp.util.ChatUtils.translate;

public class MainMenu implements GuiPage {
    /* Static */

    private static final String[] ELEMENTS = {
        "    r    ",
        " wwwwwww ",
        " fa   nl "
    };

    private static final @NotNull RandomSelector<@NotNull Material> WORLD_MATERIALS;
    private static final @NotNull RandomSelector<@NotNull Material> NETHER_MATERIALS;
    private static final @NotNull RandomSelector<@NotNull Material> END_MATERIALS;
    private static final @NotNull Predicate<@NotNull Material> NOT_AIR = m -> !m.isAir();

    static {
        /* Normal */
        WORLD_MATERIALS = new RandomSelector<>();

        Optional.ofNullable(XMaterial.GRASS_BLOCK.parseMaterial()).filter(NOT_AIR).ifPresent(WORLD_MATERIALS::add);
        Optional.ofNullable(XMaterial.DIRT.parseMaterial()).filter(NOT_AIR).ifPresent(WORLD_MATERIALS::add);
        Optional.ofNullable(XMaterial.SAND.parseMaterial()).filter(NOT_AIR).ifPresent(WORLD_MATERIALS::add);
        Optional.ofNullable(XMaterial.STONE.parseMaterial()).filter(NOT_AIR).ifPresent(WORLD_MATERIALS::add);
        Optional.ofNullable(XMaterial.GRAVEL.parseMaterial()).filter(NOT_AIR).ifPresent(WORLD_MATERIALS::add);

        WORLD_MATERIALS.trimToSize();

        /* Nether */

        NETHER_MATERIALS = new RandomSelector<>();

        Optional.ofNullable(XMaterial.BASALT.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.BLACKSTONE.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.NETHER_BRICKS.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.NETHERRACK.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.GLOWSTONE.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.SOUL_SAND.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);
        Optional.ofNullable(XMaterial.MAGMA_BLOCK.parseMaterial()).filter(NOT_AIR).ifPresent(NETHER_MATERIALS::add);

        NETHER_MATERIALS.trimToSize();

        /* End */
        END_MATERIALS = new RandomSelector<>();

        Optional.ofNullable(XMaterial.END_STONE.parseMaterial()).filter(NOT_AIR).ifPresent(END_MATERIALS::add);
        Optional.ofNullable(XMaterial.END_STONE_BRICKS.parseMaterial()).filter(NOT_AIR).ifPresent(END_MATERIALS::add);
        Optional.ofNullable(XMaterial.PURPUR_BLOCK.parseMaterial()).filter(NOT_AIR).ifPresent(END_MATERIALS::add);

        END_MATERIALS.trimToSize();
    }

    /* Dynamic */

    private @Nullable InventoryGui gui;
    private @Nullable GuiElementGroup worlds;
    private final @NotNull Map<World, WorldMenu> worldMenus;

    public MainMenu() {
        this.worldMenus =
            new MapMaker()
                .weakKeys()
                .makeMap();
    }

    @Override
    public void load(@NotNull PerWorldPlugins parent) {
        this.gui = new InventoryGui(parent, "Main Menu", ELEMENTS);
        this.gui.setFiller(new ItemStack(Objects.requireNonNull(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial()), 1));
        this.drawWorlds(parent);

        this.gui.addElement(
            new StaticGuiElement(
                    'r',
                    new ItemStack(Objects.requireNonNull(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial())),
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

        gui.addElement(
            new GuiPageElement(
                    'f',
                    new ItemStack(Objects.requireNonNull(XMaterial.PAPER.parseMaterial())),
                    GuiPageElement.PageAction.FIRST,
                    "Go to first page (current: %page%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'a',
                    new ItemStack(Objects.requireNonNull(XMaterial.OAK_SIGN.parseMaterial())),
                    GuiPageElement.PageAction.PREVIOUS,
                    "Go to previous page (%prevpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'n',
                    new ItemStack(Objects.requireNonNull(XMaterial.OAK_SIGN.parseMaterial())),
                    GuiPageElement.PageAction.NEXT,
                    "Go to next page (%nextpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'l',
                    new ItemStack(Objects.requireNonNull(XMaterial.PAPER.parseMaterial())),
                    GuiPageElement.PageAction.LAST,
                    "Go to last page (%pages%)"
            )
        );
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
                            new ItemStack(MainMenu.getMaterialForEnvironment(world.getEnvironment())),
                            click -> {
                                this.worldMenus.computeIfAbsent(world, w -> new WorldMenu(parent, world))
                                    .getGUI()
                                    .show(click.getWhoClicked());
                                return true;
                            },
                            world.getName(),
                            translate("&eConfigure the plugins for this world.")
                    )
                )
            );
    }

    private static Material getMaterialForEnvironment(final @NotNull World.Environment environment) {
        switch (environment) {
            default:
            case NORMAL:
                return MainMenu.WORLD_MATERIALS.selectRandom();
            case NETHER:
                return MainMenu.NETHER_MATERIALS.selectRandom();
            case THE_END:
                return MainMenu.END_MATERIALS.selectRandom();
        }
    }
}
