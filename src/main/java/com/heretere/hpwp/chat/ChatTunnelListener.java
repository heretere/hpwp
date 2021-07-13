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

package com.heretere.hpwp.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableSet;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.config.pojos.ConfigWorld;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelConfig;
import com.heretere.hpwp.util.ChatUtils;

public class ChatTunnelListener implements Listener {
    private final ConfigManager configManager;

    public ChatTunnelListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (!configManager.getChatTunnelsConfig().isEnabled()) {
            return;
        }

        ConfigWorld world = configManager.getConfigFromWorld(e.getPlayer().getWorld());
        ChatTunnelConfig chatTunnel = configManager.getChatTunnelsConfig()
            .getChatTunnels()
            .stream()
            .filter(tunnel -> tunnel.getId().equals(world.getChatTunnelId()))
            .findFirst()
            .orElse(null);

        if (chatTunnel == null) {
            return;
        }

        if (configManager.getChatTunnelsConfig().isAddFormatting()) {
            e.setFormat(ChatUtils.translate(chatTunnel.getFormat()) + e.getFormat());
        }

        final String baseTunnelId = world.getChatTunnelId();

        if (!(e.getRecipients() instanceof ImmutableSet)) {
            e.getRecipients()
                .removeIf(
                    player -> !player.hasPermission("hpwp.chat.bypass")
                        && !configManager.getConfigFromWorld(player.getWorld())
                            .getChatTunnelId()
                            .equals(baseTunnelId)
                );
        }
    }
}
