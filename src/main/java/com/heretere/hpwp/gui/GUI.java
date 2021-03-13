/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hpwp.gui;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.gui.elements.MainMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUI {
    private final @NotNull PerWorldPlugins parent;
    private final MainMenu mainMenu;

    public GUI(final @NotNull PerWorldPlugins parent) {
        this.parent = parent;

        this.mainMenu = new MainMenu();
    }

    public void load() {
        this.mainMenu.load(this.parent);
    }

    public void open(final @NotNull Player player) {
        this.mainMenu.drawWorlds(this.parent);
        this.mainMenu.getGUI().show(player);
        this.mainMenu.getGUI().draw(player);
    }
}
