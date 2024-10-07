package io.sorter.validators;

import java.util.*;

/**
 * Класс UniqueLineChecker предназначен для проверки уникальности строк и
 * объединения строк, которые представляют собой одинаковые или эквивалентные значения.
 * Он использует множество для хранения уникальных строк и список для отслеживания
 * позиций строк, которые были проверены.
 */
public class UniqueLineChecker {
    private final Set<String> uniqueLines; // Множество для хранения уникальных строк
    private final List<Map<String, Integer>> places; // Список, хранящий карты строк и их позиций

    /**
     * Конструктор класса UniqueLineChecker инициализирует структуру данных
     * для указанного максимального количества строк.
     *
     * @param maxLines максимальное количество уникальных строк, которое может быть хранится
     */
    public UniqueLineChecker(int maxLines) {
        this.uniqueLines = new HashSet<>(maxLines);
        this.places = new ArrayList<>();
    }

    /**
     * Метод isUnique проверяет, является ли переданная строка уникальной.
     * Если строка уже существует в множестве уникальных строк, метод возвращает true.
     * Если строка новая, она добавляется в множество и метод возвращает false.
     *
     * @param line строка, которая должна быть проверена на уникальность
     * @return true, если строка уже существует; false, если строка уникальна
     */
    public boolean isUnique(String line) {
        if (uniqueLines.contains(line)) {
            return true; /* Строка уже существует */
        }
        uniqueLines.add(line); /* Добавляем новую строку */
        return false; /* Строка уникальна */
    }

    /**
     * Метод checkUnique проверяет уникальность коллекции строк, и если одна из
     * строк уже присутствует в определённой позиции, объединяет её с указанным номером строки
     * с помощью структуры Union-Find.
     *
     * @param lineStrings массив строк для проверки
     * @param lineNumber  номер текущей строки
     * @param unionFind   объект класcа UnionFind для объединения строк
     */
    public void checkUnique(String[] lineStrings, int lineNumber, UnionFind unionFind) {
        for (int i = 0; i < lineStrings.length; i++) {
            String cur = lineStrings[i];
            if (i >= places.size()) {
                Map<String, Integer> map = new HashMap<>();
                map.put(cur, lineNumber);
                places.add(map); /* Добавляем новую карту, если это новая позиция */
            } else if (!checkEmptyString(cur) && places.get(i).containsKey(cur)) {
                int match = places.get(i).get(cur);
                unionFind.union(match, lineNumber); /* Объединяем строки */
            } else {
                places.get(i).put(cur, lineNumber); /* Добавляем строку и её номер */
            }
        }
    }

    /**
     * Метод checkEmptyString проверяет, является ли строка пустой или содержит
     * эквивалент пустой строки ("").
     *
     * @param string строка, которую нужно проверить
     * @return true, если строка пустая или равна "\"\""; false в противном случае
     */
    private boolean checkEmptyString(String string) {
        return string.isEmpty() || string.equals("\"\"");
    }
}