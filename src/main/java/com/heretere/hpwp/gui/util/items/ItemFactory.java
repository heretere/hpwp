package com.heretere.hpwp.gui.util.items;

import static com.heretere.hpwp.util.ChatUtils.translate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.cryptomorin.xseries.SkullUtils;
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
        ItemStack outputItem = Objects.requireNonNull(base.parseItem());
        ItemMeta meta = outputItem.getItemMeta();

        if (!Objects.isNull(meta)) {
            meta.setDisplayName(translate("&r&f" + name));
            meta.setLore(lore.stream().map(line -> translate("&r&f" + line)).collect(Collectors.toList()));

            if (base.equals(XMaterial.PLAYER_HEAD) && this.texture != null) {
                SkullMeta skullMeta = (SkullMeta) meta;

                SkullUtils.applySkin(
                    skullMeta,
                    texture.getTextureValue()
                );
            }

            outputItem.setItemMeta(meta);
        }

        return outputItem;
    }
}
