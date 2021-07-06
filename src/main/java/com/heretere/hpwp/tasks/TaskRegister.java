package com.heretere.hpwp.tasks;

import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskRegister {
    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    private static final TaskChainFactory factory =
        BukkitTaskChainFactory.create(JavaPlugin.getProvidingPlugin(TaskRegister.class));
}
