package com.heretere.hpwp.gui.util.items;

import java.util.Arrays;

import org.bukkit.World;

import com.cryptomorin.xseries.XMaterial;
import com.heretere.hpwp.gui.util.collection.RandomSelector;

public enum WorldItems {
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
    private final RandomSelector<ItemFactory> items;

    WorldItems(World.Environment environment, XMaterial... xMaterials) {
        this.environment = environment;
        this.items = new RandomSelector<>();

        Arrays.stream(xMaterials)
            .filter(XMaterial::isSupported)
            .forEach(material -> items.add(ItemFactory.builder().base(material).build()));
    }

    public static WorldItems getWorldForEnvironment(World.Environment find) {
        return Arrays.stream(WorldItems.values())
            .filter(worldMaterial -> worldMaterial.getEnvironment() == find)
            .findFirst()
            .orElse(WorldItems.OVERWORLD);
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public ItemFactory.ItemFactoryBuilder selectRandom() {
        return this.items.selectRandom().toBuilder();
    }

}
