package dev.deletejava.constructors.after;

import dev.deletejava.constructors.Parcel;

/// Flexible constructor bodies (final in Java 25): statements before super(),
/// as long as they don't touch `this`. Validation reads top to bottom.
public class Shipment extends Parcel {

    public Shipment(String trackingCode, double weightKg) {
        if (trackingCode == null || trackingCode.isBlank()) {
            throw new IllegalArgumentException("tracking code is required");
        }
        if (weightKg <= 0 || weightKg > 30) {
            throw new IllegalArgumentException("weight must be in (0, 30] kg");
        }
        super(trackingCode.strip().toUpperCase(), weightKg);
    }
}
