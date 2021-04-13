package de.raidcraft.rccity.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class RCCityEvent extends Event {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
}
