package com.heretere.hpwp.config;

import com.heretere.hch.MultiConfigHandler;
import com.heretere.hch.ProcessorType;
import com.heretere.hch.collection.ConfigList;
import com.heretere.hch.processor.exception.InvalidTypeException;
import com.heretere.hch.structure.builder.ConfigBuilder;
import com.heretere.hch.structure.builder.ConfigPathBuilder;
import com.heretere.hpwp.util.YamlKeyEscaper;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

final class ConfigWorld {
    private Boolean checkWorld = true;
    private ConfigList<String> disabledEvents = ConfigList.newInstance(String.class, "Testing");

    ConfigWorld(
        final @NotNull MultiConfigHandler handler,
        final @NotNull World world
    ) {
        this.createConfigSection(handler, world);
    }

    private void createConfigSection(
        final @NotNull MultiConfigHandler handler,
        final @NotNull World world
    ) {
        try {
            String worldKey = "worlds." + YamlKeyEscaper.escape(world.getName());
            ConfigBuilder.builder()
                         .setRelativePath("config.toml")
                         .addConfigPath(
                             ConfigPathBuilder
                                 .sectionBuilder()
                                 .addComment("Configuration for " + world.getName())
                                 .setKey(worldKey)
                                 .build()
                         )
                         .addConfigPath(
                             ConfigPathBuilder
                                 .fieldBuilder(Boolean.class)
                                 .addComment("Whether or not to run per world plugins on this world.")
                                 .setKey(worldKey + ".check_world")
                                 .setGetterSupplier(() -> this.checkWorld)
                                 .setSetterConsumer(check -> this.checkWorld = check)
                                 .build()
                         )
                         .addConfigPath(
                             ConfigPathBuilder
                                 .fieldBuilder(ConfigList.class)
                                 .addComment("Whether or not to run per world plugins on this world.")
                                 .setKey(worldKey + ".disabled_events")
                                 .setGetterSupplier(() -> this.disabledEvents)
                                 .setSetterConsumer(disabled -> this.disabledEvents = disabled)
                                 .build()
                         )
                         .build(handler, ProcessorType.YAML);
        } catch (IllegalAccessException | IOException | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

}
