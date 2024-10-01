package io.sorter.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {
    private DisjointSet disjointSet;


    @BeforeEach
    public void setUp() {
        disjointSet = new DisjointSet();
    }

    @Test
    @DisplayName("Тест добавления элементов и поиска представителя")
    public void testAddElementAndFind() {
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.addElement(1);
        disjointSet.addElement(2);
        disjointSet.addElement(3);

        assertEquals(1, disjointSet.find(1));
        assertEquals(2, disjointSet.find(2));
        assertEquals(3, disjointSet.find(3));
    }

    @Test
    @DisplayName("Тест объединения множеств и поиска")
    public void testUnionAndFind() {
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.addElement(1);
        disjointSet.addElement(2);
        disjointSet.addElement(3);
        disjointSet.addElement(4);

        /* Объединяем элементы */
        disjointSet.union(1, 2);
        disjointSet.union(3, 4);

        /* Проверяем, что 1 и 2 в одном множестве */
        assertEquals(disjointSet.find(1), disjointSet.find(2));

        /* Проверяем, что 3 и 4 в одном множестве */
        assertEquals(disjointSet.find(3), disjointSet.find(4));

        /* Проверяем, что 1 и 3 в разных множествах */
        assertNotEquals(disjointSet.find(1), disjointSet.find(3));

        /* Объединяем все вместе */
        disjointSet.union(2, 3);

        /* Теперь все элементы должны быть в одном множестве */
        assertEquals(disjointSet.find(1), disjointSet.find(4));
    }

    @Test
    @DisplayName("Тест получения всех элементов")
    public void testGetElements() {
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.addElement(5);
        disjointSet.addElement(6);

        Set<Integer> elements = disjointSet.getElements();
        assertTrue(elements.contains(5));
        assertTrue(elements.contains(6));
        assertEquals(2, elements.size());
    }

    @Test
    @DisplayName("Проверка добавления элемента")
    public void testAddElement() {
        disjointSet.addElement(1);
        assertEquals(Set.of(1), disjointSet.getElements(), "Элемент 1 должен быть добавлен.");
    }

    @Test
    @DisplayName("Поиск корня существующего элемента")
    public void testFindSelfRoot() {
        disjointSet.addElement(2);
        assertEquals(2, disjointSet.find(2), "Корень элемента 2 должен быть 2.");
    }

    @Test
    @DisplayName("Объединение двух элементов")
    public void testUnion() {
        disjointSet.addElement(1);
        disjointSet.addElement(2);
        disjointSet.union(1, 2);

        assertEquals(disjointSet.find(1), disjointSet.find(2), "Элементы 1 и 2 должны быть объединены.");
    }


    @ParameterizedTest(name = "{index} => элемент x={0}, элемент y={1}, ожидаемый корень={2}")
    @DisplayName("Параметризованный тест объединения и поиска корня")
    @CsvSource({
            "3, 4, 4",
            "5, 6, 6",
            "7, 8, 8"
    })
    public void testUnionAndFindParameterized(int x, int y, int expectedRoot) {
        disjointSet.addElement(x);
        disjointSet.addElement(y);
        disjointSet.union(x, y);

        int actualRoot = disjointSet.find(x);
        assertEquals(expectedRoot, actualRoot, "Ожидаемый корень после объединения должен соответствовать.");
    }
}