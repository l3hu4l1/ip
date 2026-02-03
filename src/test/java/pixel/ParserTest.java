package pixel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import pixel.exception.PixelException;
import pixel.parser.Parser;

public class ParserTest {

    @Test
    public void parseDateTime_withDateAndTime_success() throws PixelException {
        LocalDateTime result = Parser.parseDateTime("2026-01-15 1430");
        assertEquals(2026, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_withDateOnly_defaultsToEndOfDay() throws PixelException {
        LocalDateTime result = Parser.parseDateTime("2026-12-25");
        assertEquals(2026, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseDateTime_withMidnightTime_success() throws PixelException {
        LocalDateTime result = Parser.parseDateTime("2026-01-01 0000");
        assertEquals(2026, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_withEndOfDayTime_success() throws PixelException {
        LocalDateTime result = Parser.parseDateTime("2026-06-30 2359");
        assertEquals(2026, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(30, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseDateTime_withInvalidDateFormat_throwsException() {
        PixelException exception = assertThrows(PixelException.class, () -> {
            Parser.parseDateTime("2026/01/15");
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    public void parseDateTime_withInvalidTimeFormat_throwsException() {
        PixelException exception = assertThrows(PixelException.class, () -> {
            Parser.parseDateTime("2026-01-15 25:00");
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    public void parseDateTime_withTextInput_throwsException() {
        PixelException exception = assertThrows(PixelException.class, () -> {
            Parser.parseDateTime("tomorrow");
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    public void parseDateTime_withEmptyString_throwsException() {
        assertThrows(Exception.class, () -> {
            Parser.parseDateTime("");
        });
    }

    @Test
    public void parseDateTime_withLeapYearDate_success() throws PixelException {
        LocalDateTime result = Parser.parseDateTime("2024-02-29 1200");
        assertEquals(2024, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void parseDateTime_withExtraSpaces_throwsException() {
        assertThrows(Exception.class, () -> {
            Parser.parseDateTime("2026-01-15  1430");
        });
    }

    @Test
    public void parseDateTime_withInvalidMonth_throwsException() {
        assertThrows(Exception.class, () -> {
            Parser.parseDateTime("2026-13-01 1200");
        });
    }
}
