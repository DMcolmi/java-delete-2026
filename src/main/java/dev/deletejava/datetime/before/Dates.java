package dev.deletejava.datetime.before;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date, Calendar and one shared SimpleDateFormat "to avoid creating it every time".
 * SimpleDateFormat isn't thread-safe, and a Date is an instant, not a calendar day.
 */
public final class Dates {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Rome")); // where the servers are
    }

    // Adds the requested number of weekdays to an ISO date.
    public static String plusBusinessDays(String isoDate, int days) {
        Calendar calendar = Calendar.getInstance(FORMAT.getTimeZone());
        calendar.setTime(parse(isoDate));
        int added = 0;
        while (added < days) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                added++;
            }
        }
        return FORMAT.format(calendar.getTime());
    }

    // Returns the number of calendar days between two ISO dates.
    public static long daysBetween(String fromIsoDate, String toIsoDate) {
        long millis = parse(toIsoDate).getTime() - parse(fromIsoDate).getTime();
        return millis / (24 * 60 * 60 * 1000); // a day is 24 hours... except twice a year
    }

    // Parses an ISO date with the shared date formatter.
    private static Date parse(String isoDate) {
        try {
            return FORMAT.parse(isoDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Dates() {}
}
