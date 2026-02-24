package leo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import leo.exception.LeoException;

public class DateTimeUtil {
    private static final DateTimeFormatter OUTPUT_DATE =
            DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    private static final DateTimeFormatter ISO_DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ISO_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter SLASH_DATE_TIME =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /**
     * Accepts:
     *  - yyyy-MM-dd
     *  - yyyy-MM-dd HHmm
     *  - d/M/yyyy HHmm
     */
    public static LocalDateTime parseDateTime(String raw) throws LeoException {
        String s = raw.trim();
        if (s.isEmpty()) {
            throw new LeoException("Date/time cannot be empty.");
        }

        // yyyy-MM-dd
        try {
            LocalDate d = LocalDate.parse(s, ISO_DATE);
            return d.atStartOfDay();
        } catch (DateTimeParseException ignored) {
            // try next
        }

        // yyyy-MM-dd HHmm
        try {
            return LocalDateTime.parse(s, ISO_DATE_TIME);
        } catch (DateTimeParseException ignored) {
            // try next
        }

        // d/M/yyyy HHmm
        try {
            return LocalDateTime.parse(s, SLASH_DATE_TIME);
        } catch (DateTimeParseException ignored) {
            // try next
        }

        throw new LeoException("Invalid date/time format. Try yyyy-MM-dd, yyyy-MM-dd HHmm, or d/M/yyyy HHmm.");
    }

    public static String format(LocalDateTime dt) {
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dt.toLocalDate().format(OUTPUT_DATE);
        }
        return dt.format(OUTPUT_DATE_TIME);
    }

    /**
     * Storage helper: parse what we saved.
     * Accepts:
     *  - ISO LocalDateTime (e.g., 2019-12-02T18:00)
     *  - ISO LocalDate (e.g., 2019-12-02)
     */
    public static LocalDateTime parseStored(String raw) throws LeoException {
        String s = raw.trim();
        try {
            if (s.contains("T")) {
                return LocalDateTime.parse(s);
            }
            return LocalDate.parse(s).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new LeoException("Corrupted date/time in save file: " + raw);
        }
    }

    public static String toStoredString(LocalDateTime dt) {
        return dt.toString(); // ISO_LOCAL_DATE_TIME, e.g. 2019-12-02T18:00
    }
}
