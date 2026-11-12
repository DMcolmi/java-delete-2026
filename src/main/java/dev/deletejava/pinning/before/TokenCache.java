package dev.deletejava.pinning.before;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Java 21 workaround: a virtual thread blocking inside synchronized pinned its carrier thread,
 * so teams rewrote synchronized blocks as ReentrantLock with lock/try/finally.
 */
public class TokenCache {

    public record Token(String value, Instant expiresAt) {}

    private final ReentrantLock lock = new ReentrantLock();
    private final Supplier<Token> fetcher; // blocking call to the identity provider
    private final Clock clock;
    private Token current;

    public TokenCache(Supplier<Token> fetcher, Clock clock) {
        this.fetcher = fetcher;
        this.clock = clock;
    }

    // Returns a cached token or fetches a new one when the cached token expires.
    public String token() {
        lock.lock();
        try {
            if (current == null || !clock.instant().isBefore(current.expiresAt())) {
                current = fetcher.get();
            }
            return current.value();
        } finally {
            lock.unlock();
        }
    }
}
