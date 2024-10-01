package io.sorter.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LineProcessorTest {
    private LineProcessor lineProcessor;

    @BeforeEach
    public void setUp() {
        lineProcessor = new LineProcessor();
    }

    @Test
    @DisplayName("Тест полного процесса обработки файла")
    public void testProcess() throws IOException {
        /* Создаем временный входной файл */
        Path inputFile = Files.createTempFile("testInput", ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(inputFile)) {
            writer.write("John;Doe;john@example.com\n");
            writer.write("Jane;Doe;jane@example.com\n");
            writer.write("John;Smith;johnsmith@example.com\n");
            writer.write("Alice;Green;alice@example.com\n");
        }

        /* Создаем экземпляр LineProcessor и обрабатываем файл */
        LineProcessor lineProcessor = new LineProcessor();
        lineProcessor.process(inputFile.toString());

        /* Проверяем количество групп с более чем одним элементом */
        int groupCount = lineProcessor.getGroupCount();
        assertEquals(1, groupCount); // Ожидаем одну группу с более чем одним элементом

        /* Проверяем содержимое выходного файла */
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            boolean groupHeaderFound = false;
            int groupLineCount = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Группа")) {
                    groupHeaderFound = true;
                } else if (groupHeaderFound && !line.trim().isEmpty()) {
                    groupLineCount++;
                }
            }

            /* Ожидаем, что в группе будет как минимум 2 строки */
            assertTrue(groupLineCount >= 2);
        }

        /* Удаляем временные файлы */
        Files.deleteIfExists(inputFile);
    }

    @Test
    @DisplayName("Проверка установки начального количества групп в 0")
    public void testInitialGroupCount() {
        assertEquals(0, lineProcessor.getGroupCount());
    }

    @Test
    @DisplayName("Проверка IOException при обработке несуществующего файла")
    public void testProcessFileNotFound() {
        assertThrows(IOException.class, () -> lineProcessor.process("non_existent_file.txt"));
    }
}