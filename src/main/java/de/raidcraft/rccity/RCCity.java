package de.raidcraft.rccity;

import co.aikar.commands.PaperCommandManager;
import de.raidcraft.rccity.commands.AdminCommands;
import de.raidcraft.rccity.commands.PlayerCommands;
import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Contribution;
import de.raidcraft.rccity.entities.Resident;
import io.ebean.Database;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@PluginMain
public class RCCity extends JavaPlugin {

    @Getter
    @Accessors(fluent = true)
    private static RCCity instance;

    private Database database;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private PluginConfig pluginConfig;

    private PaperCommandManager commandManager;

    @Getter
    private static boolean testing = false;

    public RCCity() {
        instance = this;
    }

    public RCCity(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        instance = this;
        testing = true;
    }

    @Override
    public void onEnable() {

        loadConfig();
        setupDatabase();
        setupListener();
        setupCommands();
    }

    public void reload() {

        loadConfig();
    }

    private void loadConfig() {

        getDataFolder().mkdirs();
        pluginConfig = new PluginConfig(new File(getDataFolder(), "config.yml").toPath());
        pluginConfig.loadAndSave();
    }

    private void setupListener() {


    }

    private void setupCommands() {

        this.commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new AdminCommands(this));
        commandManager.registerCommand(new PlayerCommands(this));
    }

    private void setupDatabase() {

        this.database = new EbeanWrapper(Config.builder(this)
                .entities(
                        City.class,
                        CityProgress.class,
                        Contribution.class,
                        Resident.class
                )
                .build()).connect();
    }
}
