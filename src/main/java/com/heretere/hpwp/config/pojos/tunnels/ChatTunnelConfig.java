package com.heretere.hpwp.config.pojos.tunnels;

import com.google.gson.annotations.SerializedName;
import com.heretere.hpwp.libs.hch.core.annotation.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatTunnelConfig {
    @Comment("The id of the tunnel.")
    @Comment("Used in each world config to define which tunnel it is a part of.")
    @Comment("Also Used in receiving_tunnels to allow other tunnels to send messages to this one.")
    private String id;

    @Comment("Prepends this to your existing chat formatting")
    @Comment("Used to help other players know which world someone is chatting from")
    @Comment("Used if add_formatting is set to true.")
    @SerializedName(value = "format", alternate = { "name" })
    private String format;
}
