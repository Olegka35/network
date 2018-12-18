package service.graph;

import java.io.Serializable;
import java.util.*;

public class Graph<V> implements Serializable {
    private Map<V, List<Node<V>>> adjacencyList;
    private Set<V> vertices;
    private static final int DEFAULT_WEIGHT = Integer.MAX_VALUE;

    public Graph() {
        adjacencyList = new HashMap<>();
        vertices = new HashSet<>();
    }

    private static class Node<V> implements Serializable {
        private V name;
        private int weight;

        public Node(V name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public V getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public int hashCode() {
            return this.getName().hashCode();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name=" + name +
                    ", weight=" + weight +
                    '}';
        }
    }

    public Map<V, List<Node<V>>> getGraph() {
        return this.adjacencyList;
    }

    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }

    private void addEdge(V src, Node<V> destNode) {
        List<Node<V>> adjacentVertices = adjacencyList.get(src);
        if(adjacentVertices == null || adjacentVertices.isEmpty()) {
            adjacentVertices = new ArrayList<Node<V>>();
            adjacentVertices.add(destNode);
        } else {
            adjacentVertices.add(destNode);
        }
        adjacencyList.put(src, adjacentVertices);
    }

    public void addEdge(V src, V dest, int weight) {
        Objects.requireNonNull(src);
        Objects.requireNonNull(dest);

        this.addEdge(src, new Node<>(dest, weight));

        this.vertices.add(src);
        this.vertices.add(dest);
    }

    public void addEdge(V src, V dest) {
        Objects.requireNonNull(src);
        Objects.requireNonNull(dest);

        this.addEdge(src, new Node<>(dest, DEFAULT_WEIGHT));

        this.vertices.add(src);
        this.vertices.add(dest);
    }

    public void removeEdge(V src, V dest) {
        Objects.requireNonNull(src);
        Objects.requireNonNull(dest);

        List<Node<V>> adjacentVertices = adjacencyList.get(src);
        Node<V> removedNode = null;
        for(Node<V> node: adjacentVertices)
            if(node.name == dest)
                removedNode = node;

        if(removedNode != null)
            adjacentVertices.remove(removedNode);
    }

    public void addVertice(V vert) {
        Objects.requireNonNull(vert);
        this.vertices.add(vert);
    }

    public boolean hasRelationship(V source, V destination) {
        if (source == null && destination == null)
            return true;
        if (source != null && destination == null)
            return true;
        if (source == null && destination != null)
            return false;

        List<Node<V>> nodes = null;

        if (adjacencyList.containsKey(source)) {
            nodes = adjacencyList.get(source);
            if (nodes != null && !nodes.isEmpty()) {
                for (Node<V> neighbors : nodes) {
                    if (neighbors.getName().equals(destination))
                        return true;
                }
            }
        }
        return false;
    }

    public int getWeight(V src, V dest) {
        int weight = DEFAULT_WEIGHT;
        if (this.hasRelationship(src, dest)) {
            List<Node<V>> adjacentNodes = this.adjacencyList.get(src);
            for (Node<V> node : adjacentNodes) {
                if (node.getName().equals(dest)) {
                    weight = node.getWeight();
                }
            }
        }
        return weight;
    }

    public List<V> getAdjacentVertices(V vertex) {
        List<Node<V>> adjacentNodes = this.adjacencyList.get(vertex);
        List<V> neighborVertex = new ArrayList<>();

        if ((adjacentNodes != null) && !adjacentNodes.isEmpty()) {
            for (Node<V> v : adjacentNodes) {
                neighborVertex.add(v.getName());
            }
        }
        return neighborVertex;
    }

    public Set<V> getAllVertices() {
        return Collections.unmodifiableSet(this.vertices);
    }

    public boolean removeVertex(V vertex) {
        Objects.requireNonNull(vertex);

        if(!this.vertices.contains(vertex))
            return false;
        Iterator<Map.Entry<V, List<Node<V>>>> itr = this.adjacencyList.entrySet()
                .iterator();

        while (itr.hasNext()) {
            Map.Entry<V, List<Node<V>>> e = itr.next();
            List<Node<V>> vs = e.getValue();
            if (vertex.equals(e.getKey())) {
                itr.remove();
            }

            Iterator<Node<V>> listIterator = vs.iterator();
            while (listIterator.hasNext()) {
                Node<V> ver = listIterator.next();
                if (vertex.equals(ver.getName())) {
                    listIterator.remove();
                }

            }
        }

        Iterator<V> itrVertices = this.vertices.iterator();
        while (itrVertices.hasNext()) {
            if (vertex.equals(itrVertices.next())) {
                itrVertices.remove();
                break;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Set of Edges :\n");
        for (V v : this.adjacencyList.keySet()) {
            List<Node<V>> neighbour = this.adjacencyList.get(v);
            for (Node<V> vertex : neighbour) {
                if (vertex.getWeight() != DEFAULT_WEIGHT) {
                    sb.append(v + " -- (" + vertex.getWeight() + ")--->"
                            + vertex.getName() + "\n");
                } else {
                    sb.append(v + " ------->" + vertex.getName() + "\n");
                }

            }
        }
        sb.append("Vertices :" + this.getAllVertices());
        return sb.toString();
    }
}
