package dev.deletejava.instanceofcast.after;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;

/// Pattern matching for switch (Java 21): test, cast and bind in one step, `case null` included.
/// `case Number _, Boolean _` groups types without binding a name (Java 22).
public final class JsonWriter {

    // Serializes supported Java values as JSON.
    public static String write(Object value) {
        return switch (value) {
            case null -> "null";
            case String s -> quote(s);
            case Number _, Boolean _ -> value.toString();
            case List<?> list -> list.stream().map(JsonWriter::write).collect(joining(",", "[", "]"));
            case Map<?, ?> map -> map.entrySet().stream()
                    .map(e -> quote(String.valueOf(e.getKey())) + ":" + write(e.getValue()))
                    .collect(joining(",", "{", "}"));
            default -> throw new IllegalArgumentException("unsupported type: " + value.getClass().getName());
        };
    }

    // Escapes a string and surrounds it with JSON quotes.
    private static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private JsonWriter() {}
}
