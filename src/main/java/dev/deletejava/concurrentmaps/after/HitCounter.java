package dev.deletejava.concurrentmaps.after;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// ConcurrentHashMap with its atomic compound operations (Java 8): no locks in sight,
/// and iterating while other threads write is safe.
public class HitCounter {

    private final Map<String, Integer> hits = new ConcurrentHashMap<>();

    // Increments the hit count for a page.
    public void record(String page) {
        hits.merge(page, 1, Integer::sum);
    }

    // Returns the current hit count for a page.
    public int count(String page) {
        return hits.getOrDefault(page, 0);
    }

    // Returns an immutable copy of all page hit counts.
    public Map<String, Integer> snapshot() {
        return Map.copyOf(hits);
    }
}
