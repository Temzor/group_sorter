package io.sorter.calculator;

import io.sorter.parser.LineProcessor;
import io.sorter.validators.UnionFind;
import io.sorter.validators.UniqueLineChecker;
import io.sorter.writer.ResultWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс LineCalc отвечает за обработку строк из входного файла,
 * проверку уникальности строк, а также за группировку связанных строк.
 * Результаты обработки записываются в выходной файл.
 */
public class LineCalc {
    private final String inputFilename;
    private final int processorLineMax;
    private final UnionFind unionFind;
    private final List<String> lines;

    /**
     * Конструктор класса LineCalc.
     * @param inputFilename имя входного файла, содержащего строки для обработки
     */
    public LineCalc(String inputFilename) {
        this.inputFilename = inputFilename;
        this.processorLineMax = getEstimatedMaxLines(inputFilename);
        this.unionFind = new UnionFind(processorLineMax);
        this.lines = new ArrayList<>(processorLineMax);
    }

    /**
     * Метод calc осуществляет основную логику обработки строк:
     * читает строки из входного файла, определяет уникальные строки и группирует их.
     * Результаты записываются в файл "out.txt".
     * @return количество групп строк, найденных в процессе обработки
     */
    public int calc() {
        String outputFilename = "out.txt";
        UniqueLineChecker uniqueLineChecker = new UniqueLineChecker(processorLineMax);
        ResultWriter resultWriter = new ResultWriter(outputFilename);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            String str;
            int lineAmount = 0;

            while ((str = reader.readLine()) != null) {
                String[] parsedLine = LineProcessor.parseLine(str);
                if (parsedLine == null || uniqueLineChecker.isUnique(str)) {
                    continue;
                }

                lines.add(str);
                uniqueLineChecker.checkUnique(parsedLine, lineAmount, unionFind);
                lineAmount++;
            }
            resultWriter.writeResults(lines, unionFind);
            return resultWriter.getGroupCount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод getEstimatedMaxLines оценивает максимальное количество строк
     * в файле, предназначенном для обработки. Если количество строк меньше 10000,
     * возвращается 10000.
     * @param filename имя файла, количество строк в котором необходимо оценить
     * @return оцененное максимальное количество строк
     */
    private int getEstimatedMaxLines(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int linesCount = 0;
            while (reader.readLine() != null) {
                linesCount++;
            }

            return Math.max(linesCount, 10000);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filename, e);
        }
    }
}