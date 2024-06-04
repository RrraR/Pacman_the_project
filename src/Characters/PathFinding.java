package Characters;

import java.util.*;
import java.util.List;

public class PathFinding {

    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads

    private final int[][] board;
    private final int width;
    private final int height;

    public PathFinding(int[][] board) {
        this.board = board;
        this.width = board[0].length;
        this.height = board.length;
    }

    public List<Node> findPath(int startX, int startY, int goalX, int goalY) {

        if (!isWalkable(startX, startY) || !isWalkable(goalX, goalY)) {
            return null;
        }

        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        Node startNode = new Node(startX, startY, null, 0, calculateHeuristic(startX, startY, goalX, goalY));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.x == goalX && currentNode.y == goalY) {
                return reconstructPath(currentNode);
            }

            closedList.add(currentNode);

            for (Node neighbor : getNeighbors(currentNode)) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                int tentativeG = currentNode.g + 1;

                if (!openList.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor.x, neighbor.y, goalX, goalY);
                    neighbor.parent = currentNode;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = { {0, -1},
                               {0, 1},
                               {-1, 0},
                               {1, 0} };

        for (int[] direction : directions) {
            int newX = node.x + direction[0];
            int newY = node.y + direction[1];

            if (isWalkable(newX, newY)) {
                neighbors.add(new Node(newX, newY, node, node.g + 1, 0));
            }
        }

        return neighbors;
    }

    private boolean isWalkable(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height && board[y][x] != W;
    }

    private int calculateHeuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

}

class Node implements Comparable<Node> {
    int x, y;
    int g, h;
    Node parent;

    public Node(int x, int y, Node parent, int g, int h) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.g = g;
        this.h = h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }

    public int getF() {
        return g + h;
    }
}
