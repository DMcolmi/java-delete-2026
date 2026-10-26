package dev.deletejava.constructors;

/** A base class whose constructor the subclasses must call. */
public class Parcel {
    private final String trackingCode;
    private final double weightKg;

    public Parcel(String trackingCode, double weightKg) {
        this.trackingCode = trackingCode;
        this.weightKg = weightKg;
    }

    // Returns this parcel's tracking code.
    public String trackingCode() { return trackingCode; }
    // Returns this parcel's weight in kilograms.
    public double weightKg() { return weightKg; }
}
