package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConstructorsTest {

    @Test
    // Verifies that both implementations normalize tracking codes identically.
    void sameNormalization() {
        var before = new dev.deletejava.constructors.before.Shipment("  ab123 ", 2.5);
        var after = new dev.deletejava.constructors.after.Shipment("  ab123 ", 2.5);
        assertEquals("AB123", before.trackingCode());
        assertEquals(before.trackingCode(), after.trackingCode());
        assertEquals(before.weightKg(), after.weightKg());
    }

    @Test
    // Verifies that both implementations reject invalid shipment values.
    void sameValidation() {
        assertThrows(IllegalArgumentException.class, () -> new dev.deletejava.constructors.before.Shipment(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> new dev.deletejava.constructors.after.Shipment(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> new dev.deletejava.constructors.before.Shipment("A", 31));
        assertThrows(IllegalArgumentException.class, () -> new dev.deletejava.constructors.after.Shipment("A", 31));
    }
}
