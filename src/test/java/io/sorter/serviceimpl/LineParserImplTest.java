package io.sorter.serviceimpl;

import io.sorter.processor.LineProcessor;
import io.sorter.service.LineParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LineParserImplTest {

    private final LineParser lineParser = new LineParserImpl();

    @Test
    @DisplayName("Тест парсинга корректных строк")
    public void testParseValidLines() {
        List<String> lines = Arrays.asList(
                "Женя;Петров;30",
                "Юля;Иванова;25",
                "Женя;Петров;30"
        );

        List<String[]> parsedLines = lineParser.parseLines(lines);
        assertEquals(2, parsedLines.size());

        String[] firstLine = parsedLines.get(0);
        assertArrayEquals(new String[]{"Женя", "Петров", "30"}, firstLine);
    }

    @Test
    @DisplayName("Тест парсинга некорректной строки")
    public void testParseInvalidLine() {
        List<String> lines = Collections.singletonList(
                "Некорректная строка с незакрытой кавычкой \""
        );

        List<String[]> parsedLines = lineParser.parseLines(lines);
        assertEquals(0, parsedLines.size());
    }
    @Test
    @DisplayName("Тест парсинга c генератором строк")
    public void testProcess() throws IOException {
        File tempFile = File.createTempFile("test", ".gz");
        int groupCount = getGroupCount(tempFile);
        assertFalse(groupCount > 0);
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String firstLine = reader.readLine();
            assertTrue(firstLine.contains("Количество групп с более чем одним элементом: "));
        }
    }

    private static int getGroupCount(File tempFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(gzos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write("Женя;Петров;30\n");
            bw.write("Женя;Петров;30\n");
            bw.write("Юля;Иванова;25\n");
            bw.write("Юля;Иванова;25\n");
        }

        LineProcessor lineProcessor = new LineProcessor();
        lineProcessor.process(tempFile.getAbsolutePath());

        return lineProcessor.getGroupCount();
    }
}