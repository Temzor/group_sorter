package io.sorter.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniqueLineCheckerTest {
    private UniqueLineChecker uniqueLineChecker;

    @BeforeEach
    void setUp() {
        /* Инициализация UniqueLineChecker с пределом на 10 уникальных строк */
        uniqueLineChecker = new UniqueLineChecker(10);
    }

    @Test
    @DisplayName("**Проверка уникальности новой строки**")
    void testIsUniqueNewLine() {
        /* Проверяем, что новая строка определяется как уникальная */
        assertFalse(uniqueLineChecker.isUnique("новая строка"));
    }

    @Test
    @DisplayName("**Проверка существующей строки**")
    void testIsUniqueExistingLine() {
        /* Добавляем строку и проверяем её ещё раз */
        uniqueLineChecker.isUnique("существующая строка");
        assertTrue(uniqueLineChecker.isUnique("существующая строка"));
    }

    @Test
    @DisplayName("**Проверка работы метода checkUnique с пустыми строками**")
    void testCheckUniqueEmptyStrings() {
        UnionFind unionFind = new UnionFind(5);
        String[] lines = {"", "\"\"", "не пустая", "\"\""};

        /* Проверяем, что пустые строки игнорируются при проверке уникальности */
        uniqueLineChecker.checkUnique(lines, 0, unionFind);

        /* Поскольку пустые строки игнорируются, никаких объединений не происходит */
        assertEquals(0, unionFind.get(0));
    }

    @Test
    @DisplayName("**Проверка объединения строк в одной позиции**")
    void testCheckUniqueSamePosition() {
        UnionFind unionFind = new UnionFind(5);
        String[] lines1 = {"строка A", "строка B"};
        String[] lines2 = {"строка A", "новая строка C"};

        /* Проверьте первую строку */
        uniqueLineChecker.checkUnique(lines1, 0, unionFind);

        /* Проверьте вторую строку, где "строка A" находится в той же позиции */
        uniqueLineChecker.checkUnique(lines2, 1, unionFind);

        /* Убедитесь, что строки были объединены после проверки одинаковой позиции */
        assertEquals(unionFind.get(0), unionFind.get(1));
    }

    @Test
    @DisplayName("**Проверка отсутствия объединения для разных значений**")
    void testCheckUniqueDifferentValues() {
        UnionFind unionFind = new UnionFind(5);
        String[] lines1 = {"строка X", "строка Y"};
        String[] lines2 = {"другая строка X", "другая строка Y"};

        /* Проверяем первую строку */
        uniqueLineChecker.checkUnique(lines1, 0, unionFind);

        /* Проверяем вторую строку, все значения разные */
        uniqueLineChecker.checkUnique(lines2, 1, unionFind);

        /* Убедитесь, что не произошло объединения, так как значения не совпадают */
        assertNotEquals(unionFind.get(0), unionFind.get(1));
    }
}
