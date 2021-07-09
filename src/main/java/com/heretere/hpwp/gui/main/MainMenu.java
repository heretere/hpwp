/*
 * Project hpwp, 2021-07-09T7:41-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.gui.main;

import static com.heretere.hpwp.util.ChatUtils.translate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.gui.ConfigGui;
import com.heretere.hpwp.gui.main.global.GlobalConfigGui;
import com.heretere.hpwp.gui.main.tunnels.ChatTunnelsConfigGui;
import com.heretere.hpwp.gui.main.worlds.WorldSelectorConfigGui;
import com.heretere.hpwp.gui.util.items.ItemFactory;
import com.heretere.hpwp.gui.util.items.Texture;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.StaticGuiElement;

public final class MainMenu extends ConfigGui {
    private final GlobalConfigGui globalConfigGui;
    private final ChatTunnelsConfigGui chatTunnelsConfigGui;
    private final WorldSelectorConfigGui worldSelectorConfigGui;

    public MainMenu(
            JavaPlugin parent,
            ConfigManager configManager
    ) {
        super(
            parent,
            configManager.getConfigHandler(),
            null,
            translate("&r&oMain Menu"),
            new String[] {
                "i        ",
                ConfigGui.GROUP_LINE,
                ConfigGui.EMPTY_LINE,
            },
            null
        );

        this.globalConfigGui = new GlobalConfigGui(
                parent,
                configManager.getConfigHandler(),
                configManager.getGlobalVariables(),
                this
        );

        this.chatTunnelsConfigGui = new ChatTunnelsConfigGui(
                parent,
                configManager.getConfigHandler(),
                configManager.getChatTunnelsConfig(),
                this
        );

        this.worldSelectorConfigGui = new WorldSelectorConfigGui(
                parent,
                configManager,
                configManager.getConfigHandler(),
                this
        );

        super.getGui().addElement(
            new StaticGuiElement(
                    'i',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.INFORMATION)
                        .name("Info")
                        .loreLine("&7Per World Plugins")
                        .loreLine("")
                        .loreLine("&7========================")
                        .loreLine("Created by: &bHeretere")
                        .loreLine("Version: &evVERSION")
                        .loreLine("&7========================")
                        .loreLine(" ")
                        .loreLine("&7This is a &epremium resource &7on SpigotMC.")
                        .loreLine("&cIf you got this from somewhere else")
                        .loreLine("&cit may be malicious&7.")
                        .loreLine(" ")
                        .loreLine("&eClick on this to get a link to the resource page.")
                        .build()
                        .create(),
                    click -> {
                        click.getWhoClicked().closeInventory();
                        click.getWhoClicked().sendMessage("https://www.spigotmc.org/resources/88018/");
                        return true;
                    }
            )
        );

        GuiElementGroup elements = new GuiElementGroup('d');
        super.getGui().addElement(elements);

        elements.addElement(
            new StaticGuiElement(
                    'd',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.BLACK_MONITOR)
                        .name("Global Config")
                        .loreLine("&7Config options that affect all worlds.")
                        .build()
                        .create(),
                    click -> {
                        this.globalConfigGui.open((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        elements.addElement(
            new StaticGuiElement(
                    'd',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.MAILBOX)
                        .name("Chat Tunnels Config")
                        .loreLine("&7Configuration for separating chat messages between worlds.")
                        .build()
                        .create(),
                    click -> {
                        this.chatTunnelsConfigGui.open((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );

        elements.addElement(
            new StaticGuiElement(
                    'd',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.GLOBE)
                        .name("Worlds' Config")
                        .loreLine("&7Configuration for each world in PWP.")
                        .build()
                        .create(),
                    click -> {
                        this.worldSelectorConfigGui.open((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );
    }
}
