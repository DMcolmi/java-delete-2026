package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.optional.Customer;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class OptionalTest {

    private final Map<String, Customer> customers = Map.of(
            "1", new Customer("1", "Ada", "ada@example.com"),
            "2", new Customer("2", "Linus", null));

    private final dev.deletejava.optional.before.CustomerDirectory before =
            new dev.deletejava.optional.before.CustomerDirectory(customers);
    private final dev.deletejava.optional.after.CustomerDirectory after =
            new dev.deletejava.optional.after.CustomerDirectory(customers);

    @Test
    // Compares lookup and fallback results between the two directory implementations.
    void sameResults() {
        for (String id : List.of("1", "2", "404")) {
            assertEquals(before.emailOrDefault(id), after.emailOrDefault(id), id);
        }
        assertEquals(before.nameOrThrow("1"), after.nameOrThrow("1"));
        assertEquals(before.emails(List.of("1", "2", "404")), after.emails(List.of("1", "2", "404")));
    }

    @Test
    // Verifies that the updated lookup API represents missing customers as empty.
    void notFoundIsInTheTypeNotANull() {
        assertNull(before.find("404"));
        assertTrue(after.find("404").isEmpty());
        assertThrows(NoSuchElementException.class, () -> before.nameOrThrow("404"));
        assertThrows(NoSuchElementException.class, () -> after.nameOrThrow("404"));
    }
}
