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

package com.heretere.hpwp.gui;

import static com.heretere.hpwp.util.ChatUtils.translate;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.UnaryOperator;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.gui.util.items.Item;
import com.heretere.hpwp.gui.util.items.ItemFactory;
import com.heretere.hpwp.gui.util.items.Texture;
import com.heretere.hpwp.libs.hch.core.HCHConfig;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;
import com.heretere.hpwp.tasks.Chain;

import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import lombok.AccessLevel;
import lombok.Getter;
import net.wesjd.anvilgui.AnvilGUI;

public abstract class ConfigGui {
    public static final String EMPTY_LINE = "         ";
    public static final String GROUP_LINE = " ddddddd ";
    public static final String PAGINATION_LINE = " fp   nl ";
    public static final String STATE_ENABLED = "enabled";
    public static final String STATE_DISABLED = "disabled";

    private final JavaPlugin parent;
    private final MultiConfigHandler handler;
    @Nullable
    private final HCHConfig config;

    private final Set<Player> backExempt = Collections.newSetFromMap(new WeakHashMap<>());

    @Getter(AccessLevel.PROTECTED)
    private final InventoryGui gui;

    protected ConfigGui(
            final JavaPlugin parent,
            final MultiConfigHandler handler,
            @Nullable final HCHConfig config,
            final String name,
            final String[] elements,
            @Nullable final ConfigGui backGui
    ) {
        this.parent = parent;
        this.handler = handler;
        this.config = config;
        this.gui = new InventoryGui(parent, name, elements);
        this.gui.setCloseAction(close -> {
            boolean exempt = backExempt.contains((Player) close.getPlayer());
            backExempt.remove((Player) close.getPlayer());

            if (!exempt && !Objects.isNull(backGui)) {
                backGui.open((Player) close.getPlayer());
            }

            return false;
        });
        this.gui.setFiller(Item.FILLER.getItem());
    }

    public void open(Player player) {
        this.gui.show(player);
        this.refresh(player);
    }

    public void refresh(Player player) {
        this.gui.draw(player, true);
    }

    protected void save() {
        if (Objects.isNull(this.config)) {
            return;
        }

        Chain.IO.newChain()
            .async(() -> handler.saveConfig(config, true))
            .execute();
    }

    protected AnvilGUI.Builder createAnvilGui(UnaryOperator<AnvilGUI.Builder> builder) {
        return builder.apply(
            new AnvilGUI.Builder()
                .plugin(this.parent)
                .itemLeft(
                    ItemFactory.builder()
                        .base(XMaterial.NAME_TAG)
                        .loreLine("&7Press the furthest right output")
                        .loreLine("&7Slot to confirm the changes.")
                        .loreLine(" ")
                        .loreLine("&eOutput will be empty till text is changed.")
                        .build()
                        .create()
                )
                .onClose(this::open)
        );
    }

    protected void attachPagination() {
        this.gui.addElement(
            new GuiPageElement(
                    'f',
                    ItemFactory.builder().base(XMaterial.PLAYER_HEAD).texture(Texture.BACK_2).build().create(),
                    GuiPageElement.PageAction.FIRST,
                    translate("&r&7Go to &efirst &7page.")
            )
        );

        this.gui.addElement(
            new GuiPageElement(
                    'p',
                    ItemFactory.builder().base(XMaterial.PLAYER_HEAD).texture(Texture.BACK).build().create(),
                    GuiPageElement.PageAction.PREVIOUS,
                    translate("&r&7Go to previous page (&e%prevpage%&7).")
            )
        );

        this.gui.addElement(
            new GuiPageElement(
                    'n',
                    ItemFactory.builder().base(XMaterial.PLAYER_HEAD).texture(Texture.NEXT).build().create(),
                    GuiPageElement.PageAction.NEXT,
                    translate("&r&7Go to next page (&e%nextpage%&7).")
            )
        );

        this.gui.addElement(
            new GuiPageElement(
                    'l',
                    ItemFactory.builder().base(XMaterial.PLAYER_HEAD).texture(Texture.NEXT_2).build().create(),
                    GuiPageElement.PageAction.LAST,
                    translate("&r&7Go to last page (&e%pages%&7).")
            )
        );
    }

    public void addExempt(Player player) {
        this.backExempt.add(player);
    }
}
