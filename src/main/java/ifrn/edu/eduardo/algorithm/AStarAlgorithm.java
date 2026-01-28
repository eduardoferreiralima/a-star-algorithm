package ifrn.edu.eduardo.algorithm;

import ifrn.edu.eduardo.model.Node;
import java.util.ArrayList;
import java.util.List;

public class AStarAlgorithm {

    public static int calcH(Node n, Node endNode) {
        return 10 * (Math.abs(n.r - endNode.r) + Math.abs(n.c - endNode.c));
    }
    public static List<Node> getNeighbors(Node n, Node[][] grid, int rows, int cols) {
        List<Node> list = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int nr = n.r + dr;
                int nc = n.c + dc;

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    Node neighbor = grid[nr][nc];

                    if (Math.abs(dr) == 1 && Math.abs(dc) == 1) {
                        if (grid[n.r + dr][n.c].type.equals("WALL") &&
                                grid[n.r][n.c + dc].type.equals("WALL")) {
                            continue;
                        }
                    }
                    list.add(neighbor);
                }
            }
        }
        return list;
    }

    public static void reconstructPath(Node e, Node startNode) {
        Node t = e.parent;
        while (t != null && t != startNode) {
            t.type = "PATH";
            t = t.parent;
        }
    }
}