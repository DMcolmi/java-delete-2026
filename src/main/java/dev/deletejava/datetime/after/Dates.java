package dev.deletejava.datetime.after;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/// java.time (Java 8, and still not everywhere): immutable, thread-safe types,
/// and a `LocalDate` is a calendar day, so daylight saving time can't shift it.
public final class Dates {

    // Adds the requested number of weekdays to an ISO date.
    public static String plusBusinessDays(String isoDate, int days) {
        LocalDate date = LocalDate.parse(isoDate);
        int added = 0;
        while (added < days) {
            date = date.plusDays(1);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return date.toString();
    }

    // Returns the number of calendar days between two ISO dates.
    public static long daysBetween(String fromIsoDate, String toIsoDate) {
        return ChronoUnit.DAYS.between(LocalDate.parse(fromIsoDate), LocalDate.parse(toIsoDate));
    }

    private Dates() {}
}
