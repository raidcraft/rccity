package de.raidcraft.rccity.events;

import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Resident;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired after the progress of a city has been increased by a resident.
 * <p>Use the {@link CityProgressIncreaseEvent} to influence the outcome of the increase.
 */
public class CityProgressIncreasedEvent extends RCCityEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final CityProgress progress;
    @Getter
    private final Resident resident;
    @Getter
    private final float increase;

    public CityProgressIncreasedEvent(CityProgress progress, Resident resident, float value) {
        this.progress = progress;
        this.resident = resident;
        this.increase = value;
    }

    public City getCity() {

        return progress.city();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {

        return handlerList;
    }
}
