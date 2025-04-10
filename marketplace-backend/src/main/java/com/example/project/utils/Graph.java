package com.example.project.utils;
import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    // Добавление ребра в граф
    public void addEdge(String from, String to, int weight) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.get(from).add(new Edge(to, weight));
    }

    // Нахождение всех возможных путей с использованием рекурсии
    public List<List<String>> findAllPaths(String start, String end) {
        List<List<String>> allPaths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();

        findAllPathsRecursive(start, end, visited, currentPath, allPaths);
        return allPaths;
    }

    private void findAllPathsRecursive(String current, String end, Set<String> visited, List<String> currentPath, List<List<String>> allPaths) {
        visited.add(current);
        currentPath.add(current);

        if (current.equals(end)) {
            allPaths.add(new ArrayList<>(currentPath)); // Сохраняем найденный путь
        } else {
            if (adjacencyList.containsKey(current)) {
                for (Edge edge : adjacencyList.get(current)) {
                    if (!visited.contains(edge.to)) {
                        findAllPathsRecursive(edge.to, end, visited, currentPath, allPaths);
                    }
                }
            }
        }

        currentPath.remove(currentPath.size() - 1); // Удаляем текущий узел из пути
        visited.remove(current); // Разблокируем узел для других путей
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