package io.sorter.serviceimpl;

import io.sorter.mapper.DisjointSet;
import io.sorter.service.GroupBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Реализация интерфейса {@link GroupBuilder}, предоставляющая методы
 * для управления группами строк на основе значения их элементов.
 */
public class GroupBuilderImpl implements GroupBuilder {
    private DisjointSet disjointSet;
    private Map<String, Integer> valueToIndex;
    private Map<Integer, String> lineIndexToLine;

    /**
     * Инициализирует необходимые структуры данных для управления группами.
     */
    @Override
    public void initialize() {
        disjointSet = new DisjointSet();
        valueToIndex = new HashMap<>();
        lineIndexToLine = new HashMap<>();
    }

    /**
     * Обрабатывает строку, добавляя её в структуры данных и объединяя с
     * ранее обработанными строками при наличии общих значений.
     *
     * @param lineIndex индекс строки, представляющий её уникальный идентификатор
     * @param values массив значений, содержащихся в строке
     */
    @Override
    public void processLine(int lineIndex, String[] values) {
        disjointSet.addElement(lineIndex);
        lineIndexToLine.put(lineIndex, String.join(";", values));

        for (int colIndex = 0; colIndex < values.length; colIndex++) {
            String value = values[colIndex].trim();

            if (!value.isEmpty()) {
                String key = colIndex + "|" + value;

                if (valueToIndex.containsKey(key)) {
                    int existingIndex = valueToIndex.get(key);
                    disjointSet.union(lineIndex, existingIndex);
                } else {
                    valueToIndex.put(key, lineIndex);
                }
            }
        }
    }

    /**
     * Возвращает группы строк, сгруппированных на основе общих значений.
     *
     * @return карта, где ключ - это идентификатор группы, а значение - множество строк, принадлежащих этой группе
     */
    @Override
    public Map<Integer, Set<String>> getGroups() {
        return collectGroups();
    }

    /**
     * Собирает группы строк, объединив их в соответствии с принадлежностью
     * к одному множеству в структуре {@link DisjointSet}.
     *
     * @return карта, где ключ - это идентификатор группы, а значение - множество строк, принадлежащих этой группе
     */
    private Map<Integer, Set<String>> collectGroups() {
        Map<Integer, Set<String>> groups = new HashMap<>();
        for (int i : disjointSet.getElements()) {
            int parent = disjointSet.find(i);
            groups.computeIfAbsent(parent, k -> new HashSet<>()).add(lineIndexToLine.get(i));
        }

        return groups;
    }
}