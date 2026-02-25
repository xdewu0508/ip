package leo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import leo.exception.LeoException;

/**
 * DateTimeUtil provides utility methods for parsing and formatting dates and times.
 * It supports multiple input formats for user convenience and ISO format for storage.
 */
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
     * Parses a date/time string from user input.
     * Accepts multiple formats: yyyy-MM-dd, yyyy-MM-dd HHmm, or d/M/yyyy HHmm.
     *
     * @param raw the raw date/time string to parse
     * @return the parsed LocalDateTime
     * @throws LeoException if the format is invalid or empty
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

    /**
     * Formats a LocalDateTime for display to the user.
     * Uses date-only format for midnight times, otherwise includes time.
     *
     * @param dt the LocalDateTime to format
     * @return the formatted date/time string
     */
    public static String format(LocalDateTime dt) {
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dt.toLocalDate().format(OUTPUT_DATE);
        }
        return dt.format(OUTPUT_DATE_TIME);
    }

    /**
     * Parses a date/time string from the storage file.
     * Accepts ISO format: yyyy-MM-ddTHH:MM or yyyy-MM-dd.
     *
     * @param raw the raw date/time string from storage
     * @return the parsed LocalDateTime
     * @throws LeoException if the format is corrupted
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

    /**
     * Converts a LocalDateTime to its ISO string representation for storage.
     *
     * @param dt the LocalDateTime to convert
     * @return the ISO format string (e.g., "2019-12-02T18:00")
     */
    public static String toStoredString(LocalDateTime dt) {
        return dt.toString(); // ISO_LOCAL_DATE_TIME, e.g. 2019-12-02T18:00
    }
}
