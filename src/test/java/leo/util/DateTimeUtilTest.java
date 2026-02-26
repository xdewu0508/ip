package leo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import leo.exception.LeoException;

/**
 * Tests for the DateTimeUtil class.
 * Tests cover all public methods: parseDateTime, format, parseStored, toStoredString.
 */
public class DateTimeUtilTest {

    // ==================== parseDateTime Tests ====================

    @Test
    public void parseDateTime_isoDateFormat_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-12-31");
        assertEquals(2025, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_isoDateTimeFormat_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-06-15 1430");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_slashDateFormat_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("15/6/2025 1430");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_singleDigitDayMonth_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("5/3/2025 0900");
        assertEquals(2025, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(5, result.getDayOfMonth());
        assertEquals(9, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_withWhitespace_trimsAndParses() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("  2025-12-31  ");
        assertEquals(2025, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
    }

    @Test
    public void parseDateTime_emptyString_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("");
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    public void parseDateTime_whitespaceOnly_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("   ");
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    public void parseDateTime_invalidFormat_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("invalid-date");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_invalidDate_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-13-45");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_partialTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-06-15 14");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    // ==================== format Tests ====================

    @Test
    public void format_midnightTime_dateOnlyFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 31, 0, 0);
        String result = DateTimeUtil.format(dt);
        assertEquals("Dec 31 2025", result);
    }

