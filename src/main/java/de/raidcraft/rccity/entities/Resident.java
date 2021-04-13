package de.raidcraft.rccity.entities;

import com.google.common.base.Strings;
import io.ebean.Finder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rccity_residents")
public class Resident extends BaseEntity {

    public static final Finder<UUID, Resident> find = new Finder<>(Resident.class);

    /**
     * Gets or creates a new resident from the given offline player.
     * <p>The id will be the same as the players id.
     *
     * @param player the player to create or get the resident for
     * @param city the city the player is a member of
     * @return the created or existing achievement player
     */
    public static Resident getOrCreate(OfflinePlayer player, City city) {

        return Optional.ofNullable(find.byId(player.getUniqueId()))
                .orElseGet(() -> {
                    Resident achievementPlayer = new Resident(player, city);
                    achievementPlayer.insert();
                    return achievementPlayer;
                });
    }

    /**
     * Gets or creates a new resident from the given offline player.
     * <p>The id will be the same as the players id.
     *
     * @param player the player to create or get the resident for
     * @return the created or existing achievement player
     */
    public static Resident getOrCreate(OfflinePlayer player) {

        return Optional.ofNullable(find.byId(player.getUniqueId()))
                .orElseGet(() -> {
                    Resident achievementPlayer = new Resident(player);
                    achievementPlayer.insert();
                    return achievementPlayer;
                });
    }

    /**
     * Tries to find a resident with the given name.
     * <p>The name can be case insensitive.
     *
     * @param name the name of the player. can be case insensitive.
     * @return the player if found
     */
    public static Optional<Resident> byName(String name) {

        if (Strings.isNullOrEmpty(name)) return Optional.empty();

        return find.query().where()
                .ieq("name", name)
                .findOneOrEmpty();
    }

    /**
     * Tries to find a resident with the given id.
     * <p>The id is the same as the Minecraft's player id.
     * <p>Returns an empty optional if no resident by the id is found.
     *
     * @param uuid the unique id of the player
     * @return the resident or an empty optional
     */
    public static Optional<Resident> byId(UUID uuid) {

        if (uuid == null) return Optional.empty();

        return Optional.ofNullable(find.byId(uuid));
    }

    /**
     * The name of the resident and resident.
     */
    @Setter(AccessLevel.PRIVATE)
    private String name;

    @ManyToOne
    private City city;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Contribution> contributions = new ArrayList<>();

    Resident(OfflinePlayer player) {

        this.id(player.getUniqueId());
        this.name(player.getName());
    }

    Resident(OfflinePlayer player, City city) {

        this(player);
        this.city(city);
    }

    /**
     * @return the offline player of this resident
     */
    public OfflinePlayer offlinePlayer() {

        return Bukkit.getOfflinePlayer(id());
    }

    /**
     * @return the online player of this resident if it is online
     */
    public Optional<Player> bukkitPlayer() {

        return Optional.ofNullable(Bukkit.getPlayer(id()));
    }
}
