package io.sorter.serviceimpl;

import io.sorter.mapper.DisjointSet;
import io.sorter.service.GroupBuilder;

import java.util.*;

/**
 * Класс GroupBuilderImpl реализует интерфейс GroupBuilder и отвечает за
 * создание групп строк на основе объединяющих критериев.
 * Использует структуру данных Disjoint Set для определения связей между строками.
 */
public class GroupBuilderImpl implements GroupBuilder {

    /**
     * Строит группы строк на основе представленных данных, где каждая группа
     * состоит из строк, имеющих общие значения в определенных столбцах.
     *
     * @param uniqueLines список массивов строк, где каждая строка представлена как массив
     * @return карта, в которой ключи - идентификаторы групп, а значения - множества строк, относящихся к этим группам
     */
    @Override
    public Map<Integer, Set<String>> buildGroups(List<String[]> uniqueLines) {
        DisjointSet disjointSet = new DisjointSet(uniqueLines.size());
        Map<String, Integer> valueToIndex = new HashMap<>();
        Map<Integer, String> lineIndexToLine = new HashMap<>();

        for (int i = 0; i < uniqueLines.size(); i++) {
            String[] values = uniqueLines.get(i);
            lineIndexToLine.put(i, String.join(";", values));

            for (int colIndex = 0; colIndex < values.length; colIndex++) {
                String value = values[colIndex].trim();

                if (!value.isEmpty()) {
                    String key = colIndex + "|" + value;

                    if (valueToIndex.containsKey(key)) {
                        int existingIndex = valueToIndex.get(key);
                        disjointSet.union(i, existingIndex);
                    } else {
                        valueToIndex.put(key, i);
                    }
                }
            }
        }

        return collectGroups(uniqueLines.size(), disjointSet, lineIndexToLine);
    }

    /**
     * Собирает группы строк на основе данных, обработанных DisjointSet.
     *
     * @param size            общее количество строк
     * @param disjointSet     структура данных, используемая для хранения связей
     * @param lineIndexToLine карта, сопоставляющая индекс строки с ее строковым представлением
     * @return карта групп, где ключи - идентификаторы групп, а значения - множества строк
     */
    private Map<Integer, Set<String>> collectGroups(int size, DisjointSet disjointSet, Map<Integer, String> lineIndexToLine) {
        Map<Integer, Set<String>> groups = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int parent = disjointSet.find(i);
            groups.computeIfAbsent(parent, k -> new HashSet<>()).add(lineIndexToLine.get(i));
        }
        return groups;
    }
}