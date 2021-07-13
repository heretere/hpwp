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
