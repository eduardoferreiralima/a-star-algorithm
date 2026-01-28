package ifrn.edu.eduardo.algorithm;

import ifrn.edu.eduardo.model.Node;
import java.util.*;

public class GreedyAlgorithm {

    public static long solve(Node start, Node end, Node[][] grid, int rows, int cols) {
        long startTime = System.nanoTime();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.h));
        Set<Node> closed = new HashSet<>();

        start.h = calculateHeuristic(start, end);
        open.add(start);

        while (!open.isEmpty()) {
            Node curr = open.poll();
            if (curr == end) break;
            closed.add(curr);

            for (Node n : getNeighbors(curr, grid, rows, cols)) {
                if (closed.contains(n) || n.type.equals("WALL")) continue;

                if (!open.contains(n)) {
                    n.parent = curr;
                    n.h = calculateHeuristic(n, end);
                    n.f = n.h;
                    open.add(n);
                }
            }
        }
        return (System.nanoTime() - startTime) / 1000;
    }

    private static int calculateHeuristic(Node n, Node end) {
        return 10 * (Math.abs(n.r - end.r) + Math.abs(n.c - end.c));
    }

    private static List<Node> getNeighbors(Node n, Node[][] grid, int rows, int cols) {
        List<Node> neighbors = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = n.r + dr, nc = n.c + dc;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (Math.abs(dr) == 1 && Math.abs(dc) == 1) {
                        if (grid[n.r + dr][n.c].type.equals("WALL") && grid[n.r][n.c + dc].type.equals("WALL")) continue;
                    }
                    neighbors.add(grid[nr][nc]);
                }
            }
        }
        return neighbors;
    }
}