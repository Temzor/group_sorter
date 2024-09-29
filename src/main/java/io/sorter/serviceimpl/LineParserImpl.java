package io.sorter.serviceimpl;

import io.sorter.service.LineParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс LineParserImpl реализует интерфейс LineParser и отвечает за
 * парсинг строк и выделение уникальных строк с учетом корректности формата.
 */
public class LineParserImpl implements LineParser {

    /**
     * Парсит предоставленный список строк, выделяет уникальные строки,
     * проверяет их на соответствие критериям корректности и возвращает
     * список массивов строк.
     *
     * @param lines список строк, подлежащих парсингу
     * @return список уникальных массивов строк, каждая из которых
     * была разделена на элементы с использованием разделителя ";"
     */
    @Override
    public List<String[]> parseLines(List<String> lines) {
        Set<String> uniqueLineSet = new HashSet<>();
        List<String[]> uniqueLines = new ArrayList<>();

        for (String line : lines) {
            if (isLineValid(line) && uniqueLineSet.add(line)) {
                String[] values = line.split(";", -1);
                uniqueLines.add(values);
            }
        }

        return uniqueLines;
    }

    /**
     * Проверяет корректность строки. Строка считается корректной,
     * если количество кавычек в ней - четное.
     *
     * @param line строка для проверки
     * @return true, если строка корректна, иначе false
     */
    private boolean isLineValid(String line) {
        long quoteCount = line.chars().filter(ch -> ch == '"').count();
        return quoteCount % 2 == 0;
    }
}