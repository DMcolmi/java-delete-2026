package dev.deletejava.utilities.after;

import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/// The same helpers using only the JDK.
public final class Utils {

    // Wraps a title in a banner made of equals signs.
    public static String banner(String title) {
        String line = "=".repeat(title.length());                    // Java 11
        return line + "\n" + title + "\n" + line;
    }

    // Checks whether a string is null, empty, or whitespace-only.
    public static boolean isBlank(String s) {
        return s == null || s.isBlank();                             // Java 11; String.isBlank() alone is not null-safe
    }

    // Chooses a non-blank nickname or falls back to the full name.
    public static String displayName(String nickname, String fullName) {
        return Objects.requireNonNullElse(nickname, fullName);       // Java 9
    }

    // Returns an immutable list containing the supplied tags.
    public static List<String> immutableTags(String... tags) {
        return List.of(tags);                                        // Java 9
    }

    // Returns the most recent event, or a fallback for an empty list.
    public static String lastEvent(List<String> events) {
        return events.getLast();                                     // Java 21, SequencedCollection
    }

    // Returns an immutable copy of events in reverse chronological order.
    public static List<String> newestFirst(List<String> events) {
        return events.reversed();                                    // Java 21, a view like Lists.reverse
    }

    // Encodes a byte array as a lowercase hexadecimal string.
    public static String toHex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);                      // Java 17
    }

    // Restricts a percentage value to the inclusive range from 0 to 100.
    public static int clampPercent(int value) {
        return Math.clamp(value, 0, 100);                            // Java 21
    }

    private Utils() {}
}
