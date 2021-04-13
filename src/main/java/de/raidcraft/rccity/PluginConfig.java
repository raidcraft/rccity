package de.raidcraft.rccity;

import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
public class PluginConfig extends BukkitYamlConfiguration {

    private DatabaseConfig database = new DatabaseConfig();

    public PluginConfig(Path path) {

        super(path, BukkitYamlProperties.builder().setFormatter(FieldNameFormatters.LOWER_UNDERSCORE).build());
    }

    @ConfigurationElement
    @Getter
    @Setter
    public static class DatabaseConfig {

        private String username = "${CFG_DB_USER}";
        private String password = "${CFG_DB_PASSWORD}";
        private String driver = "${CFG_DB_DRIVER}";
        private String url = "${CFG_DB_URL}";
    }
}
