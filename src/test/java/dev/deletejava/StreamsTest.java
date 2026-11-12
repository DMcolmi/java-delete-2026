package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.streams.after.Orders;
import java.util.List;
import org.junit.jupiter.api.Test;

class StreamsTest {

    private final List<String> ids = List.of("o1", "o2", "o3");
    private final List<Boolean> paid = List.of(true, false, true);

    @Test
    // Verifies that both implementations select the same paid order IDs.
    void sameElements() {
        assertEquals(dev.deletejava.streams.before.Orders.paidIds(ids, paid), Orders.paidIds(ids, paid));
    }

    @Test
    // Verifies that the stream toList result cannot be modified.
    void butToListIsUnmodifiable() {
        // If a caller adds to the result, that's the one place this change bites
        dev.deletejava.streams.before.Orders.paidIds(ids, paid).add("o4");
        assertThrows(UnsupportedOperationException.class, () -> Orders.paidIds(ids, paid).add("o4"));
    }
}
