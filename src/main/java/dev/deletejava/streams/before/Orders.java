package dev.deletejava.streams.before;

import java.util.List;
import java.util.stream.Collectors;

/** collect(Collectors.toList()), the most typed line in Java 8 code. */
public final class Orders {

    // Returns IDs whose corresponding paid flag is true.
    public static List<String> paidIds(List<String> ids, List<Boolean> paid) {
        return ids.stream()
                .filter(id -> paid.get(ids.indexOf(id)))
                .collect(Collectors.toList());
    }

    private Orders() {}
}
