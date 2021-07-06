package com.heretere.hpwp.gui2.main.tunnels;

import static com.heretere.hpwp.util.ChatUtils.translate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelConfig;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelsConfig;
import com.heretere.hpwp.gui2.ConfigGui;
import com.heretere.hpwp.gui2.util.items.Item;
import com.heretere.hpwp.libs.hch.core.MultiConfigHandler;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.StaticGuiElement;
import net.wesjd.anvilgui.AnvilGUI;

public class ChatTunnelConfigGui extends ConfigGui {
    protected ChatTunnelConfigGui(
            JavaPlugin parent,
            MultiConfigHandler handler,
            ChatTunnelsConfigGui backGui,
            ChatTunnelsConfig chatTunnelsConfig,
            ChatTunnelConfig chatTunnelConfig
    ) {
        super(
            parent,
            handler,
            handler.getConfigByRelativePath("chat_tunnels.yml").orElseThrow(IllegalArgumentException::new),
            "Chat Tunnel Config",
            new String[] {
                "       t ",
                ConfigGui.GROUP_LINE,
                ConfigGui.EMPTY_LINE
            },
            backGui
        );

        AnvilGUI.Builder tunnelIdText = super.createAnvilGui(
            builder -> builder.title(translate("&r&0Tunnel Id"))
                .onComplete((player, text) -> {
                    chatTunnelConfig.setId(text);
                    super.save();
                    return AnvilGUI.Response.close();
                })
        );

        AnvilGUI.Builder formattedNameText = super.createAnvilGui(
            builder -> builder.title(translate("&r&0Formatted Name"))
                .onComplete((player, text) -> {
                    chatTunnelConfig.setFormat(text);
                    super.save();
                    return AnvilGUI.Response.close();
                })
        );

        GuiElementGroup elements = new GuiElementGroup('d');
        super.getGui().addElement(elements);

        elements.addElement(
            new DynamicGuiElement(
                    'd',
                    viewer -> new StaticGuiElement(
                            'd',
                            Item.PAPER.getBuilder()
                                .name("Tunnel Id")
                                .loreLine("&7Set the tunnel id of this tunnel")
                                .loreLine(" ")
                                .loreLine("&eCurrent Value: &r&f" + chatTunnelConfig.getId())
                                .build()
                                .create(),
                            click -> {
                                this.addExempt((Player) click.getWhoClicked());
                                tunnelIdText.text(chatTunnelConfig.getId()).open((Player) click.getWhoClicked());
                                return true;
                            }
                    )
            )
        );

        elements.addElement(
            new DynamicGuiElement(
                    'd',
                    viewer -> new StaticGuiElement(
                            'd',
                            Item.PAPER.getBuilder()
                                .name("Tunnel Formatted Name")
                                .loreLine("&7Set the formatted name of this tunnel")
                                .loreLine("&7This formatting is prepended before existing chat formatting")
                                .loreLine("&7Used when add_formatting is set to true")
                                .loreLine(" ")
                                .loreLine("&eCurrent Value: &r&f" + chatTunnelConfig.getFormat())
                                .build()
                                .create(),
                            click -> {
                                this.addExempt((Player) click.getWhoClicked());
                                formattedNameText.text(chatTunnelConfig.getFormat())
                                    .open((Player) click.getWhoClicked());
                                return true;
                            }
                    )
            )
        );

        super.getGui().addElement(
            new StaticGuiElement(
                    't',
                    Item.DISABLED.getBuilder()
                        .name("&cDelete")
                        .loreLine("&7Click this to &cdelete &7this chat tunnel.")
                        .loreLine(" ")
                        .loreLine("&7You will need to set a new chat tunnel")
                        .loreLine("&7For any worlds in this chat tunnel")
                        .loreLine("&7After deletion.")
                        .build()
                        .create(),
                    click -> {
                        chatTunnelsConfig.getChatTunnels().remove(chatTunnelConfig);
                        super.save();
                        backGui.open((Player) click.getWhoClicked());
                        return true;
                    }
            )
        );
    }
}