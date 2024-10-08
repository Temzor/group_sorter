package io.sorter.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnionFindTest {
    private UnionFind unionFind;

    @BeforeEach
    void setUp() {
        /* Инициализация UnionFind размером 5 */
        unionFind = new UnionFind(5);
    }

    @Test
    @DisplayName("**Проверка начального состояния**")
    void testInitialState() {
        /* Каждый элемент изначально является своим собственным родителем */
        for (int i = 0; i < 5; i++) {
            assertEquals(i, unionFind.get(i));
        }
    }

    @Test
    @DisplayName("**Объединение элементов в одно множество**")
    void testUnion() {
        /* Объединяем элементы 0 и 1 */
        unionFind.union(0, 1);

        // Проверяем, что теперь у них один и тот же корневой родитель
        assertEquals(unionFind.get(0), unionFind.get(1));
    }

    @Test
    @DisplayName("**Проверка путевого сжатия**")
    void testPathCompression() {
        /* Объединяем 0-1, затем 1-2 (что фактически объединяет все три 0-1-2) */
        unionFind.union(0, 1);
        unionFind.union(1, 2);

        /* Убеждаемся, что все три элемента имеют одного корневого родителя */
        assertEquals(unionFind.get(0), unionFind.get(2));

        /* Текущее состояние должно поддерживать один корень для всех */
        assertEquals(unionFind.get(0), unionFind.get(1));
    }

    @Test
    @DisplayName("**Объединение множеств с различными рангами**")
    void testUnionWithDifferentRanks() {
        /* Объединяем элементы 0 и 1, 2 и 3 */
        unionFind.union(0, 1);
        unionFind.union(2, 3);

        /* Делаем элементы 0 и 2 корнями множеств */
        unionFind.union(0, 2);

        /* Проверяем, что корневой родитель у всех одинаковый */
        int root0 = unionFind.get(0);
        assertEquals(root0, unionFind.get(1));
        assertEquals(root0, unionFind.get(2));
        assertEquals(root0, unionFind.get(3));
    }

    @Test
    @DisplayName("**Объединение элемента с самим собой**")
    void testUnionSelf() {
        /* Объединяем элемент с самим собой */
        unionFind.union(1, 1);

        /* Убеждаемся, что родитель не изменился */
        assertEquals(1, unionFind.get(1));
    }
}