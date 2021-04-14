package de.raidcraft.rccity;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.raidcraft.rccity.entities.City;
import de.raidcraft.rccity.entities.Resident;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@Accessors(fluent = true)
@Getter
public class TestBase {

    private ServerMock server;
    private RCCity plugin;
    private City city;
    private PlayerMock player;
    private Resident resident;

    @BeforeEach
    protected void setUp() {

        server = MockBukkit.mock();
        plugin = MockBukkit.load(RCCity.class);

        player = server.addPlayer();
        city = City.create(RandomStringUtils.randomAlphanumeric(20));
        resident = Resident.getOrCreate(player, city);
    }

    @AfterEach
    protected void tearDown() {

        MockBukkit.unmock();
    }
}