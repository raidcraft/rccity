package de.raidcraft.rccity.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import de.raidcraft.rccity.Constants;
import de.raidcraft.rccity.Messages;
import de.raidcraft.rccity.RCCity;
import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Resident;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import static de.raidcraft.rccity.Messages.Colors.ERROR;
import static de.raidcraft.rccity.Messages.cityCreated;
import static de.raidcraft.rccity.Messages.increasedProgress;

@CommandAlias("rccity:admin|rccadmin")
@CommandPermission(Constants.Permissions.ADMIN)
public class AdminCommands extends BaseCommand {

    private final RCCity plugin;

    public AdminCommands(RCCity plugin) {
        this.plugin = plugin;
    }

    @CommandPermission(Constants.Permissions.ADMIN + ".create")
    @Subcommand("create")
    public void createCity(String name) {

        Messages.send(getCurrentCommandIssuer(), cityCreated(name));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> City.create(name));
    }

    @CommandPermission(Constants.Permissions.ADMIN_PROGRESS)
    @Subcommand("progress")
    public class Progress extends BaseCommand {

        @CommandCompletion("@cities @players value")
        @CommandPermission(Constants.Permissions.ADMIN_PROGRESS + ".add")
        @Subcommand("add|increase|oontribute")
        public void increase(City city, Resident resident, float value) {

            final CommandIssuer issuer = getCurrentCommandIssuer();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                CityProgress progress = city.progress();
                if (progress.increase(resident, value)) {
                    Messages.send(issuer, increasedProgress(progress, resident, value));
                } else {
                    Messages.send(issuer, Component.text("Der Fortschritt konnte nicht erhöht werden.", ERROR));
                }
            });
        }
    }
}
