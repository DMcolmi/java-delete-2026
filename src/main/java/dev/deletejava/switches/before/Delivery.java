package dev.deletejava.switches.before;

import dev.deletejava.switches.Zone;

/** A switch statement: a variable to assign, a break per case, and a default "just in case". */
public final class Delivery {

    public static int days(Zone zone) {
        int days;
        switch (zone) {
            case LOCAL:
                days = 1;
                break;
            case NATIONAL:
            case ISLANDS:
                days = 3;
                break;
            case EU:
                days = 5;
                break;
            default:
                throw new IllegalArgumentException("unknown zone " + zone);
        }
        return days;
    }

    private Delivery() {}
}
