package dev.deletejava.pinning.after;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Supplier;

/// Since Java 24 (JEP 491) a virtual thread that blocks inside synchronized releases its carrier.
/// Plain synchronized is fine again, and -Djdk.tracePinnedThreads can leave your JVM flags too.
public class TokenCache {

    public record Token(String value, Instant expiresAt) {}

    private final Supplier<Token> fetcher; // blocking call to the identity provider
    private final Clock clock;
    private Token current;

    public TokenCache(Supplier<Token> fetcher, Clock clock) {
        this.fetcher = fetcher;
        this.clock = clock;
    }

    // Returns a cached token or fetches a new one when the cached token expires.
    public synchronized String token() {
        if (current == null || !clock.instant().isBefore(current.expiresAt())) {
            current = fetcher.get();
        }
        return current.value();
    }
}
