package io.sorter.writer;

import io.sorter.validators.UnionFind;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
     *
     * @param lines     список строк, которые будут записаны в файл
     * @param unionFind объект класса UnionFind, который управляет группами строк
     * @throws IOException если возникнет ошибка при записи в файл
     */
    public void writeResults(List<String> lines, UnionFind unionFind) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            Map<Integer, List<Integer>> groupElements = new HashMap<>();
            for (int i = 0; i < lines.size(); i++) {
                int root = unionFind.get(i); /* Получаем корень для текущей строки */
                groupElements.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
            }
            for (var entry : groupElements.entrySet()) {
                List<Integer> elements = entry.getValue();
                if (elements.size() > 1) {
                    writer.write(String.format("Group %d\n", ++groupCount)); /* Записываем информацию о группе */
                    for (var index : elements) {
                        writer.write(lines.get(index) + "\n"); /* Записываем строки группы */
                    }
                    writer.write("\n"); /* Переход на новую строку после группы */
                }
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