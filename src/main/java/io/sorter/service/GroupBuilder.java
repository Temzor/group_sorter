package io.sorter.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Интерфейс GroupBuilder определяет метод для построения групп из уникальных строк.
 * Основная цель - организовать данные, представленные в виде списка строковых массивов,
 * в группы, сгруппированные по некоторому критерию.
 */
public interface GroupBuilder {

    /**
     * Организует уникальные строки в группы.
     *
     * @param uniqueLines список массивов строк, представляющих уникальные строки
     * @return карту, где ключ - это целое число, идентифицирующее группу, а значение -
     * множество строк, принадлежащих этой группе
     */
    Map<Integer, Set<String>> buildGroups(List<String[]> uniqueLines);
}