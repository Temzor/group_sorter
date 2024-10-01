package io.sorter.service;

/**
 * Интерфейс LineParser предоставляет метод для разбора строки данных на массив значений.
 */
public interface LineParser {

    /**
     * Разобрать строку на массив значений.
     *
     * @param line строка, которую необходимо разобрать
     * @return массив значений, полученных из разбора строки
     */
    String[] parseLine(String line);
}