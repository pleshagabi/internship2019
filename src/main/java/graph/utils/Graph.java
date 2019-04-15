package graph.utils;

import java.util.ArrayList;
import java.util.List;

// class to represent a graph object
public class Graph
{
    // An array of Lists to represent adjacency list
    public List<List<Integer>> adjList = null;

    // Constructor
    public Graph(List<Edge> edges, int N)
    {
        adjList = new ArrayList<List<Integer>>(N);

        for (int i = 0; i < N; i++) {
            adjList.add(i, new ArrayList<Integer>());
        }

        // add edges to the undirected graph
        for (int i = 0; i < edges.size(); i++)
        {
            int src = edges.get(i).source;
            int dest = edges.get(i).dest;

            adjList.get(src).add(dest);
            adjList.get(dest).add(src);
        }
    }
}
