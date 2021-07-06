package com.heretere.hpwp.config.pojos.tunnels;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import com.heretere.hpwp.libs.hch.core.annotation.ConfigFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigFile("chat_tunnels.yml")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatTunnelsConfig {
    private boolean enabled = false;

    @SerializedName("add_formatting")
    private boolean addFormatting = false;

    @SerializedName("chat_tunnels")
    private List<ChatTunnelConfig> chatTunnels =
        Lists.newArrayList(
            new ChatTunnelConfig("tunnel-1", "&r&f[&bTunnel 1&r&f] "),
            new ChatTunnelConfig("tunnel-2", "&r&f[&bTunnel 2&r&f] ")
        );
}
