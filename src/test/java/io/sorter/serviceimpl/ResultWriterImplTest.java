package io.sorter.serviceimpl;

import io.sorter.service.ResultWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResultWriterImplTest {

    private ResultWriter resultWriter;
    private static final String OUTPUT_FILE = "output.txt";

    @BeforeEach
    public void setUp() {
        resultWriter = new ResultWriterImpl();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
    }

    @Test
    @DisplayName("Тест обработки null-аргумента")
    public void testWriteResultWithNullArgument() {
        assertThrows(NullPointerException.class, () -> resultWriter.writeResult(null),
                "Должно выбрасываться исключение NullPointerException при передаче null-аргумента");
    }
}