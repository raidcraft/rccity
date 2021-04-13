package de.raidcraft.rccity;

import co.aikar.commands.CommandIssuer;
import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Resident;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

import static de.raidcraft.rccity.Messages.Colors.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;

public final class Messages {

    private Messages() {}

    public static final class Colors {

        public static final TextColor BASE = YELLOW;

        public static final TextColor TEXT = BASE;
        public static final TextColor HIDDEN = GRAY;
        public static final TextColor DISABLED = DARK_GRAY;
        public static final TextColor ACCENT = GOLD;
        public static final TextColor DARK_ACCENT = DARK_AQUA;
        public static final TextColor HIGHLIGHT = AQUA;
        public static final TextColor DARK_HIGHLIGHT = DARK_AQUA;
        public static final TextColor ERROR = RED;
        public static final TextColor ERROR_ACCENT = DARK_RED;
        public static final TextColor SUCCESS = GREEN;
        public static final TextColor SUCCESS_ACCENT = DARK_GREEN;
        public static final TextColor WARNING = GOLD;
        public static final TextColor NOTE = GRAY;
    }
    public static void send(UUID playerId, Component message) {

        if (RCCity.isTesting()) return;
        BukkitAudiences.create(RCCity.instance())
                .player(playerId)
                .sendMessage(message);
    }

    public static void send(UUID playerId, Consumer<TextComponent.Builder> message) {

        TextComponent.Builder builder = text();
        message.accept(builder);
        send(playerId, builder.build());
    }

    public static void send(Object commandIssuer, Component message) {

        if (commandIssuer instanceof Player) {
            sendPlayer((Player) commandIssuer, message);
        } else if (commandIssuer instanceof ConsoleCommandSender) {
            sendConsole((ConsoleCommandSender) commandIssuer, message);
        } else if (commandIssuer instanceof RemoteConsoleCommandSender) {
            sendRemote((RemoteConsoleCommandSender) commandIssuer, message);
        } else if (commandIssuer instanceof CommandIssuer) {
            send((Object) ((CommandIssuer) commandIssuer).getIssuer(), message);
        }
    }

    public static void send(UUID target, Title title) {

        if (RCCity.isTesting()) return;
        BukkitAudiences.create(RCCity.instance())
                .player(target)
                .showTitle(title);
    }

    public static void sendPlayer(Player player, Component message) {
        send(player.getUniqueId(), message);
    }

    public static void sendConsole(ConsoleCommandSender sender, Component message) {

        sender.sendMessage(PlainComponentSerializer.plain().serialize(message));
    }

    public static void sendRemote(RemoteConsoleCommandSender sender, Component message) {

        sender.sendMessage(PlainComponentSerializer.plain().serialize(message));
    }

    public static Component cityCreated(String name) {

        return text("Die Stadt ", SUCCESS)
                .append(text(name, ACCENT))
                .append(text(" wurde erfolgreich erstellt.", SUCCESS));
    }

    public static Component increasedProgress(CityProgress progress, Resident resident, float increase) {

        return text("Der Fortschritt der Stadt ", TEXT)
                .append(city(progress.city()))
                .append(text(" wurde um ", TEXT))
                .append(text(increase, HIGHLIGHT))
                .append(text(" auf ", TEXT))
                .append(text(progress.value(), DARK_HIGHLIGHT))
                .append(text("/", ACCENT))
                .append(text(progress.requiredValue(), HIGHLIGHT))
                .append(text(" von ", TEXT))
                .append(resident(resident))
                .append(text(" erhöht.", SUCCESS));
    }

    public static Component city(City city) {

        return text(city.name(), ACCENT, UNDERLINED);
    }

    public static Component resident(Resident resident) {

        return text(resident.name(), ACCENT, UNDERLINED);
    }
}
