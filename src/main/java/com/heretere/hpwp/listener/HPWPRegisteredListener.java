package com.heretere.hpwp.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

public class HPWPRegisteredListener extends RegisteredListener {
    public HPWPRegisteredListener(
        @NotNull Listener listener,
        @NotNull EventExecutor executor,
        @NotNull EventPriority priority,
        @NotNull Plugin plugin,
        boolean ignoreCancelled
    ) {
        super(listener, executor, priority, plugin, ignoreCancelled);
    }

    @Override public void callEvent(@NotNull Event event) throws EventException {
        super.callEvent(event);
    }
}