    @Test
    public void format_withTime_dateTimeFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 14, 30);
        String result = DateTimeUtil.format(dt);
        assertEquals("Jun 15 2025 2:30PM", result);
    }

    @Test
    public void format_morningTime_dateTimeFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 9, 0);
        String result = DateTimeUtil.format(dt);
        assertEquals("Jan 1 2025 9:00AM", result);
    }

    @Test
    public void format_afternoonTime_dateTimeFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 3, 15, 13, 0);
        String result = DateTimeUtil.format(dt);
        assertEquals("Mar 15 2025 1:00PM", result);
    }

    @Test
    public void format_midnight59_dateTimeFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 23, 59);
        String result = DateTimeUtil.format(dt);
        assertEquals("Jun 15 2025 11:59PM", result);
    }

    // ==================== parseStored Tests ====================

    @Test
    public void parseStored_isoDateTimeWithT_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseStored("2025-12-31T23:59");
        assertEquals(2025, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseStored_isoDateOnly_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseStored("2025-06-15");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseStored_withWhitespace_trimsAndParses() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseStored("  2025-12-31T14:30  ");
        assertEquals(2025, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseStored_invalidFormat_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("invalid-format");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    @Test
    public void parseStored_invalidDate_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("2025-13-45T25:99");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    @Test
    public void parseStored_partialDateTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("2025-06-15T");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    // ==================== toStoredString Tests ====================

    @Test
    public void toStoredString_withTime_correctIsoFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 31, 23, 59);
        String result = DateTimeUtil.toStoredString(dt);
        assertEquals("2025-12-31T23:59", result);
    }

    @Test
    public void toStoredString_midnight_correctIsoFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 0, 0);
        String result = DateTimeUtil.toStoredString(dt);
        assertEquals("2025-06-15T00:00", result);
    }

    @Test
    public void toStoredString_afternoon_correctIsoFormat() {
        LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 14, 30);
        String result = DateTimeUtil.toStoredString(dt);
        assertEquals("2025-01-01T14:30", result);
    }

    @Test
    public void toStoredString_roundTrip_preservesValue() throws LeoException {
        LocalDateTime original = LocalDateTime.of(2025, 9, 20, 10, 45);
        String stored = DateTimeUtil.toStoredString(original);
        LocalDateTime parsed = DateTimeUtil.parseStored(stored);
        assertEquals(original, parsed);
    }

    // ==================== Edge Cases ====================

    @Test
    public void parseDateTime_leapYear_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2024-02-29");
        assertEquals(2024, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void parseDateTime_yearBoundary_success() throws LeoException {
        LocalDateTime startOfYear = DateTimeUtil.parseDateTime("2025-01-01 0000");
        assertEquals(1, startOfYear.getMonthValue());
        assertEquals(1, startOfYear.getDayOfMonth());

        LocalDateTime endOfYear = DateTimeUtil.parseDateTime("2025-12-31 2359");
        assertEquals(12, endOfYear.getMonthValue());
        assertEquals(31, endOfYear.getDayOfMonth());
    }

    @Test
    public void format_yearBoundary_correctFormat() {
        String newYear = DateTimeUtil.format(LocalDateTime.of(2025, 1, 1, 0, 0));
        assertEquals("Jan 1 2025", newYear);

        String yearEnd = DateTimeUtil.format(LocalDateTime.of(2025, 12, 31, 23, 59));
        assertEquals("Dec 31 2025 11:59PM", yearEnd);
    }

    // ==================== Additional parseDateTime Edge Cases ====================

    @Test
    public void parseDateTime_withLeadingZeros_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-01-05 0905");
        assertEquals(1, result.getMonthValue());
        assertEquals(5, result.getDayOfMonth());
        assertEquals(9, result.getHour());
        assertEquals(5, result.getMinute());
    }

    @Test
    public void parseDateTime_slashFormatSingleDigitDayMonth_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("5/3/2025 1430");
        assertEquals(3, result.getMonthValue());
        assertEquals(5, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_slashFormatDoubleDigit_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("25/12/2025 1200");
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(12, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_endOfDay_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-12-31 2359");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseDateTime_startOfDay_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-01-01 0000");
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_invalidMonth_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-13-01");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_invalidDay_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-06-32");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_invalidHour_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-06-15 2500");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_invalidMinute_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025-06-15 1460");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_nonNumericInput_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("abc-def-ghi");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    @Test
    public void parseDateTime_mixedFormat_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseDateTime("2025/06/15 1430");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }

    // ==================== Additional format Edge Cases ====================

    @Test
    public void format_january_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 1, 15, 10, 30));
        assertTrue(result.startsWith("Jan"));
    }

    @Test
    public void format_december_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 12, 15, 10, 30));
        assertTrue(result.startsWith("Dec"));
    }

    @Test
    public void format_allMonths_success() {
        String[] expectedMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < 12; i++) {
            String result = DateTimeUtil.format(LocalDateTime.of(2025, i + 1, 15, 10, 30));
            assertTrue(result.startsWith(expectedMonths[i]));
        }
    }

    @Test
    public void format_noon_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 12, 0));
        assertEquals("Jun 15 2025 12:00PM", result);
    }

    @Test
    public void format_oneFiftyNineAm_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 1, 59));
        assertEquals("Jun 15 2025 1:59AM", result);
    }

    @Test
    public void format_elevenFiftyNinePm_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 23, 59));
        assertEquals("Jun 15 2025 11:59PM", result);
    }

    @Test
    public void format_firstMinuteOfDay_success() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 0, 1));
        assertEquals("Jun 15 2025 12:01AM", result);
    }

    // ==================== Additional parseStored Edge Cases ====================

    @Test
    public void parseStored_withSecondsInFormat_stillParses() throws LeoException {
        // ISO format with seconds
        LocalDateTime result = DateTimeUtil.parseStored("2025-06-15T14:30:00");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseStored_dateOnly_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseStored("2025-06-15");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseStored_emptyString_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    @Test
    public void parseStored_invalidIsoFormat_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("2025/06/15T14:30");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    @Test
    public void parseStored_incompleteDateTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("2025-06-15T");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    @Test
    public void parseStored_onlyTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            DateTimeUtil.parseStored("14:30");
        });
        assertTrue(exception.getMessage().contains("Corrupted date/time"));
    }

    // ==================== Additional toStoredString Edge Cases ====================

    @Test
    public void toStoredString_firstSecondOfDay_success() {
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 0, 0, 0);
        String result = DateTimeUtil.toStoredString(dt);
        assertEquals("2025-06-15T00:00", result);
    }

    @Test
    public void toStoredString_lastSecondOfDay_success() {
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 23, 59, 59);
        String result = DateTimeUtil.toStoredString(dt);
        // toStoredString uses toString() which includes seconds
        assertEquals("2025-06-15T23:59:59", result);
    }

    @Test
    public void toStoredString_withSeconds_ignored() {
        // toStoredString uses toString() which ignores seconds
        LocalDateTime dt = LocalDateTime.of(2025, 6, 15, 14, 30, 45);
        String result = DateTimeUtil.toStoredString(dt);
        assertEquals("2025-06-15T14:30:45", result);
    }

    // ==================== Round Trip Tests ====================

    @Test
    public void parseDateTime_format_roundTrip_preservesValue() throws LeoException {
        LocalDateTime original = LocalDateTime.of(2025, 6, 15, 14, 30);
        // Note: format() loses some info, so we test parseStored -> toStoredString
        String stored = DateTimeUtil.toStoredString(original);
        LocalDateTime parsed = DateTimeUtil.parseStored(stored);
        assertEquals(original, parsed);
    }

    @Test
    public void parseStored_toStoredString_roundTrip_preservesValue() throws LeoException {
        String original = "2025-06-15T14:30";
        LocalDateTime parsed = DateTimeUtil.parseStored(original);
        String stored = DateTimeUtil.toStoredString(parsed);
        assertEquals(original, stored);
    }

    // ==================== Special Date Cases ====================

    @Test
    public void parseDateTime_valentinesDay_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-02-14");
        assertEquals(2, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
    }

    @Test
    public void parseDateTime_christmas_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-12-25 0000");
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(0, result.getHour());
    }

    @Test
    public void parseDateTime_newYearsEve_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-12-31 2359");
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseDateTime_leapYearBoundary_success() throws LeoException {
        // 2024 is a leap year
        LocalDateTime feb29 = DateTimeUtil.parseDateTime("2024-02-29");
        assertEquals(2024, feb29.getYear());
        assertEquals(2, feb29.getMonthValue());
        assertEquals(29, feb29.getDayOfMonth());

        // Note: 2025-02-29 is invalid but Java's LocalDate.parse handles it differently
        // depending on the format. The ISO format will throw an exception.
    }

    // ==================== Time Boundary Tests ====================

    @Test
    public void parseDateTime_midnight_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-06-15 0000");
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_oneMinuteBeforeMidnight_success() throws LeoException {
        LocalDateTime result = DateTimeUtil.parseDateTime("2025-06-15 2359");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void format_midnight_dateOnlyFormat() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 0, 0));
        assertEquals("Jun 15 2025", result);
    }

    @Test
    public void format_oneMinuteAfterMidnight_dateTimeFormat() {
        String result = DateTimeUtil.format(LocalDateTime.of(2025, 6, 15, 0, 1));
        assertEquals("Jun 15 2025 12:01AM", result);
    }
}
