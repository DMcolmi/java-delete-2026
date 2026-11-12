package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordsTest {

    @Test
    // Verifies that the record and class versions have matching value behavior.
    void sameValueSemantics() {
        var items = new ArrayList<>(List.of("book"));
        var lombok = new dev.deletejava.records.before.Order("1", "ada", items, BigDecimal.TEN);
        var record = new dev.deletejava.records.after.Order("1", "ada", items, BigDecimal.TEN);
        items.add("pen");

        assertEquals(List.of("book"), lombok.getItems());
        assertEquals(List.of("book"), record.items());
        assertEquals(new dev.deletejava.records.after.Order("1", "ada", List.of("book"), BigDecimal.TEN), record);
    }

    @Test
    // Verifies that both order implementations reject invalid totals.
    void sameValidation() {
        assertThrows(NullPointerException.class,
                () -> new dev.deletejava.records.before.Order(null, "ada", List.of(), BigDecimal.ONE));
        assertThrows(NullPointerException.class,
                () -> new dev.deletejava.records.after.Order(null, "ada", List.of(), BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class,
                () -> new dev.deletejava.records.before.Order("1", "ada", List.of(), BigDecimal.valueOf(-1)));
        assertThrows(IllegalArgumentException.class,
                () -> new dev.deletejava.records.after.Order("1", "ada", List.of(), BigDecimal.valueOf(-1)));
    }
}
