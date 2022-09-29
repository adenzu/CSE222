import java.util.*;
import java.util.function.BiConsumer;

/**
 * Adjacency list-like implementation of <code>DynamicGraph</code>.
 *
 * Provides constant-time performance for adding and getting vertices.
 * Removal of a vertex has a O(nlogn) time performance, but it can go as fast as constant time.
 *
 * Basic operations on edges (add, get, remove) have O(logn) time performance and checking if
 * an edge exists is constant-time.
 */
public class MyGraph implements DynamicGraph {

    private final LinkedHashMap<Integer, TreeMap<Integer, Edge>> adjacencyList;
    private final HashMap<Integer, Vertex> vertices;
    private final boolean directed;

    /**
     * Creates an undirected graph.
     */
    public MyGraph() {
        this(false);
    }

    /**
     * Creates either a directed or undirected graph by given value.
     * @param directed  Is graph directed.
     */
    public MyGraph(boolean directed) {
        adjacencyList = new LinkedHashMap<>();
        vertices = new HashMap<>();
        this.directed = directed;
    }

    @Override
    public void addVertex(Vertex vertex) {
        TreeMap<Integer, Edge> priorEdges = adjacencyList.get(vertex.getId());
        adjacencyList.put(vertex.getId(), priorEdges == null ? new TreeMap<>() : priorEdges);
        vertices.put(vertex.getId(), vertex);
    }

    @Override
    public void addEdge(int vertexID1, int vertexID2, double weight) {
        if (!adjacencyList.containsKey(vertexID1)) throw new NoSuchVertex(vertexID1);
        if (!adjacencyList.containsKey(vertexID2)) throw new NoSuchVertex(vertexID2);

		adjacencyList.get(vertexID1).put(vertexID2, new Edge(vertexID1, vertexID2, weight));
		if (!directed) {
			adjacencyList.get(vertexID2).put(vertexID1, new Edge(vertexID2, vertexID1, weight));
		}
    }

    @Override
    public void removeEdge(int vertexID1, int vertexID2) {
        if (!adjacencyList.containsKey(vertexID1)) return;
        adjacencyList.get(vertexID1).remove(vertexID2);
        if (!directed) {
            if (!adjacencyList.containsKey(vertexID2)) return;
            adjacencyList.get(vertexID2).remove(vertexID1);
        }
    }

    @Override
    public Vertex getVertex(int vertexID) {
        return vertices.get(vertexID);
    }

    @Override
    public void removeVertex(int vertexID) {
        if (directed) {
            adjacencyList.remove(vertexID);
            adjacencyList.forEach((vertex, edges) -> edges.remove(vertexID));
        }
        else {
            for (Integer destID : adjacencyList.remove(vertexID).keySet()) {
                if (adjacencyList.containsKey(destID)) {
                    adjacencyList.get(destID).remove(vertexID);
                }
            }
        }
        vertices.remove(vertexID);
    }

    @Override
    public void removeVertex(String label) {
        BiConsumer<Integer, Vertex> remover;
        LinkedList<Integer> removedVertices = new LinkedList<>();

        if (directed) {
            remover =
                    (integer, vertex) -> {
                        if (vertices.get(integer).getLabel().equals(label)) {
                            adjacencyList.remove(integer);
                            removedVertices.add(integer);
                        }
                        else {
                            TreeMap<Integer, Edge> edges = adjacencyList.get(integer);
                            LinkedList<Integer> destToRemove = new LinkedList<>();

                            edges.forEach((dest, edge) -> {
                                if (vertices.get(dest).getLabel().equals(label)) {
                                    destToRemove.add(dest);
                                }
                            });

                            for (int dest : destToRemove) {
                                edges.remove(dest);
                            }
                        }
                    };
        }
        else {
            remover =
                    (integer, vertex) -> {
                        if (vertices.get(integer).getLabel().equals(label)) {
                            for (Integer dest : adjacencyList.remove(integer).keySet()) {
                                if (vertices.get(dest).getLabel().equals(label)) {
                                    adjacencyList.remove(dest);
                                    removedVertices.add(dest);
                                }
                                else {
                                    adjacencyList.get(dest).remove(integer);
                                }
                            }
                            removedVertices.add(integer);
                        }
                    };
        }

        vertices.forEach(remover);

        for (int vertexID : removedVertices) {
            vertices.remove(vertexID);
        }
    }

