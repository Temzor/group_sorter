package io.sorter.processor;

import io.sorter.service.CustomFileReader;
import io.sorter.service.GroupBuilder;
import io.sorter.service.LineParser;
import io.sorter.service.ResultWriter;
import io.sorter.serviceimpl.CustomFileReaderImpl;
import io.sorter.serviceimpl.GroupBuilderImpl;
import io.sorter.serviceimpl.LineParserImpl;
import io.sorter.serviceimpl.ResultWriterImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * *Класс LineProcessor предназначен для обработки строк из файла и группировки данных.**
 */
public class LineProcessor {
    private final CustomFileReader customFileReader;
    private final LineParser lineParser;
    private final GroupBuilder groupBuilder;
    private final ResultWriter resultWriter;

    private int groupCount;

    /**
     * *Конструктор по умолчанию, инициализирующий зависимости.**
     */
    public LineProcessor() {
        this.customFileReader = new CustomFileReaderImpl();
        this.lineParser = new LineParserImpl();
        this.groupBuilder = new GroupBuilderImpl();
        this.resultWriter = new ResultWriterImpl();
    }

    /**
     * *Метод для обработки строк из указанного входного файла.**
     *
     * @param inputFile путь к входному файлу
     * @throws IOException если возникает ошибка при чтении файла
     */
    public void process(String inputFile) throws IOException {
        List<String> lines = customFileReader.readLines(inputFile);
        List<String[]> parsedLines = lineParser.parseLines(lines);
        Map<Integer, Set<String>> groups = groupBuilder.buildGroups(parsedLines);
        groupCount = resultWriter.writeResult(groups);
    }

    /**
     * *Метод для получения количества групп, сформированных в процессе обработки.**
     *
     * @return количество групп
     */
    public int getGroupCount() {
        return groupCount;
    }
}