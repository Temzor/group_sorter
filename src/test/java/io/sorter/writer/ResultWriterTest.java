package io.sorter.writer;

import io.sorter.validators.UnionFind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultWriterTest {
    private static final String OUTPUT_FILENAME = "test_output.txt";
    private ResultWriter resultWriter;

    @BeforeEach
    void setUp() {
        resultWriter = new ResultWriter(OUTPUT_FILENAME);
    }

    @Test
    @DisplayName("**Проверка записи результатов в файл**")
    void testWriteResults() throws IOException {
        List<String> lines = List.of("Строка 1", "Строка 2", "Строка 3", "Строка 4");
        UnionFind unionFind = new UnionFind(lines.size());

        /* Объединяем строки "Строка 1" и "Строка 2" в одну группу */
        unionFind.union(0, 1);

        /* Объединяем строки "Строка 3" и "Строка 4" в другую группу */
        unionFind.union(2, 3);

        resultWriter.writeResults(lines, unionFind);

        /* Читаем содержимое файла */
        List<String> writtenLines = Files.readAllLines(Paths.get(OUTPUT_FILENAME));

        /* Конвертируем List<String> в строку с системными переносами строк */
        String writtenContent = String.join(System.lineSeparator(), writtenLines);

        String expectedContent = String.join(System.lineSeparator(),
                "Количество групп с более чем одним элементом: 2",
                "",
                "Группа 1",
                "Строка 1",
                "Строка 2",
                "",
                "Группа 2",
                "Строка 3",
                "Строка 4",
                "");

        /* Проверяем содержимое записанного файла */
        assertEquals(expectedContent, writtenContent);
    }

    @Test
    @DisplayName("**Проверка возвращаемого количества групп**")
    void testGetGroupCount() throws IOException {
        List<String> lines = List.of("Строка 1", "Строка 2");
        UnionFind unionFind = new UnionFind(lines.size());

        /* Объединяем строки "Строка 1" и "Строка 2" в одну группу */
        unionFind.union(0, 1);

        resultWriter.writeResults(lines, unionFind);

        assertEquals(1, resultWriter.getGroupCount());
    }
}