package de.raidcraft.rccity;

/**
 * An adapter handles the integration of other plugins with this RCCity plugin.
 */
public interface Adapter {

    /**
     * Registers the given adapter with this plugin.
     * <p>Any exception thrown is caught and logged.
     *
     * @param plugin the RCCity plugin that calls the adapter
     * @throws Exception if any error occurred during the registration, e.g. the plugin is not enabled
     */
    void register(RCCity plugin) throws Exception;
}
