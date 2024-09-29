package io.sorter.service;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс CustomFileReader определяет метод для чтения строк из файла.
 * Основное предназначение - чтение строк из заданного файла и возврат их в виде списка.
 */
public interface CustomFileReader {

    /**
     * Читает все строки из указанного файла и возвращает их в виде списка.
     *
     * @param inputFile путь к файлу, из которого необходимо прочитать строки
     * @return список строк, прочитанных из файла
     * @throws IOException если происходит ошибка ввода-вывода при чтении файла
     */
    List<String> readLines(String inputFile) throws IOException;
}