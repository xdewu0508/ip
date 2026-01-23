import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateTimeUtil {
    private static final List<DateTimeFormatter> INPUT_FORMATS = List.of(
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
    );

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    private static final DateTimeFormatter STORAGE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime parse(String input) throws DateTimeParseException {
        for (DateTimeFormatter fmt : INPUT_FORMATS) {
            try {
                return LocalDateTime.parse(input, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        throw new DateTimeParseException("Invalid date format", input, 0);
    }

    public static String formatForUser(LocalDateTime dt) {
        return dt.format(OUTPUT_FORMAT);
    }

    public static String formatForStorage(LocalDateTime dt) {
        return dt.format(STORAGE_FORMAT);
    }

    public static LocalDateTime parseFromStorage(String text) {
        return LocalDateTime.parse(text, STORAGE_FORMAT);
    }
}
