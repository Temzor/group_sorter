package io.sorter.serviceimpl;

import io.sorter.service.GroupBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupBuilderImplTest {

    private final GroupBuilder groupBuilder = new GroupBuilderImpl();

    @Test
    @DisplayName("Тест построения групп с уникальными строками без общих значений")
    public void testBuildGroupsWithUniqueValues() {
        List<String[]> uniqueLines = Arrays.asList(
                new String[]{"John", "Doe", "30"},
                new String[]{"Jane", "Smith", "25"},
                new String[]{"Bob", "Johnson", "40"}
        );

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertEquals(3, groups.size(), "Должно быть три группы для уникальных строк");

        for (Set<String> group : groups.values()) {
            assertEquals(1, group.size(), "Каждая группа должна содержать одну строку");
        }
    }

    @Test
    @DisplayName("Тест построения групп с общими значениями в одной колонке")
    public void testBuildGroupsWithCommonValuesInOneColumn() {
        List<String[]> uniqueLines = Arrays.asList(
                new String[]{"John", "Doe", "30"},
                new String[]{"Jane", "Doe", "25"},
                new String[]{"Bob", "Johnson", "40"}
        );

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertEquals(2, groups.size(), "Должно быть две группы");

        boolean foundGroupWithDoe = false;
        for (Set<String> group : groups.values()) {
            if (group.stream().anyMatch(line -> line.contains("Doe"))) {
                foundGroupWithDoe = true;
                assertEquals(2, group.size(), "Группа с фамилией 'Doe' должна содержать две строки");
            } else {
                assertEquals(1, group.size(), "Другая группа должна содержать одну строку");
            }
        }
        assertTrue(foundGroupWithDoe, "Должна быть группа с общим значением 'Doe'");
    }

    @Test
    @DisplayName("Тест построения групп с общими значениями в разных колонках")
    public void testBuildGroupsWithCommonValuesInDifferentColumns() {
        List<String[]> uniqueLines = Arrays.asList(
                new String[]{"John", "Doe", "30"},
                new String[]{"Jane", "Smith", "30"},
                new String[]{"Bob", "Doe", "40"},
                new String[]{"Alice", "Johnson", "25"}
        );

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertEquals(2, groups.size(), "Должно быть две группы");

        boolean foundGroupWithCommonValues = false;
        for (Set<String> group : groups.values()) {
            if (group.size() == 3) {
                foundGroupWithCommonValues = true;
                assertTrue(group.stream().anyMatch(line -> line.contains("John;Doe;30")));
                assertTrue(group.stream().anyMatch(line -> line.contains("Jane;Smith;30")));
                assertTrue(group.stream().anyMatch(line -> line.contains("Bob;Doe;40")));
            } else {
                assertEquals(1, group.size(), "Другая группа должна содержать одну строку");
            }
        }
        assertTrue(foundGroupWithCommonValues, "Должна быть группа с общими значениями в разных колонках");
    }

    @Test
    @DisplayName("Тест построения групп с пустыми значениями")

    public void testBuildGroupsWithEmptyValues() {
        List<String[]> uniqueLines = Arrays.asList(
                new String[]{"John", "", "30"},
                new String[]{"", "Smith", "30"},
                new String[]{"Bob", "", "40"},
                new String[]{"Alice", "Johnson", ""}
        );

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertEquals(3, groups.size(), "Должно быть три группы");

        boolean foundGroupWithAge30 = false;
        for (Set<String> group : groups.values()) {
            if (group.stream().anyMatch(line -> line.contains(";30"))) {
                foundGroupWithAge30 = true;
                assertEquals(2, group.size(), "Группа с возрастом '30' должна содержать две строки");
            }
        }
        assertTrue(foundGroupWithAge30, "Должна быть группа с общим значением '30'");
    }

    @Test
    @DisplayName("Тест построения групп с повторяющимися строками")
    public void testBuildGroupsWithDuplicateLines() {
        List<String[]> uniqueLines = Arrays.asList(
                new String[]{"John", "Doe", "30"},
                new String[]{"John", "Doe", "30"},
                new String[]{"Bob", "Smith", "40"}
        );

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertEquals(2, groups.size(), "Должно быть две группы");

        for (Set<String> group : groups.values()) {
            if (group.stream().anyMatch(line -> line.contains("John;Doe;30"))) {
                assertEquals(1, group.size(), "Дубликаты не должны создавать дополнительные группы");
            }
        }
    }

    @Test
    @DisplayName("Тест построения групп с пустым списком строк")
    public void testBuildGroupsWithEmptyList() {
        List<String[]> uniqueLines = new ArrayList<>();

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);
        assertEquals(0, groups.size(), "Должно быть ноль групп для пустого списка");
    }


    @Test
    @DisplayName("Тест построения групп с большими данными")
    public void testBuildGroupsWithLargeDataSet() {
        List<String[]> uniqueLines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            uniqueLines.add(new String[]{"User" + i, "Group" + (i % 10), String.valueOf(20 + (i % 5))});
        }

        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(uniqueLines);

        assertTrue(groups.size() <= 50, "Количество групп не должно превышать 50");
    }
}