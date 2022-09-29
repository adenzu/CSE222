import java.util.Iterator;

/**
 * Graph class from Data Structures with Java 2nd ED.
 */
public interface Graph {

    /**
     * Return the number of vertices.
     * @return  The number of vertices.
     */
    int getNumV();

    /**
     * Determine whether this is a directed graph.
     * @return  <code>true</code> if this is a directed graph.
     */
    boolean isDirected();

    /**
     * Insert a new edge into this graph.
     * @param edge  The new edge.
     */
    void insert(Edge edge);

    /**
     * Determine whether an edge exists.
     * @param source    The source vertex.
     * @param dest      The destination vertex.
     * @return          True if there is an edge from source to dest.
     */
    boolean isEdge(int source, int dest);

    /**
     * Get the edge between two vertices.
     * @param source    The source vertex.
     * @param dest      The destination vertex.
     * @return          The <code>Edge</code> between two vertices or an <code>Edge</code> with a weight
     *                  <code>Double.POSITIVE_INFINITY</code> if there's no edge.
     */
    Edge getEdge(int source, int dest);

    /**
     * Return an iterator to the edges connected
     * to given vertex.
     * @param source    The source vertex.
     * @return          An edge iterator to the vertices
 *                      connected to source.
     */
    Iterator<Edge> edgeIterator(int source);

    /**
     * Edge class that stores source vertex, destination vertex, and edge weight.
     */
    class Edge {
        private int source;
        private int dest;
        private double weight;

        /**
         * Constructs an Edge from source to dest. Sets the weight to 1.0.
         * @param source    Source vertex of edge.
         * @param dest      Destination vertex of edge.
         */
        public Edge(int source, int dest) {
            this(source, dest, 1);
        }

        /**
         * Constructs an Edge from source to dest. Sets the weight to w.
         * @param source    Source vertex of edge.
         * @param dest      Destination vertex of edge.
         * @param weight    Weight of edge.
         */
        public Edge(int source, int dest, double weight) {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

        /**
         * Compares two edges for equality. Edges are equal if their source
         * and destination vertices are the same. The weight is not considered.
         * @param obj   Object to be compared.
         * @return      <code>true</code> if equals.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Edge) {
                return ((Edge) obj).source == source && ((Edge) obj).dest == dest;
            }
            return false;
        }

        /**
         * @return  Source vertex.
         */
        public int getSource() {
            return source;
        }

        /**
         * @return  Destination vertex.
         */
        public int getDest() {
            return dest;
        }

        /**
         * @return  Weight.
         */
        public double getWeight() {
            return weight;
        }

        /**
         * Returns the hash code for an edge. The hash code depends only on
         * the source and destination.
         * @return  Hash code.
         */
        @Override
        public int hashCode() {
            return 2 * source + 3 * dest;
        }

        /**
         * @return  A string representation of the edge.
         */
        @Override
        public String toString() {
            return source + " " + dest + " " + weight;
        }
    }
}
