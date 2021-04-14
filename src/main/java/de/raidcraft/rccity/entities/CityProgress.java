package de.raidcraft.rccity.entities;

import de.raidcraft.rccity.events.CityProgressChangeEvent;
import de.raidcraft.rccity.events.CityProgressChangedEvent;
import io.ebean.annotation.DbEnumValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;
import org.bukkit.Bukkit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rccity_progress")
public class CityProgress extends BaseEntity {

    public static CityProgress create(City city) {

        CityProgress progress = new CityProgress(city);
        progress.save();
        return progress;
    }

    @ManyToOne
    private City city;
    private Status status = Status.ACTIVE;
    private float requiredValue;
    @Setter(AccessLevel.PRIVATE)
    private float value;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Contribution> contributions = new ArrayList<>();

    CityProgress(City city) {

        this.city = city;
    }

    /**
     * Increases the progress of the city behind this city progress by the given amount.
     * <p>A {@link CityProgressChangeEvent} is fired and can be cancelled.
     *
     * @param resident the resident that increased the progress
     * @param value the value to increase the progress by
     * @return true if the progress was increase or false if the event was cancelled
     */
    public boolean increase(Resident resident, float value) {

        return setValue(resident, this.value() + value);
    }

    /**
     * Sets the progress of the city behind this city progress to the given amount.
     * <p>A {@link CityProgressChangeEvent} is fired and can be cancelled.
     *
     * @param resident the resident that sets the progress
     * @param value the value to set the progress to
     * @return true if the progress was changed or false if the event was cancelled
     */
    public boolean setValue(Resident resident, float value) {

        final float oldValue = this.value();
        CityProgressChangeEvent event = new CityProgressChangeEvent(this, resident, oldValue, value);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return false;
        value = event.getValue();

        Contribution.create(this, resident, value);
        this.value(value).save();

        Bukkit.getPluginManager().callEvent(new CityProgressChangedEvent(this, resident, oldValue, value));

        return true;
    }

    public enum Status {

        ACTIVE,
        EXPIRED,
        SUCCESSFUL;

        @DbEnumValue
        public String getValue() {

            return name();
        }
    }
}
