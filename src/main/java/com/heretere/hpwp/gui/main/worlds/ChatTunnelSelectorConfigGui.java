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
