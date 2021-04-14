package de.raidcraft.rccity;

import org.bukkit.configuration.file.FileConfiguration;

public final class PluginConfig {

    private final FileConfiguration config;

    PluginConfig(FileConfiguration config) {
        this.config = config;
    }

    public boolean debug() {

        return config.getBoolean("debug", false);
    }
}
