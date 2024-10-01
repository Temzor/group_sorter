package io.sorter.processor;

import io.sorter.service.GroupBuilder;
import io.sorter.service.LineParser;
import io.sorter.service.ResultWriter;
import io.sorter.serviceimpl.GroupBuilderImpl;
import io.sorter.serviceimpl.LineParserImpl;
import io.sorter.serviceimpl.ResultWriterImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * Класс для обработки строк в файле. Поддерживает чтение из обычных и сжатых (GZIP) файлов,
 * разбор строк, построение групп и запись результатов.
 */
public class LineProcessor {
    private int groupCount = 0;

    /**
     * Обрабатывает файл построчно, группируя данные и записывая результаты.
     * Поддерживает обработку как обычных, так и сжатых GZIP файлов.
     *
     * @param inputFile путь к входному файлу.
     * @throws IOException если возникает ошибка ввода-вывода при чтении или записи файла.
     */
    public void process(String inputFile) throws IOException {
        InputStream fileStream = new FileInputStream(inputFile);
        InputStream inputStream;
        if (inputFile.endsWith(".gz")) {
            inputStream = new GZIPInputStream(fileStream);
        } else {
            inputStream = fileStream;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        GroupBuilder groupBuilder = new GroupBuilderImpl();
        ResultWriter resultWriter = new ResultWriterImpl();

        groupBuilder.initialize();

        String line;
        int lineIndex = 0;
        LineParser lineParser = new LineParserImpl();

        while ((line = reader.readLine()) != null) {
            if (isLineValid(line)) {
                String[] values = lineParser.parseLine(line);
                if (values != null) {
                    groupBuilder.processLine(lineIndex, values);
                    lineIndex++;
                }
            }
        }
        reader.close();

        groupCount = resultWriter.writeResult(groupBuilder.getGroups());
    }

    /**
     * Возвращает количество групп, сформированных в результате обработки файла.
     *
     * @return количество групп.
     */
    public int getGroupCount() {
        return groupCount;
    }

    /**
     * Проверяет, является ли строка допустимой для обработки.
     * Строка считается допустимой, если количество кавычек в ней чётное.
     *
     * @param line строка для проверки.
     * @return true, если строка допустима; false в противном случае.
     */
    boolean isLineValid(String line) {
        long quoteCount = line.chars().filter(ch -> ch == '"').count();
        return quoteCount % 2 == 0;
    }
}