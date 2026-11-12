package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.utilities.after.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;

class UtilitiesTest {

    private final List<String> events = List.of("created", "paid", "shipped");

    @Test
    // Compares utility method results between the two implementations.
    void sameResults() {
        assertEquals(dev.deletejava.utilities.before.Utils.banner("Hi"), Utils.banner("Hi"));
        for (String s : new String[] {null, "", "  ", "x"}) {
            assertEquals(dev.deletejava.utilities.before.Utils.isBlank(s), Utils.isBlank(s), "isBlank(" + s + ")");
        }
        assertEquals(dev.deletejava.utilities.before.Utils.displayName(null, "Ada"), Utils.displayName(null, "Ada"));
        assertEquals(dev.deletejava.utilities.before.Utils.immutableTags("a", "b"), Utils.immutableTags("a", "b"));
        assertEquals(dev.deletejava.utilities.before.Utils.lastEvent(events), Utils.lastEvent(events));
        assertEquals(dev.deletejava.utilities.before.Utils.newestFirst(events), Utils.newestFirst(events));
        byte[] bytes = {0, 15, -1};
        assertEquals(dev.deletejava.utilities.before.Utils.toHex(bytes), Utils.toHex(bytes));
        for (int v : new int[] {-5, 50, 150}) {
            assertEquals(dev.deletejava.utilities.before.Utils.clampPercent(v), Utils.clampPercent(v));
        }
    }

    @Test
    // Verifies that utility methods return lists that cannot be modified.
    void immutableListsRejectWrites() {
        assertThrows(UnsupportedOperationException.class, () -> Utils.immutableTags("a").add("b"));
    }
}
