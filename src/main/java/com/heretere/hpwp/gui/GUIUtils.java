package com.heretere.hpwp.gui;

import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;

public final class GUIUtils {
    private GUIUtils() {
        throw new IllegalStateException("Utility Class.");
    }

    public static void attachPaginationToGUI(final InventoryGui gui) {
        gui.addElement(
            new GuiPageElement(
                    'f',
                    Items.PAPER.getItem(),
                    GuiPageElement.PageAction.FIRST,
                    "Go to first page (current: %page%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'a',
                    Items.SIGN.getItem(),
                    GuiPageElement.PageAction.PREVIOUS,
                    "Go to previous page (%prevpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'n',
                    Items.SIGN.getItem(),
                    GuiPageElement.PageAction.NEXT,
                    "Go to next page (%nextpage%)"
            )
        );

        gui.addElement(
            new GuiPageElement(
                    'l',
                    Items.PAPER.getItem(),
                    GuiPageElement.PageAction.LAST,
                    "Go to last page (%pages%)"
            )
        );
    }
}
