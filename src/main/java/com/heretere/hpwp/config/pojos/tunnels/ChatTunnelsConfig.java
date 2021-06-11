package com.heretere.hpwp.config.pojos.tunnels;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import com.heretere.hch.core.annotation.ConfigFile;

import java.util.List;

@ConfigFile("chat_tunnels.yml")
public class ChatTunnelsConfig {
    private boolean enabled = false;

    @SerializedName("add_formatting")
    private boolean addFormatting = false;

    @SerializedName("chat_tunnels")
    private List<ChatTunnelConfig> chatTunnels =
        Lists.newArrayList(
            new ChatTunnelConfig("tunnel-1", "Tunnel 1"),
            new ChatTunnelConfig("tunnel-2", "Tunnel 2")
        );

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAddFormatting() {
        return addFormatting;
    }

    public List<ChatTunnelConfig> getChatTunnels() {
        return chatTunnels;
    }

    public ChatTunnelsConfig() {
    }

    public ChatTunnelsConfig(
            boolean enabled,
            boolean addFormatting,
            List<ChatTunnelConfig> chatTunnels
    ) {
        this.enabled = enabled;
        this.addFormatting = addFormatting;
        this.chatTunnels = chatTunnels;
    }
}
