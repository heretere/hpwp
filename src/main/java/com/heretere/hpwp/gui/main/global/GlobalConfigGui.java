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

package com.heretere.hpwp.gui.main.global;

import static com.heretere.hpwp.util.ChatUtils.translate;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.heretere.hpwp.config.pojos.GlobalVariables;
import com.heretere.hpwp.gui.ConfigGui;
import com.heretere.hpwp.gui.main.MainMenu;
import com.heretere.hpwp.gui.util.items.Item;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.StaticGuiElement;
import net.wesjd.anvilgui.AnvilGUI;

public final class GlobalConfigGui extends ConfigGui {
    private final AnvilGUI.Builder textInput;

    public GlobalConfigGui(
            final JavaPlugin parent,
            final MultiConfigHandler handler,
            final GlobalVariables globalVariables,
            final MainMenu backGui
    ) {
        super(
            parent,
            handler,
            handler.getConfigByRelativePath("global.yml").orElseThrow(IllegalArgumentException::new),
            "Global Config",
            new String[] {
                ConfigGui.EMPTY_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.EMPTY_LINE
            },
            backGui
        );

        this.textInput = super.createAnvilGui(
            builder -> builder.title(translate("&r&0Disabled Message"))
                .onClick((slot, state) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        globalVariables.setCommandDisabledMessage(state.getText());
                        super.save();
                        return Lists.newArrayList(AnvilGUI.ResponseAction.close());
                    }

                    return Lists.newArrayList();
                })
        );

        GuiElementGroup group = new GuiElementGroup('d');
        super.getGui().addElement(group);

        group.addElement(
            new DynamicGuiElement(
                    'd',
                    viewer -> new StaticGuiElement(
                            'd',
                            Item.PAPER.getBuilder()
                                .name("&cCommand Disabled Message")
                                .loreLine("&7Set the message that is sent to the player")
                                .loreLine("&7When a command is disabled in a world.")
                                .loreLine(" ")
                                .loreLine("&eCurrent Value: &r&f" + globalVariables.getCommandDisabledMessage())
                                .build()
                                .create(),
                            click -> {
                                this.addExempt((Player) click.getWhoClicked());
                                this.textInput.text(globalVariables.getCommandDisabledMessage())
                                    .open((Player) click.getWhoClicked());
                                return true;
                            }
                    )
            )
        );
    }
}
