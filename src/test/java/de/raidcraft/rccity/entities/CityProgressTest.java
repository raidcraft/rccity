package de.raidcraft.rccity.entities;

import de.raidcraft.rccity.TestBase;
import de.raidcraft.rccity.events.CityProgressChangeEvent;
import de.raidcraft.rccity.events.CityProgressChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CityProgressTest extends TestBase {

    @Captor
    ArgumentCaptor<CityProgressChangedEvent> argCaptor;

    private TestListener listener;

    @Override
    @BeforeEach
    public void setUp() {

        super.setUp();

        MockitoAnnotations.initMocks(this);

        listener = spy(new TestListener());
        Bukkit.getPluginManager().registerEvents(listener, plugin());
    }

    @Nested
    @DisplayName("increase(...)")
    class increase {

        @Test
        @DisplayName("should fire increase progress event")
        void shouldFireIncreaseProgressEvent() {

            city().progress().increase(resident(), 20f);

            verify(listener, times(1)).onProgressEvent(any());
        }

        @Test
        @DisplayName("should increase and save city progress")
        void shouldIncreaseCityProgress() {

            city().progress().increase(resident(), 100f);

            city().refresh();

            assertThat(city().progress())
                    .extracting(CityProgress::value)
                    .isEqualTo(100f);
        }

        @Test
        @DisplayName("should allow cancelling the progress increase")
        void shouldAllowCancellingTheProgressIncrease() {

            listener.cancel = true;
            assertThat(city().progress().increase(resident(), 100f))
                    .isFalse();

            city().refresh();

            assertThat(city().progress())
                    .extracting(CityProgress::value)
                    .isEqualTo(0f);
        }

        @Test
        @DisplayName("should fire progress changed event")
        void shouldFireProgressChangedEvent() {

            city().progress().increase(resident(), 100f);

            verify(listener, times(1))
                    .onProgressChangedEvent(argCaptor.capture());

            assertThat(argCaptor.getValue())
                    .extracting(CityProgressChangedEvent::getOldValue, CityProgressChangedEvent::getValue)
                    .contains(0f, 100f);
        }

        @Test
        @DisplayName("should use the new value if changed in the event")
        void shouldUseTheNewValueIfChangedInTheEvent() {

            listener.value = 20f;
            assertThat(city().progress().increase(resident(), 100f))
                    .isTrue();

            city().refresh();

            assertThat(city().progress())
                    .extracting(CityProgress::value)
                    .isEqualTo(20f);
        }
    }

    static class TestListener implements Listener {

        private boolean cancel = false;
        private float value = 0f;

        @EventHandler
        public void onProgressEvent(CityProgressChangeEvent event) {

            event.setCancelled(cancel);
            if (value > 0) {
                event.setValue(value);
            }
        }

        @EventHandler
        public void onProgressChangedEvent(CityProgressChangedEvent event) {

        }
    }
}