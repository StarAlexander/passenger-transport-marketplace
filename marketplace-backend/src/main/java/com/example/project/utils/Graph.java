package com.example.project.utils;
import java.util.*;

public class Graph {
    // Список смежности для представления графа
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    // Метод для добавления ребра в граф
    public void addEdge(String from, String to, int weight) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
    }

    // Нахождение всех возможных путей с использованием итеративного подхода
    public List<List<String>> findAllPaths(String start, String end) {
        List<List<String>> allPaths = new ArrayList<>();
        Deque<PathNode> stack = new ArrayDeque<>();

        // Инициализация: добавляем начальный узел в стек
        stack.push(new PathNode(start, new ArrayList<>(), new HashSet<>()));

        while (!stack.isEmpty()) {
            PathNode currentNode = stack.pop();
            String current = currentNode.city;
            List<String> currentPath = currentNode.path;
            Set<String> visited = currentNode.visited;

            // Добавляем текущий узел в путь и множество посещенных узлов
            currentPath.add(current);
            visited.add(current);

            // Если достигли конечного узла, сохраняем путь
            if (current.equals(end)) {
                allPaths.add(new ArrayList<>(currentPath));
            } else {
                // Если есть соседние узлы, добавляем их в стек
                if (adjacencyList.containsKey(current)) {
                    for (Edge edge : adjacencyList.get(current)) {
                        if (!visited.contains(edge.to)) {
                            // Создаем копии текущего пути и множества посещенных узлов
                            List<String> newPath = new ArrayList<>(currentPath);
                            Set<String> newVisited = new HashSet<>(visited);

                            // Добавляем новый узел в стек
                            stack.push(new PathNode(edge.to, newPath, newVisited));
                        }
                    }
                }
            }

            // Удаляем текущий узел из пути (не нужно, если создавать новые списки на каждом шаге)
            currentPath.remove(currentPath.size() - 1);
        }

        return allPaths;
    }

    // Внутренний класс для представления узла пути
    private static class PathNode {
        String city; // Текущий город
        List<String> path; // Текущий путь
        Set<String> visited; // Посещенные города

        PathNode(String city, List<String> path, Set<String> visited) {
            this.city = city;
            this.path = path;
            this.visited = visited;
        }
    }

    // Внутренний класс для представления ребра
    private static class Edge {
        String to;
        int weight;

        Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}