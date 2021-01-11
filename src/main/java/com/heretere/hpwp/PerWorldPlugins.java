package com.heretere.hpwp;

import com.heretere.hdl.DependencyPlugin;
import com.heretere.hdl.dependency.maven.annotation.MavenDependency;
import com.heretere.hdl.dependency.maven.annotation.MavenRepository;
import com.heretere.hdl.relocation.annotation.Relocation;
import com.heretere.hpwp.config.ConfigManager;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "HPWP", version = "1.0.0")
@ApiVersion(ApiVersion.Target.v1_13)
@LogPrefix("HPWP")

@MavenRepository("https://jitpack.io")
@MavenDependency("org|tomlj:tomlj:1.0.0")
@MavenDependency("org|antlr:antlr4-runtime:4.7.2")
@Relocation(from = "org|tomlj", to = "com|heretere|hpwp|libs|tomlj")
@Relocation(from = "org|antlr", to = "com|heretere|hpwp|core|libs|antlr")
public class PerWorldPlugins extends DependencyPlugin {
    @Override public void load() {

    }

    @Override public void enable() {
        new ConfigManager(this);
    }

    @Override public void disable() {

    }
}
