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
 * Класс ResultWriterImpl реализует интерфейс ResultWriter и занимается
 * записью групп строк в файл, отбирая только те группы, которые содержат
 * более одного элемента.
 */
public class ResultWriterImpl implements ResultWriter {

    /**
     * Записывает отфильтрованные и отсортированные группы строк в файл.
     * Каждая группа, содержащая более одного элемента, записывается
     * в файл "output.txt".
     *
     * @param groups карта, где ключом является идентификатор группы,
     *               а значением - множество уникальных строк
     * @return количество групп, содержащих более одного элемента
     * @throws IOException если произошла ошибка при записи в файл
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
     * Фильтрует и сортирует группы строк. Удаляет группы,
     * содержащие один или менее элементов, и сортирует оставшиеся
     * группы по убыванию их размера.
     *
     * @param groups карта, где ключом является идентификатор группы,
     *               а значением - множество уникальных строк
     * @return список отфильтрованных и отсортированных групп
     */
    private List<Set<String>> filterAndSortGroups(Map<Integer, Set<String>> groups) {
        List<Set<String>> groupList = new ArrayList<>(groups.values());
        groupList.removeIf(set -> set.size() <= 1);
        groupList.sort((s1, s2) -> Integer.compare(s2.size(), s1.size()));
        return groupList;
    }
}