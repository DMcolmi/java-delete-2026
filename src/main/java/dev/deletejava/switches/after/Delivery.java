package dev.deletejava.switches.after;

import dev.deletejava.switches.Zone;

/// Switch expression (Java 14): no fall-through, no break, and no default needed.
/// It's exhaustive over the enum: add a zone and this stops compiling.
public final class Delivery {

    // Returns the delivery time in days for a zone.
    public static int days(Zone zone) {
        return switch (zone) {
            case LOCAL -> 1;
            case NATIONAL, ISLANDS -> 3;
            case EU -> 5;
        };
    }

    private Delivery() {}
}
