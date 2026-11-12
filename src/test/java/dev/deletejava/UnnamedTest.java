package dev.deletejava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.deletejava.unnamed.after.Inventory;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UnnamedTest {

    @Test
    // Compares inventory calculations between the two implementations.
    void sameResults() {
        assertEquals(dev.deletejava.unnamed.before.Inventory.parseOrZero("x"), Inventory.parseOrZero("x"));
        assertEquals(dev.deletejava.unnamed.before.Inventory.parseOrZero("42"), Inventory.parseOrZero("42"));
        assertEquals(dev.deletejava.unnamed.before.Inventory.count(List.of("a", "b")), Inventory.count(List.of("a", "b")));
        var stock = Map.of("a", 3, "b", 4);
        assertEquals(dev.deletejava.unnamed.before.Inventory.totalStock(stock), Inventory.totalStock(stock));

        int before = dev.deletejava.unnamed.before.Inventory.netChange(List.of(
                new dev.deletejava.unnamed.before.Inventory.Restocked("a", 10, "MXP"),
                new dev.deletejava.unnamed.before.Inventory.Sold("a", 3, "web")));
        int after = Inventory.netChange(List.of(
                new Inventory.Restocked("a", 10, "MXP"),
                new Inventory.Sold("a", 3, "web")));
        assertEquals(7, after);
        assertEquals(before, after);
    }
}
