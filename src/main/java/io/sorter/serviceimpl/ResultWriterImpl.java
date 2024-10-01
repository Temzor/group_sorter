package io.sorter.serviceimpl;

import io.sorter.service.ResultWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Реализация интерфейса {@link ResultWriter}, предназначенная для записи
 * результатов в файл.
 * <p>
 * Этот класс предоставляет функциональность для фильтрации и сортировки
 * групп строк, а также для записи этих данных в текстовый файл.
 * Метод {@link #writeResult(Map)} позволяет сохранить информацию о
 * группах, содержащих более одного элемента, в файл "output.txt".
 * </p>
 */
public class ResultWriterImpl implements ResultWriter {

    /**
     * Пишет результат в файл "output.txt", включая количество групп с
     * более чем одним элементом и содержимое каждой из таких групп.
     *
     * @param groups карта, где ключ — идентификатор группы, а значение —
     *               множество строк, принадлежащих этой группе.
     * @return количество групп с более чем одним элементом, которые были записаны.
     * @throws IOException если возникает ошибка при записи в файл.
     */
    @Override
    public int writeResult(Map<Integer, Set<String>> groups) throws IOException {
        List<Set<String>> groupList = filterAndSortGroups(groups);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Количество групп с более чем одним элементом: " + groupList.size());
            writer.newLine();
            writer.newLine();

            int groupNumber = 1;
            for (Set<String> group : groupList) {
                writer.write("Группа " + groupNumber);
                writer.newLine();
                for (String line : group) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine();
                groupNumber++;
            }
        }
        return groupList.size();
    }

    /**
     * Фильтрует и сортирует группы, оставляя только группы с более чем
     * одним элементом и сортируя их по убыванию размера.
     * <p>
     * Метод удаляет группы, содержащие только один элемент или менее, и
     * сортирует оставшиеся группы по размеру в порядке убывания.
     * </p>
     *
     * @param groups карта групп для фильтрации и сортировки.
     * @return список отфильтрованных и отсортированных групп.
     */
    List<Set<String>> filterAndSortGroups(Map<Integer, Set<String>> groups) {
        List<Set<String>> groupList = new ArrayList<>(groups.values());
        groupList.removeIf(set -> set.size() <= 1);
        groupList.sort((s1, s2) -> Integer.compare(s2.size(), s1.size()));
        return groupList;
    }
}