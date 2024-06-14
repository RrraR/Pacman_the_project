package Components;

import java.util.*;
import java.util.List;
import static Components.Boards.*;
import static Components.GameBoard.board;

public class PathFinding {

    private final int width;
    private final int height;

    public PathFinding() {
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

            if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
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
                    neighbor.h = calculateHeuristic(neighbor.getX(), neighbor.getY(), goalX, goalY);
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
            int newX = node.getX() + direction[0];
            int newY = node.getY() + direction[1];

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

