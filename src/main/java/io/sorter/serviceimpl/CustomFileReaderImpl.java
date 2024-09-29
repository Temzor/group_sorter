package io.sorter.serviceimpl;

import io.sorter.service.CustomFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * *Класс CustomFileReaderImpl реализует интерфейс CustomFileReader и предоставляет функциональность для чтения строк из gzip-архивированного файла.**
 */
public class CustomFileReaderImpl implements CustomFileReader {
    /**
     * *Метод для чтения строк из указанного входного файла.**
     *
     * @param inputFile путь к входному файлу
     * @return список строк, считанных из файла
     * @throws IOException если возникает ошибка при чтении файла
     */
    @Override
    public List<String> readLines(String inputFile) throws IOException {
        List<String> lines = new ArrayList<>();

        try (InputStream fileStream = new FileInputStream(inputFile);
             InputStream gzipStream = new GZIPInputStream(fileStream);
             Reader decoder = new InputStreamReader(gzipStream, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(decoder)) {

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        return lines;
    }
}