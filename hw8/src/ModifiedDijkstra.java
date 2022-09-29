import java.util.*;

/**
 * Modified Dijkstra's Algorithm for graphs whose vertices have 'boosting' property.
 *
 * Apart from first and last vertices from one vertex to another every vertex in between
 * decreases their leading edge's weight by their 'boosting' value.
 *
 * Self edges are not considered for paths in this algorithm.
 *
 * Boosting values are assumed to be always less than weight of every edge of that vertex.
 *
 * Implemented with O(n^2logn) time-performance. Can go as fast as constant time.
 */
public class ModifiedDijkstra {

    private final int source;
    private HashMap<Integer, Double> minDistanceMap;
    private HashMap<Integer, Integer> leadingVertexMap;

    /**
     * Execute this method on given graph starting from given vertex.
     * @param graph     Graph to be traversed.
     * @param source    Vertex that will be traversed first.
     */
    public ModifiedDijkstra(MyGraph graph, DynamicGraph.Vertex source) {
        this.source = source.getId();
        execute(graph, source);
    }

    /**
     * Get the ID of starting vertex.
     * @return  ID of starting vertex.
     */
    public int getSource() {
        return source;
    }

    /**
     * Get minimum distance from <code>source</code> to given vertex.
     * @param vertexID  ID of the vertex whose min distance is wanted to be learned.
     * @return          Minimum distance from <code>source</code> to vertex with given <code>vertexID</code>.
     */
    public Double getMinDistance(int vertexID) {
        return minDistanceMap.get(vertexID);
    }

    /**
     * Get leading vertex of given vertex.
     * @param vertexID  ID of the vertex whose leading vertex is wanted to be learned.
     * @return          Leading vertex of vertex with given <code>vertexID</code>.
     */
    public int getLeadingVertex(int vertexID) {
        return leadingVertexMap.get(vertexID);
    }

    /**
     * Get every vertex that is traversed from <code>source</code> to given vertex.
     * @param vertexID  ID of vertex whose path is wanted to be learned.
     * @return          List of vertices that were traversed from <code>source</code> to given vertex in order.
     */
    public List<Integer> getLeadingVertices(int vertexID) {
        LinkedList<Integer> vertices = new LinkedList<>();

        while (vertexID != source) {
            vertices.addFirst(vertexID);
            vertexID = getLeadingVertex(vertexID);
        }
        vertices.addFirst(source);

        return vertices;
    }

    /**
     * @return Minimum distance map of vertices.
     */
    public HashMap<Integer, Double> getMinDistanceMap() {
        return minDistanceMap;
    }

    /**
     * @return Leading vertex map of vertices.
     */
    public HashMap<Integer, Integer> getLeadingVertexMap() {
        return leadingVertexMap;
    }

    /**
     * Basically the real function.
     * @param graph     Graph.
     * @param source    Source.
     * @see ModifiedDijkstra
     */
    private void execute(MyGraph graph, DynamicGraph.Vertex source) {

        // Distance array element class substitute
        class VertexWithDistance implements Comparable<VertexWithDistance> {
            private int id;
            private double distance;
            private double boosting;

            private VertexWithDistance(int id) {
                this(id, 0, 0);
            }

            private VertexWithDistance(int id, double distance, double boosting) {
                this.id = id;
                this.distance = distance;
                this.boosting = boosting;
            }

            private void setId(int id) {
                this.id = id;
            }

            private void setDistance(double distance) {
                this.distance = distance;
            }

            private int getId() {
                return id;
            }

            private double getDistance() {
                return distance;
            }

            @Override
            public int compareTo(VertexWithDistance o) {
                return Double.compare(distance, o.distance);
            }

            @Override
            public int hashCode() {
                return id;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof VertexWithDistance) return id == ((VertexWithDistance) obj).id;
                return false;
            }

            private static double boostingOf(DynamicGraph.Vertex vertex) {
                String boosting = vertex.getValue("boosting");
                return boosting == null ? 0 : Double.parseDouble(boosting);
            }
        }

        HashSet<Integer> traversedVertices = new HashSet<>(graph.getNumV());
        TreeSet<VertexWithDistance> nextVertices = new TreeSet<>();
        nextVertices.add(new VertexWithDistance(source.getId(), 0, VertexWithDistance.boostingOf(source)));

        minDistanceMap = new HashMap<>(graph.getNumV());
        leadingVertexMap = new HashMap<>(graph.getNumV());

        while (!nextVertices.isEmpty()) {
            VertexWithDistance currVertex = nextVertices.pollFirst();

            assert currVertex != null;
            int currVertexID = currVertex.getId();
            double distanceToCurrVertex = currVertex.getDistance();

            traversedVertices.add(currVertexID);
            Iterator<Graph.Edge> edgeIterator = graph.edgeIterator(currVertexID);

            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();

                int dest = edge.getDest();

                if (traversedVertices.contains(dest)) continue;

                double weight = edge.getWeight();

                Double minDistance = minDistanceMap.get(dest);

                if (minDistance == null) {
                    minDistanceMap.put(dest, weight);
                    leadingVertexMap.put(dest, currVertexID);

                    double destBoosting = VertexWithDistance.boostingOf(graph.getVertex(dest));
                    nextVertices.add(new VertexWithDistance(dest, weight, destBoosting));
                }
                else {
                    double alternativeDistance = weight + distanceToCurrVertex - currVertex.boosting;

                    if (minDistance > alternativeDistance) {
                        minDistanceMap.put(dest, alternativeDistance);
                        leadingVertexMap.put(dest, currVertexID);

                        double destBoosting = VertexWithDistance.boostingOf(graph.getVertex(dest));
                        VertexWithDistance updatedDest = new VertexWithDistance(dest, alternativeDistance, destBoosting);

                        nextVertices.remove(updatedDest);
                        nextVertices.add(updatedDest);
                    }
                }
            }
        }
    }
}
