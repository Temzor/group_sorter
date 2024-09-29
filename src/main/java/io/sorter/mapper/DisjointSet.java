package io.sorter.mapper;

/**
 * Класс DisjointSet реализует структуру данных "Система непересекающихся множеств"
 * (Union-Find), которая предоставляет операции для объединения множеств и
 * определения корня элемента с использованием компрессии пути для оптимизации.
 */
public class DisjointSet {
    /**
     * Массив родительских узлов для представления множества.
     * parent[i] - родитель элемента i. Если parent[i] == i,
     * то i является корнем дерева.
     */
    private final int[] parent;

    /**
     * Создает новую систему непересекающихся множеств с заданным количеством элементов.
     *
     * @param size количество элементов в системе множеств
     */
    public DisjointSet(int size) {
        parent = new int[size];
        initializeParent(size);
    }

    /**
     * Инициализирует массив родительских узлов, задав каждый элемент своим собственным родителем.
     *
     * @param size количество элементов, для которых необходимо инициализировать родительские узлы
     */
    private void initializeParent(int size) {
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    /**
     * Определяет корневой элемент множества, к которому принадлежит элемент element.
     * Реализует оптимизацию "компрессии пути", чтобы сократить путь от элемента до корня.
     *
     * @param element элемент, для которого необходимо найти корень
     * @return корневой элемент множества, в которое входит элемент element
     */
    public int find(int element) {
        if (parent[element] == element) {
            return element;
        } else {
            int root = find(parent[element]);
            parent[element] = root; // Компрессия пути
            return root;
        }
    }

    /**
     * Объединяет множества, содержащие элементы x и y, путем присоединения
     * одного дерева к корню другого.
     *
     * @param x элемент первого множества
     * @param y элемент второго множества
     */
    public void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);
        if (xRoot != yRoot) {
            parent[yRoot] = xRoot;
        }
    }
}