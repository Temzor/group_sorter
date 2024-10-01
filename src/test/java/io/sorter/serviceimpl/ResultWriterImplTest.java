package io.sorter.serviceimpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ResultWriterImplTest {

    @Test
    @DisplayName("Тест записи результатов")
    public void testWriteResult() throws IOException {
        /* Создаем пример данных групп */
        Map<Integer, Set<String>> groups = new HashMap<>();

        Set<String> group1 = new HashSet<>();
        group1.add("John;Doe;john@example.com");
        group1.add("Jane;Doe;jane@example.com");

        Set<String> group2 = new HashSet<>();
        group2.add("Alice;Green;alice@example.com");

        groups.put(1, group1);
        groups.put(2, group2);

        /* Переопределяем метод writeResult для записи в StringWriter */
        class TestResultWriter extends ResultWriterImpl {
            private final StringWriter stringWriter = new StringWriter();

            @Override
            public int writeResult(Map<Integer, Set<String>> groups) throws IOException {
                List<Set<String>> groupList = filterAndSortGroups(groups);

                try (BufferedWriter writer = new BufferedWriter(stringWriter)) {
                    writer.write("Количество групп с более чем одним элементом: " + groupList.size());
                    writer.newLine();
                    writer.newLine();

                    int groupNumber = 1;
                    for (Set<String> group : groupList) {
                        writer.write("Группа " + groupNumber);
                        writer.newLine();
                        for (String line : group) {
                            writer.write(line);
                            writer.newLine();
                        }
                        writer.newLine();
                        groupNumber++;

                    }
                }
                return groupList.size();
            }

            public String getResult() {
                return stringWriter.toString();
            }
        }

        TestResultWriter testWriter = new TestResultWriter();
        int groupCount = testWriter.writeResult(groups);

        /* Ожидаем, что будет только одна группа с более чем одним элементом */
        assertEquals(1, groupCount);

        String output = testWriter.getResult();

        /* Проверяем, что выходные данные содержат ожидаемую информацию */
        assertTrue(output.contains("Группа 1"));
        assertTrue(output.contains("John;Doe;john@example.com"));
        assertTrue(output.contains("Jane;Doe;jane@example.com"));
        assertFalse(output.contains("Alice;Green;alice@example.com"));
    }

    @Test
    @DisplayName("Тест: Проверка записи результата в файл")
    void testWriteResultIO() throws IOException {
        ResultWriterImpl resultWriter = new ResultWriterImpl();
        Map<Integer, Set<String>> groups = new HashMap<>();
        groups.put(1, new HashSet<>(Arrays.asList("apple", "banana")));
        groups.put(2, new HashSet<>(Collections.singletonList("orange")));
        groups.put(3, new HashSet<>(Arrays.asList("mango", "peach")));

        int result = resultWriter.writeResult(groups);

        assertEquals(2, result, "Должно быть записано 2 группы с более чем одним элементом.");

        /* Проверка содержимого файла (пример проверки, если требуется) */
        List<String> lines = Files.readAllLines(Path.of("output.txt"));
        assertTrue(lines.contains("Группа 1"), "Файл должен содержать запись для Группы 1");
        assertTrue(lines.contains("Группа 2"), "Файл должен содержать запись для Группы 2");
    }

    @ParameterizedTest(name = "Тест фильтрации и сортировки: {index}")
    @MethodSource("provideGroupsForFilterAndSort")
    @DisplayName("Параметризованный тест: Фильтрация и сортировка групп")
    void testFilterAndSortGroups(Map<Integer, Set<String>> groups, List<Set<String>> expected) {
        ResultWriterImpl resultWriter = new ResultWriterImpl();
        List<Set<String>> result = resultWriter.filterAndSortGroups(groups);
        assertEquals(expected, result, "Результаты фильтрации и сортировки должны совпадать с ожидаемыми.");
    }

    private static List<Arguments> provideGroupsForFilterAndSort() {
        return Arrays.asList(
                Arguments.of(Map.of(
                        1, new HashSet<>(Arrays.asList("a", "b")),
                        2, new HashSet<>(Collections.singletonList("c")),
                        3, new HashSet<>(Arrays.asList("d", "e", "f"))
                ), List.of(
                        new HashSet<>(Arrays.asList("d", "e", "f")),
                        new HashSet<>(Arrays.asList("a", "b"))
                )),
                Arguments.of(Map.of(
                        1, new HashSet<>(Collections.singleton("x")),
                        2, new HashSet<>(Collections.singleton("y"))
                ), Collections.emptyList())
        );
    }
}