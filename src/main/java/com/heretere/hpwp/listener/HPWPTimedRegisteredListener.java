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

package com.heretere.hpwp.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.jetbrains.annotations.NotNull;

import com.heretere.hpwp.PerWorldPlugins;
import com.heretere.hpwp.util.RegisteredListenerUtils;

public final class HPWPTimedRegisteredListener extends TimedRegisteredListener implements HPWPListener {
    private final @NotNull PerWorldPlugins parent;
    private final @NotNull TimedRegisteredListener delegate;

    public HPWPTimedRegisteredListener(
            final @NotNull PerWorldPlugins parent,
            final @NotNull TimedRegisteredListener delegate
    ) {
        super(
            delegate.getListener(),
            RegisteredListenerUtils.getExecutorFromRegisteredListener(delegate)
                .orElseThrow(IllegalArgumentException::new),
            delegate.getPriority(),
            delegate.getPlugin(),
            delegate.isIgnoringCancelled()
        );

        this.parent = parent;
        this.delegate = delegate;
    }

    @Override
    public void callEvent(final @NotNull Event event) throws EventException {
        if (RegisteredListenerUtils.checkEnabled(this.parent, this, event)) {
            this.delegate.callEvent(event);
        }
    }

    @Override
    public @NotNull RegisteredListener getDelegate() {
        return this.delegate;
    }
}
