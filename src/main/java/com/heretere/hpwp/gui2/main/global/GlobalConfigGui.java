package com.heretere.hpwp.gui2.main.global;

import static com.heretere.hpwp.util.ChatUtils.translate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.heretere.hpwp.config.pojos.GlobalVariables;
import com.heretere.hpwp.gui2.ConfigGui;
import com.heretere.hpwp.gui2.main.MainMenu;
import com.heretere.hpwp.gui2.util.items.Item;
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
                .onComplete((player, text) -> {
                    globalVariables.setCommandDisabledMessage(text);
                    super.save();
                    return AnvilGUI.Response.close();
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
