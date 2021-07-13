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

import static com.heretere.hpwp.util.ChatUtils.translate;

import org.bukkit.World;
import org.bukkit.entity.Player;
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
import de.themoep.inventorygui.StaticGuiElement;

public final class ChatTunnelSelectorConfigGui extends ConfigGui {
    private final ChatTunnelsConfig chatTunnelsConfig;
    private final ConfigWorld configWorld;
    private final GuiElementGroup tunnels;

    public ChatTunnelSelectorConfigGui(
            JavaPlugin parent,
            MultiConfigHandler handler,
            ChatTunnelsConfig chatTunnelsConfig,
            ConfigWorld configWorld,
            World world,
            ConfigGui backGui
    ) {
        super(
            parent,
            handler,
            handler.getConfigByRelativePath(world.getName() + ".yml").orElseThrow(IllegalArgumentException::new),
            translate("&r&0Select Chat Tunnel"),
            new String[] {
                "i        ",
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.PAGINATION_LINE
            },
            backGui
        );

        this.chatTunnelsConfig = chatTunnelsConfig;
        this.configWorld = configWorld;

        this.tunnels = new GuiElementGroup('d');
        super.getGui().addElement(this.tunnels);

        super.getGui().addElement(
            new StaticGuiElement(
                    'i',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.INFORMATION)
                        .name("Info")
                        .loreLine("&7Select which chat tunnel this world is in.")
                        .loreLine("&7Worlds in the same chat tunnel can communicate with each other.")
                        .loreLine(" ")
                        .loreLine("&7If you want to make a new chat tunnel go to the &fChat Tunnel")
                        .loreLine("&7config from the Main Menu.")
                        .build()
                        .create()
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
        this.tunnels.clearElements();
        this.chatTunnelsConfig.getChatTunnels()
            .forEach(
                tunnel -> this.tunnels.addElement(
                    new DynamicGuiElement(
                            'd',
                            viewer -> new StaticGuiElement(
                                    'd',
                                    Item.PAPER.getBuilder()
                                        .name(tunnel.getId())
                                        .loreLine("&7Click to set the chat tunnel for this world.")
                                        .loreLine(" ")
                                        .loreLine("&eCurrent Id: &r&f" + tunnel.getId())
                                        .loreLine("&eCurrent Format: &r&f" + tunnel.getFormat())
                                        .build()
                                        .create(),
                                    click -> {
                                        this.configWorld.setChatTunnelId(tunnel.getId());
                                        super.save();
                                        viewer.closeInventory();
                                        return true;
                                    }
                            )
                    )
                )
            );
    }
}
