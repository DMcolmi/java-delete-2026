package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.instanceofcast.after.JsonWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class InstanceofCastTest {

    @Test
    // Compares JSON output for the supported value types.
    void sameJson() {
        var order = new LinkedHashMap<String, Object>();
        order.put("id", 42);
        order.put("customer", "Ada \"the first\"");
        order.put("paid", true);
        order.put("items", List.of("book", 2.5));
        order.put("notes", null);
        order.put("tags", Arrays.asList("x", null));

        String expected = dev.deletejava.instanceofcast.before.JsonWriter.write(order);
        assertEquals(expected, JsonWriter.write(order));
        assertEquals("{\"id\":42,\"customer\":\"Ada \\\"the first\\\"\",\"paid\":true,"
                + "\"items\":[\"book\",2.5],\"notes\":null,\"tags\":[\"x\",null]}", expected);
    }

    @Test
    // Verifies that both serializers reject unsupported value types.
    void bothRejectUnknownTypes() {
        assertThrows(IllegalArgumentException.class, () -> dev.deletejava.instanceofcast.before.JsonWriter.write(new Object()));
        assertThrows(IllegalArgumentException.class, () -> JsonWriter.write(new Object()));
    }
}
