package finlab;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Utility {

    public Graph parseCSV(File file) throws InvalidDataFileException {
        Graph graph = new Graph();
        Integer[][] adjacencyMatrix;
        List<String> vertices = new ArrayList<>();
        List<List<Integer>> csv = new ArrayList<>();
        List<Integer> csvIntTemp;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] values;

            // Segment to determine if Graph is Directed or Undirected
            // New Graph Instances are directed by default
            String line = br.readLine();    // read first line
            if (line.contains("UN") || line.contains("un") || line.contains("Un")) graph.setDirected(false);

            line = br.readLine();   // read second line
            values = line.split(",");   // read the vertex list
            vertices = Arrays.asList(values);

            // succeeding lines in csv are now the adjacency matrix itself
            while((line = br.readLine()) != null) {
                csvIntTemp = new ArrayList<>();
                values = line.split(",");
                for (String x : values) csvIntTemp.add(Integer.parseInt(x));
                csv.add(csvIntTemp);
            }

        } catch (Exception ignored) {
            throw new InvalidDataFileException("Invalid CSV file.");
        }



        // Construct the matrix
        List<Integer[]> list = new ArrayList<>();
        for (List<Integer> l : csv) {
            Integer[] integers = l.toArray(Integer[]::new);
            list.add(integers);
        }
        adjacencyMatrix = list.toArray(new Integer[0][]);

        // Check for errors
        if (vertices.size() != csv.get(0).size())
            throw new InvalidDataFileException("Invalid CSV file.");

        graph.setMatrix(adjacencyMatrix);
        graph.setVertexList(vertices);

        return graph;
    }

    public void depthTraversal
            (String startingVertex, Integer[][] matrix, List<String> vertices)
            throws InvalidVertexException {
        StringBuilder result = new StringBuilder();
        boolean[] visitedVertices = new boolean[matrix.length];

        int b=0, count=0;
        for (String a : vertices){
            if(!a.equals(startingVertex)){
                b++;
            }
            if(a.equals(startingVertex)){
                count = b;
                result.append(vertices.get(count));
                visitedVertices[count] = true;
            }
        }

        int counter = 1;
        while (counter < vertices.size()) {
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix[count][j] != -1) {
                    if (!visitedVertices[j]) {
                        result.append(vertices.get(j));
                        visitedVertices[j] = true;
                        count = j;
                        counter++;
                        j=0;
                    }
                }
            }

            count++;
            if (count > vertices.size()-1){
                count=0;
            }

        }
        System.out.print(result);
    }
 }
    /**
     * Asks input from user, gets traversal and prints returned value
     * or prints error message
     *
     * ALGORITHM
     *  Declare new StringBuilder
     *  Instantiate boolean as visited array
     *  Create a new queue for traversal method
     *  Enqueue visiteed node
     *  While queue is not empty
     *      visited vertex is equal to vertices index
     *      append value to StringBuilder
     *      For every adjacent vertex
     *          if matrix index is not equal to 1 and not visited  by vertex
     *          Enqueue vertices
     *  Print value of returned traversal
     */

    public void breadthTraversal
            (String startingVertex, Integer[][] matrix, List<String> vertices)
            throws InvalidVertexException {

        int inputIndex = vertices.indexOf(startingVertex);
        StringBuilder returnVal = new StringBuilder();

        // visited array
        boolean[] visited = new boolean[vertices.size()];
        Arrays.fill(visited, false);

        // Create a queue for BFT
        Queue<String> queue = new Queue<>();

        // Mark the current node as visited and enqueue it
        visited[inputIndex] = true;
        queue.enqueue(startingVertex);

        int visitedVertexIndex;
        while (!queue.isEmpty()) {
            visitedVertexIndex = vertices.indexOf(queue.poll());

            // Append to StringBuilder
            returnVal.append(vertices.get(visitedVertexIndex));

            // For every adjacent vertex
            for (int i = 0; i < vertices.size(); i++) {
                if (matrix[visitedVertexIndex][i] != -1 && (!visited[i])) {
                    queue.enqueue(vertices.get(i));
                    visited[i] = true;
                }
            }
        }

        System.out.println(returnVal);
    }


    public void determineShortestPath(Integer[][] matrix, int index, List<String> vertices) {

        int nVertices = matrix[0].length;

        // shortestDistances[i] will hold the
        // shortest distance from src to i
        int[] shortestDistances = new int[nVertices];

        // added[i] will true if vertex i is
        // included / in shortest path tree
        // or shortest distance from src to
        // i is finalized
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as
        // INFINITE and added[] as false
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        // Distance of source vertex from
        // itself is always 0
        shortestDistances[index] = 0;

        // Parent array to store shortest
        // path tree
        int[] parents = new int[nVertices];

        // The starting vertex does not
        // have a parent
        parents[index] = -1;

        // Find shortest path for all
        // vertices
        for (int i = 1; i < nVertices; i++) {

            // Pick the minimum distance vertex
            // from the set of vertices not yet
            // processed. nearestVertex is
            // always equal to startNode in
            // first iteration.
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as
            // processed
            added[nearestVertex] = true;

            // Update dist value of the
            // adjacent vertices of the
            // picked vertex.
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                int edgeDistance = matrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        int nVertices1 = shortestDistances.length;
        System.out.print("Vertex\t\t Distance\tPath");
        for (int vertexIndex = 0;
             vertexIndex < nVertices1;
             vertexIndex++)
        {
            if (vertexIndex != index)
            {
                System.out.print("\n" + vertices.get(index) + " -> ");
                System.out.print(vertices.get(vertexIndex) + " \t\t ");
                System.out.print(shortestDistances[vertexIndex] + "\t\t");
                printPath(vertexIndex, parents, vertices);
            }
        }
    }

    private static void printPath(int currentVertex, int[] parents, List<String> vertices) {
        // Base case : Source node has
        // been processed
        if (currentVertex == -1)
        {
            return;
        }
        printPath(parents[currentVertex], parents, vertices);
        System.out.print(vertices.get(currentVertex) + " ");
    }

}




