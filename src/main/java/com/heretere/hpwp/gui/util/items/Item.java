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
