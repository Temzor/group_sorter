package io.sorter;

import io.sorter.processor.LineProcessor;

import java.io.IOException;

/**
 * Основной класс, содержащий метод main для запуска программы обработки файла.
 * Программа ожидает имя текстового файла в качестве аргумента командной строки,
 * обрабатывает строки из файла и выводит результаты.
 */
public class Main {

    /**
     * Метод main, который является точкой входа в программу.
     * Он считывает имя входного файла из аргументов командной строки,
     * запускает обработку файла, подсчитывает количество групп строк
     * и выводит время выполнения программы.
     *
     * @param args аргументы командной строки, где args[0] должно содержать имя файла для обработки
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: java -jar имя_пакета.jar inputfile.txt");
            return;
        }

        String inputFile = args[0];
        long startTime = System.currentTimeMillis();

        try {
            LineProcessor lineProcessor = new LineProcessor();
            lineProcessor.process(inputFile);

            long endTime = System.currentTimeMillis();
            System.out.println("Время выполнения программы: " + (endTime - startTime) + " мс");
            System.out.println("Количество групп с более чем одним элементом: " + lineProcessor.getGroupCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}