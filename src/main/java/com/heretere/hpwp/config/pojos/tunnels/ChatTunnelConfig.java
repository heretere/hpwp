package com.heretere.hpwp.config.pojos.tunnels;

import com.heretere.hch.core.annotation.Comment;

public class ChatTunnelConfig {
    @Comment("The id of the tunnel.")
    @Comment("Used in each world config to define which tunnel it is a part of.")
    @Comment("Also Used in receiving_tunnels to allow other tunnels to send messages to this one.")
    private String id;

    @Comment("The name of the tunnel.")
    @Comment("Used if add_formatting is set to true.")
    private String name;

    public ChatTunnelConfig() {
    }

    public ChatTunnelConfig(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
