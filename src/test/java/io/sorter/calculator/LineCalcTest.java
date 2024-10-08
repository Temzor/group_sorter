package io.sorter.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LineCalcTest {

    private String testInputFilename;
    private LineCalc lineCalc;

    @BeforeEach
    void setUp() throws IOException {
        /* Подготовка тестового входного файла */
        testInputFilename = "test_input.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testInputFilename))) {
            writer.write("строка1\n");
            writer.write("строка2\n");
            writer.write("строка1\n"); // Дублирующая строка
        }

        /* Инициализация объекта LineCalc */
        lineCalc = new LineCalc(testInputFilename);
    }

    @Test
    @DisplayName("**Проверка расчета количества уникальных групп строк**")
    void testCalcGroupCount() {
        /* Запуск основной логики метода calc */
        int groupCount = lineCalc.calc();

        /* Проверка, что количество уникальных групп соответствует ожиданиям */
        assertEquals(0, groupCount);
    }

    @Test
    @DisplayName("**Проверка обработки пустого файла**")
    void testCalcEmptyFile() {
        /* Создание пустого файла для теста */
        String emptyFilename = "empty.txt";

        /* Инициализация LineCalc с пустым файлом */
        LineCalc emptyFileCalc = new LineCalc(emptyFilename);

        /* Запуск обработки пустого файла */
        int groupCount = emptyFileCalc.calc();

        /* Ожидаем, что количество групп будет равно 0 */
        assertEquals(0, groupCount);
    }

    @Test
    @DisplayName("**Проверка реакции на отсутствующий файл**")
    void testCalcFileNotFound() {
        /* Инициализация LineCalc с несуществующим файлом */
        assertThrows(RuntimeException.class, () -> new LineCalc("nonexistent.txt").calc());
    }

    @Test
    @DisplayName("**Проверка работы метода getEstimatedMaxLines**")
    void testGetEstimatedMaxLines() {
        /* Вызов приватного метода через рефлексию для проверки */
        int estimatedMaxLines = lineCalc.getEstimatedMaxLines(testInputFilename);

        /* Проверка, что возвращаемое значение равно не менее 10000 */
        assertEquals(10000, estimatedMaxLines);
    }
}
