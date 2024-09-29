package io.sorter.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DisjointSetTest {

    @Test
    @DisplayName("Тест метода объединения и поиска")
    public void testFindAndUnion() {
        DisjointSet ds = new DisjointSet(5);

        ds.union(0, 1);
        ds.union(1, 2);

        assertEquals(ds.find(0), ds.find(2));
        assertNotEquals(ds.find(0), ds.find(3));

        ds.union(3, 4);

        assertEquals(ds.find(3), ds.find(4));
        assertNotEquals(ds.find(2), ds.find(3));
    }

    @Test
    @DisplayName("Тест множества с одиночными элементами")
    public void testSingleElementSets() {
        DisjointSet ds = new DisjointSet(3);

        assertEquals(0, ds.find(0));
        assertEquals(1, ds.find(1));
        assertEquals(2, ds.find(2));
    }
}