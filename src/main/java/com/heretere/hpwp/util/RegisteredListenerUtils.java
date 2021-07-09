/*
 * Project hpwp, 2021-07-09T7:41-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.util;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.listener.HPWPListener;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RegisteredListenerUtils {
    private static final @NotNull Field EXECUTOR_FIELD =
        FieldUtils.getField(RegisteredListener.class, "executor", true);

    public static @NotNull Optional<EventExecutor> getExecutorFromRegisteredListener(
            final @NotNull RegisteredListener listener
    ) {
        try {
            return Optional.of((EventExecutor) EXECUTOR_FIELD.get(listener));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static boolean checkEnabled(
            final @NotNull PerWorldPlugins parent,
            final @NotNull HPWPListener listener,
            final @NotNull Event event
    ) {
        if (!parent.isEnabled()) {
            return true;
        }

        parent.getInjector().registerEvent(listener.getDelegate().getPlugin(), event.getClass());

        final Optional<World> world = WorldUtil.getWorldFromEvent(event);

        return world.map(
            value -> parent.getConfigManager()
                .getConfigFromWorld(value)
                .eventEnabledForPlugin(
                    listener.getDelegate().getPlugin(),
                    event.getClass()
                )
        ).orElse(true);

    }
}
