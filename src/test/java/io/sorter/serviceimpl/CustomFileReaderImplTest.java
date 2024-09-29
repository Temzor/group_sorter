package io.sorter.serviceimpl;

import io.sorter.service.CustomFileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class CustomFileReaderImplTest {

    private CustomFileReader customFileReader;
    private static final String TEST_FILE_DIR = "test_files";

    @BeforeEach
    public void setUp() throws IOException {
        customFileReader = new CustomFileReaderImpl();
        Files.createDirectories(new File(TEST_FILE_DIR).toPath());
    }

    @AfterEach
    public void tearDown() {
        File dir = new File(TEST_FILE_DIR);
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (!file.delete()) {
                    System.err.println("Не удалось удалить файл: " + file.getAbsolutePath());
                }
            }
            if (!dir.delete()) {
                System.err.println("Не удалось удалить директорию: " + dir.getAbsolutePath());
            }
        }
    }

    @Test
    @DisplayName("Тест чтения из корректного GZIP-файла")
    public void testReadLinesFromValidGzipFile() throws IOException {
        String filename = TEST_FILE_DIR + "/valid.gz";
        List<String> expectedLines = Arrays.asList("Строка 1", "Строка 2", "Строка 3");

        createGzipFile(filename, expectedLines);

        List<String> actualLines = customFileReader.readLines(filename);

        assertEquals(expectedLines, actualLines, "Считанные строки должны соответствовать ожидаемым");
    }

    @Test
    @DisplayName("Тест чтения из пустого GZIP-файла")
    public void testReadLinesFromEmptyGzipFile() throws IOException {
        String filename = TEST_FILE_DIR + "/empty.gz";

        createGzipFile(filename, Collections.emptyList());

        List<String> actualLines = customFileReader.readLines(filename);

        assertTrue(actualLines.isEmpty(), "Список строк должен быть пуст");
    }

    @Test
    @DisplayName("Тест обработки отсутствующего файла")
    public void testReadLinesFromNonExistingFile() {
        String filename = TEST_FILE_DIR + "/nonexistent.gz";

        assertThrows(FileNotFoundException.class, () -> customFileReader.readLines(filename), "Должно выбрасываться исключение FileNotFoundException при отсутствии файла");
    }

    @Test
    @DisplayName("Тест чтения из некорректного GZIP-файла")
    public void testReadLinesFromInvalidGzipFile() throws IOException {
        String filename = TEST_FILE_DIR + "/invalid.gz";

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write("Некорректные данные".getBytes(StandardCharsets.UTF_8));
        }

        assertThrows(IOException.class, () -> customFileReader.readLines(filename),
                "Должно выбрасываться исключение IOException при чтении из некорректного GZIP-файла");
    }

    @Test
    @DisplayName("Тест чтения из файла без GZIP-компрессии")
    public void testReadLinesFromNonGzipFile() throws IOException {
        String filename = TEST_FILE_DIR + "/plain.txt";
        List<String> lines = Arrays.asList("Строка 1", "Строка 2");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        assertThrows(IOException.class, () -> customFileReader.readLines(filename),
                "Должно выбрасываться исключение IOException при чтении из файла без GZIP-компрессии");
    }

    @Test
    @DisplayName("Тест чтения из GZIP-файла с большим количеством строк")
    public void testReadLinesFromLargeGzipFile() throws IOException {
        String filename = TEST_FILE_DIR + "/large.gz";
        List<String> expectedLines = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            expectedLines.add("Строка номер " + i);
        }

        createGzipFile(filename, expectedLines);

        List<String> actualLines = customFileReader.readLines(filename);

        assertEquals(expectedLines.size(), actualLines.size(), "Количество считанных строк должно соответствовать ожидаемому");
        assertEquals(expectedLines, actualLines, "Считанные строки должны соответствовать ожидаемым");
    }

    private void createGzipFile(String filename, List<String> lines) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             GZIPOutputStream gzipOut = new GZIPOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(gzipOut, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}