package dev.deletejava.concurrentmaps.before;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Collections.synchronizedMap makes each call atomic, not the get-then-put between them,
 * so every compound action and every iteration still needs a synchronized block.
 */
public class HitCounter {

    private final Map<String, Integer> hits = Collections.synchronizedMap(new HashMap<>());

    // Increments the hit count for a page.
    public void record(String page) {
        synchronized (hits) {
            Integer current = hits.get(page);
            hits.put(page, current == null ? 1 : current + 1);
        }
    }

    // Returns the current hit count for a page.
    public int count(String page) {
        return hits.getOrDefault(page, 0);
    }

    // Returns an immutable copy of all page hit counts.
    public Map<String, Integer> snapshot() {
        synchronized (hits) { // copying iterates: without this lock, ConcurrentModificationException
            return Map.copyOf(hits);
        }
    }
}
