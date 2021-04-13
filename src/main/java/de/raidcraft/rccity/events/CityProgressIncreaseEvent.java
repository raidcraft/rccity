package de.raidcraft.rccity.events;

import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.CityProgress;
import de.raidcraft.rccity.entities.Resident;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The event is fired when a resident increases the progress of a city.
 * <p>The progress can be increased by calling {@link CityProgress#increase(Resident, float)}.
 * <p>The {@link CityProgressIncreasedEvent} is fired after this event if it is not cancelled.
 */
public class CityProgressIncreaseEvent extends RCCityEvent implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final CityProgress progress;
    @Getter
    private final Resident resident;
    @Getter
    @Setter
    private float increase;

    @Getter
    @Setter
    private boolean cancelled;

    public CityProgressIncreaseEvent(CityProgress progress, Resident resident, float value) {
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
