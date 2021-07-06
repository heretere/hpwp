package com.heretere.hpwp.gui2.main.tunnels;

import static com.heretere.hpwp.util.ChatUtils.translate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelConfig;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelsConfig;
import com.heretere.hpwp.gui2.ConfigGui;
import com.heretere.hpwp.gui2.main.MainMenu;
import com.heretere.hpwp.gui2.util.items.Item;
import com.heretere.hpwp.gui2.util.items.ItemFactory;
import com.heretere.hpwp.gui2.util.items.Texture;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.StaticGuiElement;

public final class ChatTunnelsConfigGui extends ConfigGui {
    private final JavaPlugin parent;
    private final MultiConfigHandler handler;

    private final ChatTunnelsConfig chatTunnelsConfig;
    private final GuiElementGroup tunnels;

    public ChatTunnelsConfigGui(
            JavaPlugin parent,
            MultiConfigHandler handler,
            ChatTunnelsConfig chatTunnelsConfig,
            MainMenu backGui
    ) {
        super(
            parent,
            handler,
            handler.getConfigByRelativePath("chat_tunnels.yml").orElseThrow(IllegalArgumentException::new),
            translate("&r&0Chat Tunnels"),
            new String[] {
                "i st   j ",
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.GROUP_LINE,
                ConfigGui.PAGINATION_LINE
            },
            backGui
        );

        this.parent = parent;
        this.handler = handler;

        this.chatTunnelsConfig = chatTunnelsConfig;

        this.tunnels = new GuiElementGroup('d');
        super.getGui().addElement(this.tunnels);

        super.getGui().addElement(
            new StaticGuiElement(
                    'i',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.INFORMATION)
                        .name("Info")
                        .loreLine("&7Chat tunnels allow you separate")
                        .loreLine("&7player messages between worlds.")
                        .loreLine(" ")
                        .loreLine("&7To enable this functionality click on")
                        .loreLine("&7the panes to the right of this item.")
                        .loreLine(" ")
                        .loreLine("&7The formatting option allows you to")
                        .loreLine("&7prepend a configurable tag to each")
                        .loreLine("&7player's message.")
                        .loreLine(" ")
                        .loreLine("&eGo to the &fWorlds &esection to")
                        .loreLine("&eset the chat tunnel of a world")
                        .build()
                        .create()
            )
        );

        GuiStateElement enabledState = new GuiStateElement(
                's',
                new GuiStateElement.State(
                        change -> {
                            this.chatTunnelsConfig.setEnabled(true);
                            super.save();
                        },
                        ConfigGui.STATE_ENABLED,
                        Item.ENABLED.getBuilder()
                            .name("&aChat tunnels are currently enabled.")
                            .loreLine("&7Click to &cdisable &7chat tunnels.")
                            .build()
                            .create()
                ),
                new GuiStateElement.State(
                        change -> {
                            this.chatTunnelsConfig.setEnabled(false);
                            super.save();
                        },
                        ConfigGui.STATE_DISABLED,
                        Item.DISABLED.getBuilder()
                            .name("&cChat tunnels are currently disabled.")
                            .loreLine("&7Click to &aenable &7chat tunnels.")
                            .build()
                            .create()
                )
        );

        enabledState.setState(this.chatTunnelsConfig.isEnabled() ? ConfigGui.STATE_ENABLED : ConfigGui.STATE_DISABLED);
        super.getGui().addElement(enabledState);

        GuiStateElement formattingState = new GuiStateElement(
                't',
                new GuiStateElement.State(
                        change -> {
                            this.chatTunnelsConfig.setAddFormatting(true);
                            super.save();
                        },
                        ConfigGui.STATE_ENABLED,
                        Item.ENABLED.getBuilder()
                            .name("&aFormatting is currently enabled.")
                            .loreLine("&7Click to &cdisable &7formatting.")
                            .build()
                            .create()
                ),
                new GuiStateElement.State(
                        change -> {
                            this.chatTunnelsConfig.setAddFormatting(false);
                            super.save();
                        },
                        ConfigGui.STATE_DISABLED,
                        Item.DISABLED.getBuilder()
                            .name("&cFormatting is currently disabled.")
                            .loreLine("&7Click to &aenable &7formatting.")
                            .build()
                            .create()
                )
        );

        formattingState.setState(
            this.chatTunnelsConfig.isAddFormatting() ? ConfigGui.STATE_ENABLED : ConfigGui.STATE_DISABLED
        );
        super.getGui().addElement(formattingState);

        super.getGui().addElement(
            new StaticGuiElement(
                    'j',
                    ItemFactory.builder()
                        .base(XMaterial.PLAYER_HEAD)
                        .texture(Texture.PLUS_ICON)
                        .name("&aCreate new chat tunnel")
                        .loreLine("&7Click this to create a new chat tunnel")
                        .loreLine("&7The tunnel will be added to the end")
                        .loreLine("&7of the list.")
                        .build()
                        .create(),
                    click -> {
                        int size = chatTunnelsConfig.getChatTunnels().size() + 1;
                        chatTunnelsConfig.getChatTunnels()
                            .add(new ChatTunnelConfig("tunnel-" + size, "&r&f[&bTunnel " + size + "&r&f] "));
                        super.save();
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
                                        .loreLine("&7Click to edit this chat tunnel")
                                        .loreLine(" ")
                                        .loreLine("&eCurrent Id: &r&f" + tunnel.getId())
                                        .loreLine("&eCurrent Format: &r&f" + tunnel.getFormat())
                                        .build()
                                        .create(),
                                    click -> {
                                        new ChatTunnelConfigGui(
                                                this.parent,
                                                this.handler,
                                                this,
                                                chatTunnelsConfig,
                                                tunnel
                                        ).open((Player) click.getWhoClicked());
                                        return true;
                                    }
                            )
                    )
                )
            );
    }
}
