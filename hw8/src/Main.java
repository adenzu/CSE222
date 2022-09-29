import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Main class.
 */
public class Main {
    /**
     * Main function.
     * @param args  args
     */
    public static void main(String[] args) {
        print("Please increase stack size, 16MB was tested and was found enough.");
        print("Proceeding without incrementing it will likely result in stack overflow");
        print("due to DFS search which is implemented recursively.");
        print("Press enter to proceed...");
        new Scanner(System.in).nextLine();

        print("Created non-directed graph.\n");
        MyGraph undirectedGraph = new MyGraph();

        int vertexCount = 1000;

        addUniqueVertices(undirectedGraph, 0, vertexCount);
        addUniqueVertices(undirectedGraph, vertexCount, 10 * vertexCount);
        addUniqueVertices(undirectedGraph, 11 * vertexCount, 100 * vertexCount);

        print("Number of vertices in graph: " + undirectedGraph.getNumV() + "\n");

        print("Adding a vertex with id that already exists in graph.");
        undirectedGraph.addVertex(new DynamicGraph.Vertex(0));
        print("Number of vertices in graph: " + undirectedGraph.getNumV() + "\n");

        print("Adding edges from every vertex to other 10 random vertices with the weight of destID/(sourceID + 1)...");
        timer(() -> {
            for (int i = 0; i < undirectedGraph.getNumV(); i++) {
                for (int j = 0; j < 10; j++) {
                    int destID = ThreadLocalRandom.current().nextInt(0, undirectedGraph.getNumV());
                    undirectedGraph.addEdge(i, destID, (double) destID / (i + 1));
                }
            }
        });

        print("Now modified BFS will be executed on this graph with source vertex 1...");
        double BFSDistance = timer(() -> BFSTotalDistance(undirectedGraph, 0));
        print("The total distance traversed by modified BFS: " + BFSDistance + "\n");

        print("Now modified DFS will be executed on this graph with source vertex 0...");
        double DFSDistance = timer(() -> DFSTotalDistance(undirectedGraph, 0));
        print("The total distance traversed by modified DFS: " + DFSDistance + "\n");

        print("Difference in traversed distances are: " + BFSDistance + "-" + DFSDistance + "=" + (BFSDistance - DFSDistance));

        print("Iterating over edges of first 5 vertices...");
        for(int i = 0; i < 5; i++){
            Iterator<Graph.Edge> edgeIterator = undirectedGraph.edgeIterator(i);
            print("Edges of vertex with id: " + i);
            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();
                print(edge);
            }
            print();
        }

        print("Adding an edge from 0 to 5 with weight 8008135.");
        undirectedGraph.addEdge(0, 5, 8008135);

        print("Iterating and removing edges of second 5 vertices...");
        for(int i = 5; i < 10; i++){
            Iterator<Graph.Edge> edgeIterator = undirectedGraph.edgeIterator(i);
            LinkedList<Graph.Edge> edges = new LinkedList<>();

            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();
                edges.add(edge);
            }

            for (Graph.Edge edge : edges) {
                print("Removing " + edge);
                undirectedGraph.removeEdge(edge.getSource(), edge.getDest());
            }

