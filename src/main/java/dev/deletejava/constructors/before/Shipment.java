package dev.deletejava.constructors.before;

import dev.deletejava.constructors.Parcel;

/** super() had to be the first statement, so validation hides in static helpers called inside the argument list. */
public class Shipment extends Parcel {

    public Shipment(String trackingCode, double weightKg) {
        super(normalize(trackingCode), checkWeight(weightKg));
    }

    // Trims and uppercases a tracking code after validating it.
    private static String normalize(String trackingCode) {
        if (trackingCode == null || trackingCode.isBlank()) {
            throw new IllegalArgumentException("tracking code is required");
        }
        return trackingCode.strip().toUpperCase();
    }

    // Validates a shipment weight and returns it unchanged.
    private static double checkWeight(double weightKg) {
        if (weightKg <= 0 || weightKg > 30) {
            throw new IllegalArgumentException("weight must be in (0, 30] kg");
        }
        return weightKg;
    }
}
