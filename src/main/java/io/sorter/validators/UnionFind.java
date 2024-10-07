package io.sorter.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс UnionFind предоставляет реализацию структуры данных "система непересекающихся множеств"
 * (Union-Find) с использованием подхода путевого сжатия и рангов для эффективного объединения
 * множеств и поиска их корневых элементов.
 */
public class UnionFind {
    private final List<Integer> ranks;  /* Список, хранящий ранги наборов */
    private final List<Integer> parents; /* Список, хранящий родительские элементы для каждого элемента */

    /**
     * Конструктор класса UnionFind инициализирует структуру данных для указанного размера.
     * Каждый элемент инициализируется как собственный родитель, а ранг каждого элемента равен 0.
     *
     * @param size размер множества, количество элементов в Union-Find
     */
    public UnionFind(int size) {
        ranks = new ArrayList<>(Collections.nCopies(size, 0));
        parents = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            parents.add(i);
        }
    }

    /**
     * Метод get возвращает корневого родителя для элемента, выполняя путь сжатия.
     *
     * @param i индекс элемента, для которого нужно получить корневого родителя
     * @return индекс корневого элемента множества, к которому принадлежит элемент i
     */
    public int get(int i) {
        if (parents.get(i) != i) {
            parents.set(i, get(parents.get(i))); /* Путь сжатия */
        }
        return parents.get(i);
    }

    /**
     * Метод union объединяет два элемента в одно множество.
     * Если элементы принадлежат одному множеству, то операция игнорируется.
     * Иначе объединяются множества, к которым принадлежат указанные элементы,
     * с учетом их рангов для поддержания сбалансированности структуры.
     *
     * @param firstElement  первый элемент, который необходимо объединить
     * @param secondElement второй элемент, который необходимо объединить
     */
    public void union(int firstElement, int secondElement) {
        firstElement = get(firstElement); /* Получаем корень первого элемента */
        secondElement = get(secondElement); /* Получаем корень второго элемента */
        if (firstElement == secondElement) {
            return; /* Если уже в одном множестве, ничего не делаем */
        }
        if (ranks.get(firstElement).equals(ranks.get(secondElement))) {
            ranks.set(firstElement, ranks.get(firstElement) + 1); /* Увеличиваем ранг */
        }
        /* Объединяем множества согласно рангу */
        if (ranks.get(firstElement) < ranks.get(secondElement)) {
            parents.set(firstElement, secondElement);
        } else {
            parents.set(secondElement, firstElement);
        }
    }
}