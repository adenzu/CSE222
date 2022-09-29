import java.util.HashMap;

/**
 * Graph implementation which enables dynamic size of vertices.
 */
public interface DynamicGraph extends Graph{

    /**
     * Adds given vertex to this graph. If a vertex with same id already existed in this graph, overrides its
     * label and weight.
     * @param vertex    Vertex to be added.
     */
    void addVertex(Vertex vertex);

    /**
     * Adds an edge between given vertices.
     * @param vertexID1 First vertex id.
     * @param vertexID2 Second vertex id.
     * @param weight    Weight of edge.
     */
    void addEdge(int vertexID1, int vertexID2, double weight);

    /**
     * Removes the edge between given vertices.
     * @param vertexID1 First vertex id.
     * @param vertexID2 Second vertex id.
     */
    void removeEdge(int vertexID1, int vertexID2);

    /**
     * Get the vertex in this graph with given ID.
     * @param vertexID  ID of the vertex to be found.
     * @return          Vertex in this graph with given ID, or <code>null</code> if no such vertex exists.
     */
    Vertex getVertex(int vertexID);

    /**
     * Removes vertex with given id from this graph.
     * @param vertexID  ID of the vertex to be removed.
     */
    void removeVertex(int vertexID);

    /**
     * Removes vertex with given label.
     * @param label Label of vertex to be removed.
     */
    void removeVertex(String label);

    /**
     * Filter the vertices by the given user-defined
     * property and returns a subgraph of the graph.
     * @param key       Property name/key.
     * @param filter    Property value.
     * @return          Filtered <code>MyGraph</code>;
     */
    MyGraph filterVertices(String key, String filter);

    /**
     * Constructs a double matrix with its first row and column showing vertex ids.
     * The value at <code>(0, 0)</code> is always <b>0.0</b> and <code>Infinity</code> is used to represent no edge.
     * <pre>
// Example
MyGraph graph = new MyGraph();
graph.addVertex(new Vertex(54));
graph.addVertex(new Vertex(41));
graph.addEdge(54, 41, 13.5);

// The matrix will be as
// 0.0    54.0        41.0
// 54.0   Infinity    13.5
// 41.0   Infinity    Infinity
     *  </pre>
     *
     *
     * @return  Adjacency matrix of this graph.
     */
    double[][] exportMatrix();

    /**
     * Prints this graph in adjacency list format.
     *
     * TODO:
     * AbstractGraph importing style printing needed!!!
     */
    void printGraph();

    /**
     * Class to represent vertices in <code>DynamicGraph</code>.
     */
    class Vertex {

        private int id;
        private String label;
        private double weight;
        private HashMap<String, String> propertyMap;

        /**
         * Constructs new Vertex with given ID, and <code>""</code> and <code>0</code> as its label and weight respectively.
         * @param id    ID of this vertex.
         */
        public Vertex(int id) {
            this(id, "", 0);
        }

        /**
         * Construct a <code>Vertex</code> with given id, weight, and label.
         * @param id        ID of this vertex.
         * @param weight    Weight of this vertex.
         * @param label     Label of this vertex.
         */
        public Vertex(int id, String label, double weight) {
            this.id = id;
            this.weight = weight;
            this.label = label;
            propertyMap = new HashMap<>();
        }

        /**
         * Puts given property, returns the prior value of given key, if there was none returns <code>null</code>.
         * @param key   Value to be put corresponding to.
         * @param value Value to be put.
         * @return      Prior value of given key, <code>null</code> if there was none.
         */
        public String putProperty(String key, String value) {
            return propertyMap.put(key, value);
        }

        /**
         * Removes given property.
         * @param key   Property to be deleted.
         * @return      Value of the deleted property, <code>null</code> if property didn't exist.
         */
        public String removeProperty(String key) {
            return propertyMap.remove(key);
        }

        /**
         * Returns vertex's property value.
         * @param key   Property name.
         * @return      Value of this vertex's given property. <code>null</code> if no such property existed.
         */
        public String getValue(String key) {
            return propertyMap.get(key);
        }

        /**
         * @return  ID of this vertex.
         */
        public int getId() {
            return id;
        }

        /**
         * @return  Label of this vertex.
         */
        public String getLabel() {
            return label;
        }

        /**
         * @return  Weight of this vertex.
         */
        public double getWeight() {
            return weight;
        }

        /**
         * @return  ID of this vertex.
         */
        @Override
        public int hashCode() {
            return id;
        }

        /**
         * @param obj   Other object to be compared.
         * @return      <code>true</code> if both have same IDs, <code>false</code> otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Vertex) return id == ((Vertex) obj).id;
            return false;
        }
    }
}
