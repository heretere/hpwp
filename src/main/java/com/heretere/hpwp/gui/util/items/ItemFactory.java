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

package com.heretere.hpwp.gui.util.items;

import static com.heretere.hpwp.util.ChatUtils.translate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@RequiredArgsConstructor(onConstructor_ = { @Builder })
@Builder(toBuilder = true)
public class ItemFactory {
    private final XMaterial base;
    private final String name;
    private final Texture texture;
    @Singular(value = "loreLine", ignoreNullCollections = true)
    private final List<String> lore;

    public ItemStack create() {
        ItemStack outputItem = base.equals(XMaterial.PLAYER_HEAD) && this.texture != null ? XSkull.createItem()
                .profile(Profileable.of(ProfileInputType.BASE64, texture.getTextureValue()))
                .apply() : Objects.requireNonNull(base.parseItem());
        ItemMeta meta = outputItem.getItemMeta();

        if (!Objects.isNull(meta)) {
            meta.setDisplayName(translate("&r&f" + name));
            meta.setLore(lore.stream().map(line -> translate("&r&f" + line)).collect(Collectors.toList()));

            outputItem.setItemMeta(meta);
        }

        return outputItem;
    }
}
