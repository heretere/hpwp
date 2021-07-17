/*
 * Project hpwp, 2021-07-13T19:01-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.config.pojos;

import org.jetbrains.annotations.NotNull;

import com.google.gson.annotations.SerializedName;
import com.heretere.hpwp.libs.hch.core.annotation.Comment;
import com.heretere.hpwp.libs.hch.core.annotation.ConfigFile;

import lombok.Data;

@ConfigFile("global.yml")
@Data
public class GlobalVariables {
    @Comment("The message that is sent when someone tries to execute a disabled command.")
    @Comment("Use the '&' symbol for color codes.")
    @SerializedName(value = "command_disabled_message", alternate = { "commandDisabledMessage" })
    private @NotNull String commandDisabledMessage = "&cSorry, that command is disabled.";

    @Comment("Receive update notifications. Message is sent to all people with hpwp.notify permission.")
    @SerializedName("update_notification")
    private boolean updateNotification = true;

    @Comment("Use d (day), h(hour), s(second) to change update checking interval.")
    @SerializedName("update_notification_interval")
    private @NotNull String updateNotificationInterval = "4h 0m 0s";
}
