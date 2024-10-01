package io.sorter.serviceimpl;

import io.sorter.service.LineParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class LineParserImplTest {
    private LineParser lineParser;

    @BeforeEach
    @DisplayName("Создание экземпляра LineParserImpl перед каждым тестом")
    public void setUp() {
        lineParser = new LineParserImpl();
    }

    @Test
    @DisplayName("Тест разбора простой строки")
    public void testParseSimpleLine() {
        LineParser parser = new LineParserImpl();
        String line = "value1;value2;value3";
        String[] result = parser.parseLine(line);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    @Test
    @DisplayName("Тест разбора строки с пустыми значениями")
    public void testParseLineWithEmptyValues() {
        LineParser parser = new LineParserImpl();
        String line = "value1;;value3;";
        String[] result = parser.parseLine(line);
        assertArrayEquals(new String[]{"value1", "", "value3", ""}, result);
    }

    @Test
    @DisplayName("Тест разбора строки со специальными символами")
    public void testParseLineWithSpecialCharacters() {
        LineParser parser = new LineParserImpl();
        String line = "value1;val;ue2;value3";
        String[] result = parser.parseLine(line);
        assertArrayEquals(new String[]{"value1", "val", "ue2", "value3"}, result);
    }

    @Test
    @DisplayName("Тест обработки пустой строки")
    public void testParseEmptyLine() {
        String[] expected = {""};
        String actualLine = "";
        assertArrayEquals(expected, lineParser.parseLine(actualLine), "Пустая строка должна вернуть массив с одним пустым элементом");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "value1;value2;value3",
            "value1;;value3",
            ";value2;",
            ";;",
    })
    @DisplayName("Тест обработки строки с различными разделителями")
    public void testParseLineWithVariousDelimiters(String line) {
        String[] expected = line.split(";", -1);
        assertArrayEquals(expected, lineParser.parseLine(line), "Строка: " + line + " должна быть правильно разбита по разделителю ';'");
    }

    @Test
    @DisplayName("Тест обработки строки без разделителей")
    public void testParseLineWithoutDelimiters() {
        String[] expected = {"value"};
        String actualLine = "value";
        assertArrayEquals(expected, lineParser.parseLine(actualLine), "Строка без разделителей должна вернуть массив с одним элементом");
    }
}