package io.sorter.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Класс для представления структуры данных "Система непересекающихся множеств" (disjoint set).
 * Позволяет эффективно выполнять операции нахождения и объединения множеств.
 */
public class DisjointSet {
    private final Map<Integer, Integer> parent;

    /**
     * Создает новый экземпляр системы непересекающихся множеств.
     */
    public DisjointSet() {
        parent = new HashMap<>();
    }

    /**
     * Добавляет элемент в систему непересекающихся множеств.
     * Если элемент уже существует, операция не изменяет структуру.
     *
     * @param x элемент, который необходимо добавить.
     */
    public void addElement(int x) {
        parent.putIfAbsent(x, x);
    }

    /**
     * Находит и возвращает представителя (корень) множества, в которое входит данный элемент.
     * Производит сжатие пути, чтобы ускорить будущие запросы.
     *
     * @param element элемент, для которого необходимо найти корень.
     * @return представитель множества, в которое входит данный элемент.
     */
    public int find(int element) {
        if (parent.get(element) != element) {
            parent.put(element, find(parent.get(element)));
        }
        return parent.get(element);
    }

    /**
     * Объединяет два множества, в которые входят заданные элементы.
     * Если элементы находятся в одном множестве, операция не изменяет структуру.
     *
     * @param x первый элемент для объединения.
     * @param y второй элемент для объединения.
     */
    public void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);

        if (xRoot != yRoot) {
            parent.put(xRoot, yRoot);
        }
    }

    /**
     * Возвращает множество всех добавленных элементов.
     *
     * @return коллекция, содержащая все добавленные элементы.
     */
    public Set<Integer> getElements() {
        return parent.keySet();
    }
}