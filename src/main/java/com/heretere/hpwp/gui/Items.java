package com.heretere.hpwp.gui;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.gui.elements.collection.RandomSelector;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public enum Items {
    ENABLED(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem()),
    DISABLED(XMaterial.RED_STAINED_GLASS_PANE.parseItem()),
    FILLER(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()),
    BLACKLIST(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()),
    WHITELIST(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem()),
    SIGN(XMaterial.OAK_SIGN.parseItem()),
    PAPER(XMaterial.PAPER.parseItem());

    private final ItemStack item;

    Items(ItemStack item) {
        this.item = Objects.requireNonNull(item);
    }

    public ItemStack getItem() {
        return new ItemStack(this.item);
    }

    public enum WorldMaterials {
        OVERWORLD(
                World.Environment.NORMAL,
                XMaterial.GRASS_BLOCK,
                XMaterial.DIRT,
                XMaterial.SAND,
                XMaterial.STONE,
                XMaterial.GRAVEL,
                XMaterial.CACTUS,
                XMaterial.SPONGE,
                XMaterial.OAK_LEAVES
        ),
        NETHER(
                World.Environment.NETHER,
                XMaterial.BASALT,
                XMaterial.BLACKSTONE,
                XMaterial.NETHER_BRICKS,
                XMaterial.NETHERRACK,
                XMaterial.GLOWSTONE,
                XMaterial.SOUL_SAND,
                XMaterial.MAGMA_BLOCK
        ),
        END(
                World.Environment.THE_END,
                XMaterial.END_STONE,
                XMaterial.END_STONE_BRICKS,
                XMaterial.PURPUR_BLOCK,
                XMaterial.OBSIDIAN,
                XMaterial.DRAGON_EGG
        );

        private final World.Environment environment;
        private final RandomSelector<ItemStack> items;

        WorldMaterials(World.Environment environment, XMaterial... xMaterials) {
            this.environment = environment;
            this.items = new RandomSelector<>();

            Arrays.stream(xMaterials)
                .map(XMaterial::parseItem)
                .filter(Objects::nonNull)
                .filter(i -> !i.getType().isAir())
                .forEach(this.items::add);
        }

        public World.Environment getEnvironment() {
            return this.environment;
        }

        public ItemStack selectRandom() {
            return new ItemStack(this.items.selectRandom());
        }

        public static WorldMaterials getWorldForEnvironment(World.Environment find) {
            return Arrays.stream(WorldMaterials.values())
                .filter(worldMaterial -> worldMaterial.getEnvironment() == find)
                .findFirst()
                .orElse(WorldMaterials.OVERWORLD);
        }

    }
}
