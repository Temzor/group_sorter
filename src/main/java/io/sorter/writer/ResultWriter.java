package io.sorter.writer;

import io.sorter.validators.UnionFind;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс ResultWriter предназначен для записи результатов группировки строк в
 * указанный файл. Он использует структуру Union-Find для определения групп
 * и объединяет строки, которые принадлежат одной группе.
 */
public class ResultWriter {
    private final String outputFilename; /* Имя файла для записи результатов */
    private int groupCount; /* Счетчик групп */

    /**
     * Конструктор класса ResultWriter инициализирует имя выходного файла.
     *
     * @param outputFilename имя файла, в который будут записаны результаты
     */
    public ResultWriter(String outputFilename) {
        this.outputFilename = outputFilename;
        this.groupCount = 0; /* Изначально групп нет */
    }

    /**
     * Метод writeResults записывает группированные строки в файл.
     * Он использует Union-Find для определения корня каждой строки
     * и группирует их по этому корню.
     * Перед выводом группирует и сортирует группы по размеру в порядке убывания.
     * Также в начале выводит количество групп с более чем одним элементом.
     *
     * @param lines     список строк, которые будут записаны в файл
     * @param unionFind объект класса UnionFind, который управляет группами строк
     * @throws IOException если возникнет ошибка при записи в файл
     */
    public void writeResults(List<String> lines, UnionFind unionFind) throws IOException {
        /* Используем BufferedWriter с OutputStreamWriter и явно указываем кодировку UTF-8 */
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFilename), StandardCharsets.UTF_8))) {

            Map<Integer, List<Integer>> groupElements = new HashMap<>();
            for (int i = 0; i < lines.size(); i++) {
                int root = unionFind.get(i); /* Получаем корень для текущей строки */
                groupElements.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
            }

            /* Собираем группы с более чем одним элементом */
            List<List<Integer>> groupsWithMoreThanOneElement = new ArrayList<>();
            for (List<Integer> group : groupElements.values()) {
                if (group.size() > 1) {
                    groupsWithMoreThanOneElement.add(group);
                }
            }

            /* Сортируем группы по количеству элементов в порядке убывания */
            groupsWithMoreThanOneElement.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

            /* Записываем количество групп с более чем одним элементом */
            groupCount = groupsWithMoreThanOneElement.size();
            writer.write("Количество групп с более чем одним элементом: " + groupCount + "\n\n");

            /* Выводим группы с наибольшим числом элементов сверху */
            int groupNumber = 1;
            for (List<Integer> group : groupsWithMoreThanOneElement) {
                writer.write(String.format("Группа %d\n", groupNumber));
                for (int index : group) {
                    writer.write(lines.get(index) + "\n");
                }
                writer.write("\n"); /* Переход на новую строку после группы */
                groupNumber++;
            }
        }
    }

    /**
     * Метод getGroupCount возвращает количество групп, которые были записаны.
     *
     * @return количество групп
     */
    public int getGroupCount() {

        return groupCount; /* Возвращаем счетчик групп */
    }
}