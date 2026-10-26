package dev.deletejava.constructors;

/** A base class whose constructor the subclasses must call. */
public class Parcel {
    private final String trackingCode;
    private final double weightKg;

    public Parcel(String trackingCode, double weightKg) {
        this.trackingCode = trackingCode;
        this.weightKg = weightKg;
    }

    public String trackingCode() { return trackingCode; }
    public double weightKg() { return weightKg; }
}
