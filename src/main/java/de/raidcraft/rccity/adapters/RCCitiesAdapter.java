package de.raidcraft.rccity.adapters;

import de.raidcraft.rccity.Adapter;
import de.raidcraft.rccity.RCCity;

/**
 * The adapter handles the integration with the RCCities (player cities) plugin.
 * <p>In particular it makes sure that players cannot join RCCity if they are
 * resident in another city.
 */
public class RCCitiesAdapter implements Adapter {

    @Override
    public void register(RCCity plugin) throws Exception {

    }
}
