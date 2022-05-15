package com.heretere.hpwp.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FieldFinder {
    public static Optional<Plugin> findPlugin(Object instance) {
        Class<?> current = instance.getClass();
        List<Field> fields = Lists.newArrayList();
        while (current != null) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }

        for (Field f : fields) {
            if (!(Plugin.class.isAssignableFrom(f.getType())))
                continue;

            try {
                Plugin plugin = (Plugin) FieldUtils.readField(f, instance, true);
                return Optional.ofNullable(plugin);
            } catch (IllegalAccessException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
