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
