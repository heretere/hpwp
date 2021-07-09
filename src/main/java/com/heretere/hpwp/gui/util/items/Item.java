package com.heretere.hpwp.gui.util.items;

import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Item {
    ENABLED(ItemFactory.builder().base(XMaterial.GREEN_STAINED_GLASS_PANE).name("&aEnabled").build()),
    DISABLED(ItemFactory.builder().base(XMaterial.RED_STAINED_GLASS_PANE).name("&cDisabled").build()),
    FILLER(ItemFactory.builder().base(XMaterial.GRAY_STAINED_GLASS_PANE).name(" ").build()),
    BLACKLIST(ItemFactory.builder().base(XMaterial.BLACK_STAINED_GLASS_PANE).name("&fBlackList").build()),
    WHITELIST(ItemFactory.builder().base(XMaterial.WHITE_STAINED_GLASS_PANE).name("&fWhiteList").build()),
    SIGN(ItemFactory.builder().base(XMaterial.OAK_SIGN).name(" ").build()),
    PAPER(ItemFactory.builder().base(XMaterial.PAPER).name(" ").build());

    private final ItemFactory itemFactory;

    public ItemFactory.ItemFactoryBuilder getBuilder() {
        return this.itemFactory.toBuilder();
    }

    public ItemStack getItem() {
        return this.itemFactory.create();
    }

}
