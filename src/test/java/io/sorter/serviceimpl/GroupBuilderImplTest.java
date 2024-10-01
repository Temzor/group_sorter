package io.sorter.serviceimpl;

import io.sorter.service.GroupBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupBuilderImplTest {
    private GroupBuilder groupBuilder;

    @BeforeEach
    @DisplayName("Инициализация структуры перед каждым тестом")
    public void setUp() {
        groupBuilder = new GroupBuilderImpl();
        groupBuilder.initialize();
    }

    @Test
    @DisplayName("Тест процесса обработки строк и формирования групп")
    public void testProcessLineAndGrouping() {
        GroupBuilderImpl groupBuilder = new GroupBuilderImpl();
        groupBuilder.initialize();

        /* Строки для обработки */
        String[] line1 = {"John", "Doe", "john@example.com"};
        String[] line2 = {"Jane", "Doe", "jane@example.com"};
        String[] line3 = {"John", "Smith", "johnsmith@example.com"};
        String[] line4 = {"Alice", "Green", "alice@example.com"};

        groupBuilder.processLine(1, line1);
        groupBuilder.processLine(2, line2);
        groupBuilder.processLine(3, line3);
        groupBuilder.processLine(4, line4);

        Map<Integer, Set<String>> groups = groupBuilder.getGroups();

        /* Ожидаем как минимум две группы */
        int groupCount = groups.size();
        assertTrue(groupCount >= 2);

        /* Проверяем, что John и Jane Doe в одной группе */
        boolean johnDoeGroupFound = false;
        for (Set<String> group : groups.values()) {
            if (group.contains("John;Doe;john@example.com") && group.contains("Jane;Doe;jane@example.com")) {
                johnDoeGroupFound = true;
                break;
            }
        }
        assertTrue(johnDoeGroupFound);

        /* Проверяем, что Alice в отдельной группе */
        boolean aliceGroupFound = false;
        for (Set<String> group : groups.values()) {
            if (group.contains("Alice;Green;alice@example.com") && group.size() == 1) {
                aliceGroupFound = true;
                break;
            }
        }
        assertTrue(aliceGroupFound);
    }

    @Test
    @DisplayName("Тест получения групп после обработки строк")
    public void testGetGroups() {
        String[] line1 = {"value1", "value2"};
        String[] line2 = {"value3", "value4"};
        groupBuilder.processLine(0, line1);
        groupBuilder.processLine(1, line2);

        Map<Integer, Set<String>> expectedGroups = new HashMap<>();
        Set<String> group1 = new HashSet<>();
        group1.add("value1;value2");
        expectedGroups.put(0, group1);

        Set<String> group2 = new HashSet<>();
        group2.add("value3;value4");
        expectedGroups.put(1, group2);

        assertEquals(expectedGroups, groupBuilder.getGroups(), "Группы должны соответствовать обработанным строкам");
    }
}