    @Override
    public MyGraph filterVertices(String key, String filter) {
        MyGraph filtered = new MyGraph(directed);

        vertices.forEach((integer, vertex) -> {
            String value = vertex.getValue(key);
            if (value != null && value.equals(filter)) {
                filtered.addVertex(vertex);
            }
        });

        for (Integer vertexID : filtered.vertices.keySet()) {
            Iterator<Edge> edgeIterator = edgeIterator(vertexID);

            while (edgeIterator.hasNext()) {
                Edge edge = edgeIterator.next();
                if (filtered.vertices.get(edge.getDest()) != null) {
                    filtered.addEdge(vertexID, edge.getDest(), edge.getWeight());
                }
            }
        }

        return filtered;
    }

    @Override
    public double[][] exportMatrix() {
        int vertexCount = getNumV();
        double[][] adjacencyMatrix = new double[vertexCount + 1][vertexCount + 1];

        for (int i = 1; i < vertexCount + 1; i++) {
            for (int j = 1; j < vertexCount + 1; j++) {
                adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        HashMap<Integer, Integer> idToIndex = new HashMap<>(vertexCount);
        LinkedList<Collection<Edge>> edgesList = new LinkedList<>();

        final int[] i = {0};
        BiConsumer<Integer, TreeMap<Integer, Edge>> vertexMatrixer = (vertex, edges) -> {
            idToIndex.put(vertex, i[0]);
            adjacencyMatrix[i[0] + 1][0] = vertex;
            adjacencyMatrix[0][i[0] + 1] = vertex;
            edgesList.add(edges.values());
            i[0]++;
        };

        adjacencyList.forEach(vertexMatrixer);

        for (Collection<Edge> edges : edgesList) {
            for (Edge edge : edges) {
                int source = edge.getSource();
                int dest = edge.getDest();

                int sourceIndex = idToIndex.get(source);
                int destIndex = idToIndex.get(dest);

                adjacencyMatrix[sourceIndex + 1][destIndex + 1] = edge.getWeight();
            }
        }

        return adjacencyMatrix;
    }

    @Override
    public void printGraph() {
        System.out.println(getNumV());
        adjacencyList.forEach(((integer, integerEdgeTreeMap) -> integerEdgeTreeMap.forEach(((integer1, edge) -> System.out.println(edge)))));
    }

    @Override
    public int getNumV() {
        return vertices.size();
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public void insert(Edge edge) {
        addEdge(edge.getSource(), edge.getDest(), edge.getWeight());
    }

    @Override
    public boolean isEdge(int source, int dest) {
        if (!adjacencyList.containsKey(source)) return false;
        return adjacencyList.get(source).containsKey(dest);
    }

    @Override
    public Edge getEdge(int source, int dest) {
        if (!adjacencyList.containsKey(source)) throw new NoSuchVertex(source);
        return adjacencyList.get(source).get(dest);
    }

    /**
     * Iterates edges of given vertex in ascending order of weight.
     * @param source    The source vertex.
     * @return          Edge iterator.
     */
    @Override
    public Iterator<Edge> edgeIterator(int source) {
        if (adjacencyList.containsKey(source)) {
            return adjacencyList.get(source).values().iterator();
        }
        return Collections.emptyIterator();
    }

    private static class NoSuchVertex extends RuntimeException {
        private NoSuchVertex() {
            super("No such vertex exists.");
        }

        private NoSuchVertex(int vertexID) {
            super("No such vertex exists with ID " + vertexID + ".");
        }
    }
}
