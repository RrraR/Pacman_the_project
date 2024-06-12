package Components;

import java.util.Objects;

public class Node implements Comparable<Node> {
    private int x;
    private int y;
    int g, h;
    Node parent;

    public Node(int x, int y, Node parent, int g, int h) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.g = g;
        this.h = h;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
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
