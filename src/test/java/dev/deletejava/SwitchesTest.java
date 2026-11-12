package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.switches.Zone;
import dev.deletejava.switches.after.Delivery;
import org.junit.jupiter.api.Test;

class SwitchesTest {

    @Test
    // Compares delivery-day results for every zone.
    void sameDaysForEveryZone() {
        for (Zone zone : Zone.values()) {
            assertEquals(dev.deletejava.switches.before.Delivery.days(zone), Delivery.days(zone), zone.name());
        }
    }

    @Test
    // Verifies that both implementations reject a null zone.
    void bothRejectNull() {
        assertThrows(NullPointerException.class, () -> dev.deletejava.switches.before.Delivery.days(null));
        assertThrows(NullPointerException.class, () -> Delivery.days(null));
    }
}
