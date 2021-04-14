package de.raidcraft.rccity;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.base.Strings;
import de.raidcraft.rccity.commands.AdminCommands;
import de.raidcraft.rccity.commands.PlayerCommands;
import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Contribution;
import de.raidcraft.rccity.entities.Resident;
import io.ebean.Database;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@PluginMain
public class RCCity extends JavaPlugin {

    @Getter
    @Accessors(fluent = true)
    private static RCCity instance;

    private Database database;

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
        saveDefaultConfig();
    }

    private void setupListener() {


    }

    private void setupCommands() {

        this.commandManager = new PaperCommandManager(this);

        // contexts
        cityContext(commandManager);
        residentContext(commandManager);

        // completions
        cityCompletion(commandManager);

        commandManager.registerCommand(new AdminCommands(this));
        commandManager.registerCommand(new PlayerCommands(this));
    }

    private void cityContext(PaperCommandManager commandManager) {

        commandManager.getCommandContexts().registerContext(City.class, context -> {
            String arg = context.popFirstArg();
            if (Strings.isNullOrEmpty(arg)) return null;
            return City.byName(arg).orElseThrow(() -> new InvalidCommandArgument("Es gibt keine Stadt mit dem Namen " + arg));
        });
    }

    private void cityCompletion(PaperCommandManager commandManager) {

        commandManager.getCommandCompletions().registerAsyncCompletion("cities",
                context -> City.find.all().stream().map(City::name).collect(Collectors.toSet()));
    }

    private void residentContext(PaperCommandManager commandManager) {

        commandManager.getCommandContexts().registerIssuerAwareContext(Resident.class, context -> {

            if (context.hasFlag("self")) {
                return Resident.getOrCreate(context.getPlayer());
            }

            String arg = context.popFirstArg();
            Player player;
            if (!Strings.isNullOrEmpty(arg) && arg.startsWith("@")) {
                player = selectPlayer(context.getSender(), arg);
            } else {
                if (Strings.isNullOrEmpty(arg)) {
                    return Resident.getOrCreate(context.getPlayer());
                }
                try {
                    UUID uuid = UUID.fromString(arg);
                    return Resident.find.byId(uuid);
                } catch (Exception e) {
                    Optional<Resident> achievementPlayer = Resident.byName(arg);
                    if (achievementPlayer.isPresent()) return achievementPlayer.get();
                    player = Bukkit.getPlayerExact(arg);
                }
            }

            if (player == null) {
                throw new InvalidCommandArgument("Der Spieler " + arg + " wurde nicht gefunden.");
            }

            return Resident.getOrCreate(player);
        });
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

    private Player selectPlayer(CommandSender sender, String playerIdentifier) {

        List<Player> matchedPlayers;
        try {
            matchedPlayers = getServer().selectEntities(sender, playerIdentifier).parallelStream()
                    .unordered()
                    .filter(e -> e instanceof Player)
                    .map(e -> ((Player) e))
                    .collect(Collectors.toList());
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidCommandArgument(String.format("Error parsing selector '%s' for %s! See console for more details",
                    playerIdentifier, sender.getName()));
        }
        if (matchedPlayers.isEmpty()) {
            throw new InvalidCommandArgument(String.format("No player found with selector '%s' for %s",
                    playerIdentifier, sender.getName()));
        }
        if (matchedPlayers.size() > 1) {
            throw new InvalidCommandArgument(String.format("Error parsing selector '%s' for %s. ambiguous result (more than one player matched) - %s",
                    playerIdentifier, sender.getName(), matchedPlayers.toString()));
        }

        return matchedPlayers.get(0);
    }
}
