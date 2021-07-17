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
