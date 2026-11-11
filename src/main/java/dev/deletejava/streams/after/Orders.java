package dev.deletejava.streams.after;

import java.util.List;

/// `Stream.toList()` (Java 16). Unlike `Collectors.toList()`, the list it returns is unmodifiable.
public final class Orders {

    // Returns IDs whose corresponding paid flag is true.
    public static List<String> paidIds(List<String> ids, List<Boolean> paid) {
        return ids.stream()
                .filter(id -> paid.get(ids.indexOf(id)))
                .toList();
    }

    private Orders() {}
}
