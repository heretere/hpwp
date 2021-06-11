package com.heretere.hpwp.chat;

import com.google.common.collect.ImmutableSet;
import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.config.pojos.ConfigWorld;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatTunnelListener implements Listener {
    private final ConfigManager configManager;

    public ChatTunnelListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
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
            e.setFormat("[" + chatTunnel.getName() + "] " + e.getFormat());
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
