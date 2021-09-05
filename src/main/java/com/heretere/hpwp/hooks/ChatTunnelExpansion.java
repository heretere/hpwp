package com.heretere.hpwp.hooks;

import com.heretere.hpwp.config.ConfigManager;
import com.heretere.hpwp.config.pojos.ConfigWorld;
import com.heretere.hpwp.config.pojos.tunnels.ChatTunnelConfig;
import com.heretere.hpwp.util.ChatUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatTunnelExpansion extends PlaceholderExpansion {
    private final ConfigManager configManager;

    public ChatTunnelExpansion(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pwp-chat";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Heretere";
    }

    @Override
    public @NotNull String getVersion() {
        return "VERSION";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("tunnel") && player instanceof Player) {
            Player p = (Player) player;
            ConfigWorld configWorld = this.configManager.getConfigFromWorld(p.getWorld());
            ChatTunnelConfig chatTunnel = this.configManager.getChatTunnelsConfig()
                .getChatTunnels()
                .stream()
                .filter(tunnel -> tunnel.getId().equals(configWorld.getChatTunnelId()))
                .findFirst()
                .orElse(null);

            return chatTunnel != null ? ChatUtils.translate(chatTunnel.getFormat()) : null;
        }

        return null;
    }
}