            print();
        }

        if (undirectedGraph.getEdge(0, 5) == null) {
            print("Edge from 0 to 5 is deleted.\n");
        }

        print("Removing half of vertices...");
        timer(() -> {
            for (int i = undirectedGraph.getNumV() / 2 - 1; i >= 0; i--) {
                undirectedGraph.removeVertex(i * 2);
            }
        });

        print("Number of vertices now: " + undirectedGraph.getNumV() + "\n");

        print("Add that many new vertices with label 'freedom' to it...");
        timer(() -> {
            for (int i = undirectedGraph.getNumV() - 1; i >= 0; i--) {
                undirectedGraph.addVertex(new DynamicGraph.Vertex(i * 2, "freedom", 0));
            }
        });

        print("Number of vertices now: " + undirectedGraph.getNumV() + "\n");

        print("Remove indexes with label 'ease' (there won't be successful deletion due to not having such vertex)...");
        timer(() -> undirectedGraph.removeVertex("ease"));

        print("Number of vertices now: " + undirectedGraph.getNumV() + "\n");

        print("Remove indexes with label 'freedom'...");
        timer(() -> undirectedGraph.removeVertex("freedom"));

        print("Number of vertices now: " + undirectedGraph.getNumV() + "\n");

        print("Add new vertices to get to old vertex count...");
        timer(() -> {
            for (int i = undirectedGraph.getNumV() - 1; i >= 0; i--) {
                undirectedGraph.addVertex(new DynamicGraph.Vertex(i * 2));
            }
        });

        print("Number of vertices now: " + undirectedGraph.getNumV() + "\n");

        print("Filtering graph, this will return an empty graph since no vertex has any property...");
        MyGraph filtered = timer(() -> undirectedGraph.filterVertices("", ""));

        print("Number of vertices in filtered graph: " + filtered.getNumV() + "\n");

        print("Adding property 'p' to every vertex in graph with value of id % 5...");
        timer(() -> {
            for (int i = 0; i < undirectedGraph.getNumV(); i++) {
                undirectedGraph.getVertex(i).putProperty("p", Integer.toString(i % 5));
            }
        });

        print("Adding edges from 785 to both 384 and 385 before filtering just to make sure you see filtering works.");
        undirectedGraph.addEdge(785, 385, 0);
        undirectedGraph.addEdge(785, 384, 0);

        print("Filtering graph to get every vertex with value of 'p' that is equal to '0'...");
        filtered = timer(() -> undirectedGraph.filterVertices("p", "0"));

        print("Number of vertices in filtered graph: " + filtered.getNumV() + "\n");

        print("Iterating until finding a vertex that has any edge in filtered graph.");
        for (int i = 5; i < undirectedGraph.getNumV(); i += 10) {
            boolean success = false;
            Iterator<Graph.Edge> filteredIterator = filtered.edgeIterator(i);
            while (filteredIterator.hasNext()) {
                print(filteredIterator.next());
                success = true;
            }
            if (success) break;
        }
        print();

        print("Creating another graph which will be smaller to see the adjacency matrix.");
        MyGraph smol = new MyGraph();

        print("Adding 10 vertices.");
        for (int i = 0; i < 10; i++) smol.addVertex(new DynamicGraph.Vertex(i));

        print("Adding two random edges to every vertex whose weights are source*dest.");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                int dest = ThreadLocalRandom.current().nextInt(0, 10);
                smol.addEdge(i, dest, i * dest);
            }
        }

        print("Exporting adjacency matrix of small graph.");
        double[][] adjacencyMatrix = timer(smol::exportMatrix);

        print("Printing its adjacency matrix.");
        printMatrix(adjacencyMatrix, 10);

        print("The total distance traversed from vertex with ID 0 with BFS on last graph: " + BFSTotalDistance(smol, 0));

        print("Now same executions will be done for directed graphs.");
        print("Press enter to proceed.");
        new Scanner(System.in).nextLine();

        print("Created directed graph.\n");
        MyGraph directedGraph = new MyGraph();

        addUniqueVertices(directedGraph, 0, vertexCount);
        addUniqueVertices(directedGraph, vertexCount, 10 * vertexCount);
        addUniqueVertices(directedGraph, 11 * vertexCount, 100 * vertexCount);

        print("Number of vertices in graph: " + directedGraph.getNumV() + "\n");

        print("Adding a vertex with id that already exists in graph.");
        directedGraph.addVertex(new DynamicGraph.Vertex(0));
        print("Number of vertices in graph: " + directedGraph.getNumV() + "\n");

        print("Adding edges from every vertex to other 10 random vertices with the weight of destID/(sourceID + 1)...");
        timer(() -> {
            for (int i = 0; i < directedGraph.getNumV(); i++) {
                for (int j = 0; j < 10; j++) {
                    int destID = ThreadLocalRandom.current().nextInt(0, directedGraph.getNumV());
                    directedGraph.addEdge(i, destID, (double) destID / (i + 1));
                }
            }
        });

        print("Now modified BFS will be executed on this graph with source vertex 0...");
        BFSDistance = timer(() -> BFSTotalDistance(directedGraph, 0));
        print("The total distance traversed by modified BFS: " + BFSDistance + "\n");

        print("Now modified DFS will be executed on this graph with source vertex 0...");
        DFSDistance = timer(() -> DFSTotalDistance(directedGraph, 0));
        print("The total distance traversed by modified DFS: " + DFSDistance + "\n");

        print("Difference in traversed distances are: " + BFSDistance + "-" + DFSDistance + "=" + (BFSDistance - DFSDistance));

        print("Iterating over edges of first 5 vertices...");
        for(int i = 0; i < 5; i++){
            Iterator<Graph.Edge> edgeIterator = directedGraph.edgeIterator(i);
            print("Edges of vertex with id: " + i);
            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();
                print(edge);
            }
            print();
        }

        print("Adding an edge from 0 to 5 with weight 8008135.");
        directedGraph.addEdge(0, 5, 8008135);

        print("Iterating and removing edges of second 5 vertices...");
        for(int i = 5; i < 10; i++){
            Iterator<Graph.Edge> edgeIterator = directedGraph.edgeIterator(i);
            LinkedList<Graph.Edge> edges = new LinkedList<>();

            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();
                edges.add(edge);
            }

            for (Graph.Edge edge : edges) {
                print("Removing " + edge);
                directedGraph.removeEdge(edge.getSource(), edge.getDest());
            }

            print();
        }

        if (directedGraph.getEdge(0, 5) == null) {
            print("Edge from 0 to 5 is deleted.\n");
        }

        print("Removing half of vertices...");
        timer(() -> {
            for (int i = directedGraph.getNumV() / 2 - 1; i >= 0; i--) {
                directedGraph.removeVertex(i * 2);
            }
        });

        print("Number of vertices now: " + directedGraph.getNumV() + "\n");

        print("Add that many new vertices with label 'freedom' to it...");
        timer(() -> {
            for (int i = directedGraph.getNumV() - 1; i >= 0; i--) {
                directedGraph.addVertex(new DynamicGraph.Vertex(i * 2, "freedom", 0));
            }
        });

        print("Number of vertices now: " + directedGraph.getNumV() + "\n");

        print("Remove indexes with label 'ease' (there won't be successful deletion due to not having such vertex)...");
        timer(() -> directedGraph.removeVertex("ease"));

        print("Number of vertices now: " + directedGraph.getNumV() + "\n");

        print("Remove indexes with label 'freedom'...");
        timer(() -> directedGraph.removeVertex("freedom"));

        print("Number of vertices now: " + directedGraph.getNumV() + "\n");

        print("Add new vertices to get to old vertex count...");
        timer(() -> {
            for (int i = directedGraph.getNumV() - 1; i >= 0; i--) {
                directedGraph.addVertex(new DynamicGraph.Vertex(i * 2));
            }
        });

        print("Number of vertices now: " + directedGraph.getNumV() + "\n");

        print("Filtering graph, this will return an empty graph since no vertex has any property...");
        filtered = timer(() -> directedGraph.filterVertices("", ""));

        print("Number of vertices in filtered graph: " + filtered.getNumV() + "\n");

        print("Adding property 'p' to every vertex in graph with value of id % 5...");
        timer(() -> {
            for (int i = 0; i < directedGraph.getNumV(); i++) {
                directedGraph.getVertex(i).putProperty("p", Integer.toString(i % 5));
            }
        });

        print("Adding edges from 785 to both 384 and 385 before filtering just to make sure you see filtering works.");
        directedGraph.addEdge(785, 385, 0);
        directedGraph.addEdge(785, 384, 0);

        print("Filtering graph to get every vertex with value of 'p' that is equal to '0'...");
        filtered = timer(() -> directedGraph.filterVertices("p", "0"));

        print("Number of vertices in filtered graph: " + filtered.getNumV() + "\n");

        print("Iterating until finding a vertex that has any edge in filtered graph.");
        for (int i = 5; i < directedGraph.getNumV(); i += 10) {
            boolean success = false;
            Iterator<Graph.Edge> filteredIterator = filtered.edgeIterator(i);
            while (filteredIterator.hasNext()) {
                print(filteredIterator.next());
                success = true;
            }
            if (success) break;
        }
        print();

        print("Creating another graph which is smaller to see the adjacency matrix.");
        smol = new MyGraph(true);

        print("Adding 10 vertices.");
        for (int i = 0; i < 10; i++) smol.addVertex(new DynamicGraph.Vertex(i));

        print("Adding two random edges to every vertex whose weights are source*dest.");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                int dest = ThreadLocalRandom.current().nextInt(0, 10);
                smol.addEdge(i, dest, i * dest);
            }
        }

        print("Exporting adjacency matrix of small graph.");
        adjacencyMatrix = timer(smol::exportMatrix);

        print("Printing its adjacency matrix.");
        printMatrix(adjacencyMatrix, 10);

        print("The total distance traversed from vertex with ID 0 with BFS on last graph: " + BFSTotalDistance(smol, 0));

        print("BFS and DFS methods will tested next, press enter to continue...");
        new Scanner(System.in).nextLine();

        print("Creating a graph with 6 vertices to show BFS takes the shorter path between levels.");
        print("But doesn't traverses from level n to n-1.");
        smol = new MyGraph();

        smol.addVertex(new DynamicGraph.Vertex(0));
        smol.addVertex(new DynamicGraph.Vertex(1));
        smol.addVertex(new DynamicGraph.Vertex(2));
        smol.addVertex(new DynamicGraph.Vertex(3));
        smol.addVertex(new DynamicGraph.Vertex(4));
        smol.addVertex(new DynamicGraph.Vertex(5));

        // Shortest path from 0 to 5
        smol.addEdge(0, 1, 1);
        smol.addEdge(1, 2, 1);
        smol.addEdge(2, 5, 1);

        smol.addEdge(0, 3, 54);
        smol.addEdge(3, 5, 1);  // Shortest edge vertex 5 has with vertices one level before it

        // Shortest path without decrementing
        smol.addEdge(0, 4, 35);
        smol.addEdge(4, 5, 10);

        print("Adjacency matrix of created graph:");
        printMatrix(smol.exportMatrix(), 10);

        print("Result of printGraph:");
        smol.printGraph();
        print();

        print("Now modified BFS will be done starting from vertex 0.");
        print("But even though shortest path to vertex 5 is 0->1->2->5 it is not traversed that way.");
        print("This is because BFS traverses nodes in only incremental level order. And 5 is in level 2 whilst 2 is in 3.");
        print("So instead 0->4->5 is traversed for it is shorter than 0->3->5.");

        print("Now see the total distance traversed by modified BFS: " + BFSTotalDistance(smol, 0) + "\n");

        print("Now modified DFS will be done on last graph starting from vertex 0.");
        print("The total distance traversed by modified DFS: " + DFSTotalDistance(smol, 0) + "\n");

        print("Press enter to proceed to Modified Dijkstra Tests...");
        new Scanner(System.in).nextLine();

        print("It will be done first for undirected graph and then directed graph which are the ones we created above.");
        print("Both will be done starting from vertex 1.");
        print();
        print("Calculating for undirected graph...");
        ModifiedDijkstra undirectedDijkstra = timer(() -> new ModifiedDijkstra(undirectedGraph, undirectedGraph.getVertex(1)));

        print("Path that is taken to access to 97 is:");
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer i : undirectedDijkstra.getLeadingVertices(97)) {
            stringBuilder.append(i).append("->");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        print(stringBuilder.toString());

        print("With distance of: " + undirectedDijkstra.getMinDistance(97));

        print("\nCalculating for directed graph...");
        ModifiedDijkstra directedDijkstra = timer(() -> new ModifiedDijkstra(directedGraph, directedGraph.getVertex(1)));

        print("Path that is taken to access to 97 is:");
        stringBuilder = new StringBuilder();

        for (Integer i : directedDijkstra.getLeadingVertices(97)) {
            stringBuilder.append(i).append("->");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        print(stringBuilder.toString());

        print("With distance of: " + directedDijkstra.getMinDistance(97));

        print("\nFrom now on edge cases will be shown.\nPress enter to proceed...");
        new Scanner(System.in).nextLine();
        print();
        print("Trying adding edge from 10 to 11 in last graph. There are no vertices with these IDs.");
        try {
            smol.addEdge(10, 11, 0);
        } catch (Exception e) {
            print("Exception message:");
            print(e.getMessage());
        }
        print();
        print("Trying adding edge from 0 to 11 in last graph. There is no vertex with ID 11.");
        try {
            smol.addEdge(0, 11, 0);
        } catch (Exception e) {
            print("Exception message:");
            print(e.getMessage());
        }
        print();
        print("Trying getting the edge between 10 and 11. There are no vertices with these IDs");
        try {
            smol.getEdge(10, 11);
        } catch (Exception e) {
            print("Exception message:");
            print(e.getMessage());
        }
        print();
        print("However trying getting edge between 0 and 11 won't be a problem and simply return null.");
        print("Result: " + smol.getEdge(0, 11));
        print();
        print("These are the only methods that throw exceptions. Since if there's no vertex or edge to remove from");
        print("graph, it is in the wanted form anyway and no exception is needed in removal methods.");
        print("And for edge iteration method it returns an empty iterator if given vertex is not found, etc.");
    }

    /**
     * Runs given function and prints taken time.
     * @param function  Function to be run.
     */
    private static void timer(Runnable function) {
        long st = System.currentTimeMillis();
        function.run();
        print("It took " + (System.currentTimeMillis() - st) + " milliseconds.\n");
    }

    /**
     * Runs given function and prints taken time.
     * @param function  Function to be run.
     * @return          The result of the function.
     * @param <E>       The return type of function.
     */
    private static<E> E timer(Supplier<E> function) {
        long st = System.currentTimeMillis();
        E e = function.get();
        print("It took " + (System.currentTimeMillis() - st) + " milliseconds.\n");
        return e;
    }

    /**
     * Adds <code>count</code> many vertices with ids starting from <code>start</code>.
     * @param graph Graph to add vertices.
     * @param start Starting id value.
     * @param count Number of vertices to be added.
     */
    private static void addUniqueVertices(DynamicGraph graph, int start, int count) {
        print("Adding " + count + " unique vertices to graph...");
        timer(() -> {
            for (int i = 0; i < count; i++) {
                graph.addVertex(new DynamicGraph.Vertex(i + start));
            }
        });
    }

    /**
     * Prints new line.
     */
    private static void print() {
        System.out.println();
    }

    /**
     * Prints.
     * @param o Printed.
     */
    private static void print(Object o) {
        System.out.println(o);
    }

    /**
     * Fancy prints given double matrix.
     * @param matrix    Double matrix to be printed.
     * @param padding   Width of each column.
     */
    private static void printMatrix(double[][] matrix, int padding) {
        for (double[] arr : matrix) {
            StringBuilder line = new StringBuilder();
            for (double item : arr) {
                String itemStr = Double.toString(item);
                line.append(itemStr).append(" ".repeat(padding - itemStr.length()));
            }
            System.out.println(line);
        }
        System.out.println();
    }

    private static double BFSTotalDistance(Graph graph, int source) {
        double totalDistance = 0;

        HashMap<Integer, Integer> vertexLevel = new HashMap<>(graph.getNumV());
        HashMap<Integer, Double> shortestDistance = new HashMap<>(graph.getNumV());
        HashMap<Integer, Double> leadingEdgeWeight = new HashMap<>(graph.getNumV());

        Queue<Integer> vertices = new LinkedList<>();
        vertices.add(source);
        vertexLevel.put(source, 0);
        shortestDistance.put(source, 0.0);
        leadingEdgeWeight.put(source, 0.0);

        while (!vertices.isEmpty()) {
            int currVertex = vertices.poll();
            int currLevel = vertexLevel.get(currVertex);

            double distanceToCurrVertex = shortestDistance.get(currVertex);
            totalDistance += leadingEdgeWeight.get(currVertex);

            Iterator<Graph.Edge> edgeIterator = graph.edgeIterator(currVertex);

            while (edgeIterator.hasNext()) {
                Graph.Edge edge = edgeIterator.next();

                int dest = edge.getDest();
                double weight = edge.getWeight();
                double distance = distanceToCurrVertex + weight;

                Integer destLevel = vertexLevel.get(dest);

                if (destLevel == null) {
                    shortestDistance.put(dest, distance);
                    vertexLevel.put(dest, currLevel + 1);
                    leadingEdgeWeight.put(dest, weight);
                    vertices.add(dest);
                }
                else if (destLevel > currLevel && shortestDistance.get(dest) > distance) {
                    shortestDistance.put(dest, distance);
                    leadingEdgeWeight.put(dest, weight);
                }
            }
        }

        return totalDistance;
    }

    private static double DFSTotalDistance(MyGraph graph, int source) {
        HashSet<Integer> visitedVertices = new HashSet<>(graph.getNumV());
        return DFSTotalDistance(graph, source, visitedVertices);
    }

    private static double DFSTotalDistance(MyGraph graph, int source, HashSet<Integer> visitedVertices) {
        double totalDistance = 0;

        visitedVertices.add(source);

        Iterator<Graph.Edge> edgeIterator = graph.edgeIterator(source);

        while (edgeIterator.hasNext()) {
            Graph.Edge edge = edgeIterator.next();

            int dest = edge.getDest();

            if (visitedVertices.contains(dest)) continue;

            totalDistance += edge.getWeight() + DFSTotalDistance(graph, edge.getDest(), visitedVertices);
        }

        return totalDistance;
    }

    private static double SearchDifference(MyGraph graph, int source) {
        return BFSTotalDistance(graph, source) - DFSTotalDistance(graph, source);
    }
}