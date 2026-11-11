package dev.deletejava.utilities.before;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/** Everyday helpers that used to need Guava or Commons Lang. */
public final class Utils {

    // Wraps a title in a banner made of equals signs.
    public static String banner(String title) {
        String line = StringUtils.repeat("=", title.length());
        return line + "\n" + title + "\n" + line;
    }

    // Checks whether a string is null, empty, or whitespace-only.
    public static boolean isBlank(String s) {
        return StringUtils.isBlank(s);
    }

    // Chooses a non-blank nickname or falls back to the full name.
    public static String displayName(String nickname, String fullName) {
        return MoreObjects.firstNonNull(nickname, fullName);
    }

    // Returns an immutable list containing the supplied tags.
    public static List<String> immutableTags(String... tags) {
        return ImmutableList.copyOf(tags);
    }

    // Returns the most recent event, or a fallback for an empty list.
    public static String lastEvent(List<String> events) {
        return Iterables.getLast(events);
    }

    // Returns an immutable copy of events in reverse chronological order.
    public static List<String> newestFirst(List<String> events) {
        return Lists.reverse(events);
    }

    // Encodes a byte array as a lowercase hexadecimal string.
    public static String toHex(byte[] bytes) {
        return BaseEncoding.base16().lowerCase().encode(bytes);
    }

    // Restricts a percentage value to the inclusive range from 0 to 100.
    public static int clampPercent(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private Utils() {}
}
