package de.raidcraft.rccity.entities;

import com.google.common.base.Strings;
import io.ebean.Finder;
import io.ebean.annotation.Index;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rccity_cities")
public class City extends BaseEntity {

    public static final Finder<UUID, City> find = new Finder<>(City.class);

    public static City create(String name) {

        City city = new City(name);
        city.save();
        return city;
    }

    /**
     * Tries to find a city with the given name.
     * <p>The name can be case insensitive.
     *
     * @param name the name of the city. can be case insensitive.
     * @return the city if found
     */
    public static Optional<City> byName(String name) {

        if (Strings.isNullOrEmpty(name)) return Optional.empty();

        return find.query().where()
                .ieq("name", name)
                .findOneOrEmpty();
    }

    @Index(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Resident> residents = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<CityProgress> progressHistory = new ArrayList<>();

    City(String name) {
        this.name = name;
    }

    /**
     * @return the current active progress of this city
     */
    public CityProgress progress() {

        return progressHistory()
                .stream().filter(cityProgress -> cityProgress.status() == CityProgress.Status.ACTIVE)
                .findFirst()
                .orElse(CityProgress.create(this));
    }
}
