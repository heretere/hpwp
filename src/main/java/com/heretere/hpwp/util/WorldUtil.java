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

package com.heretere.hpwp.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.logging.Level;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import lombok.experimental.UtilityClass;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Optional;

@UtilityClass
public final class WorldUtil {

    private static MethodHandle inventoryViewGetPlayerHandle;
    private static MethodHandle humanEntityGetWorldHandle;
    private static boolean reflectionInitialized = false;

    private static synchronized void ensureReflectionInitialized() {
        if (reflectionInitialized) {
            return;
        }

        try {
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            Class<?> inventoryViewClass = Class.forName("org.bukkit.inventory.InventoryView");
            Class<?> humanEntityClass = Class.forName("org.bukkit.entity.HumanEntity");

            inventoryViewGetPlayerHandle = lookup.findVirtual(
                inventoryViewClass,
                "getPlayer",
                MethodType.methodType(humanEntityClass)
            );

            humanEntityGetWorldHandle = lookup.findVirtual(
                humanEntityClass,
                "getWorld",
                MethodType.methodType(World.class)
            );

            reflectionInitialized = true;
        } catch (Throwable e) {
            Bukkit.getLogger().log(Level.FINE, "WorldUtil: inventory-based world resolution unavailable on this server implementation", e);
        }
    }

    public static Optional<World> getWorldFromEvent(final Event event) {
        if (event instanceof BlockEvent) {
            return Optional.of(((BlockEvent) event).getBlock().getWorld());
        }

        if (event instanceof PlayerEvent) {
            return Optional.of(((PlayerEvent) event).getPlayer().getWorld());
        }

        if (event instanceof InventoryEvent) {
            return fromInventoryEvent((InventoryEvent) event);
        }

        if (event instanceof EntityEvent) {
            return Optional.of(((EntityEvent) event).getEntity().getWorld());
        }

        if (event instanceof HangingEvent) {
            return Optional.of(((HangingEvent) event).getEntity().getWorld());
        }

        if (event instanceof VehicleEvent) {
            return Optional.of(((VehicleEvent) event).getVehicle().getWorld());
        }

        if (event instanceof WeatherEvent) {
            return Optional.of(((WeatherEvent) event).getWorld());
        }

        if (event instanceof WorldEvent) {
            return Optional.of(((WorldEvent) event).getWorld());
        }

        return Optional.empty();
    }

    private static Optional<World> fromInventoryEvent(final InventoryEvent inventoryEvent) {
        try {
            ensureReflectionInitialized();

            if (!reflectionInitialized) {
                return Optional.empty();
            }

            Object inventoryViewObject = inventoryEvent.getView();
            if (inventoryViewObject == null) {
                return Optional.empty();
            }

            Object playerObject = inventoryViewGetPlayerHandle.invoke(inventoryViewObject);
            if (playerObject == null) {
                return Optional.empty();
            }

            Object worldObject = humanEntityGetWorldHandle.invoke(playerObject);
            if (worldObject instanceof World) {
                return Optional.of((World) worldObject);
            }
        } catch (Throwable e) {
            Bukkit.getLogger().log(Level.FINE, "WorldUtil: failed to resolve world from InventoryEvent", e);
        }

        return Optional.empty();
    }
}