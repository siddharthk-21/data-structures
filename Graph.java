import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 
 */
@SuppressWarnings("unused")
public abstract class Graph<V,E> {

    // the list of nodes
    private LinkedList<Vertex<V>> nodes;

    // unique identifiers
    private static int vid, eid;

    /**
     * Constructor for a graph
     */
    public Graph() {
        vid = 0; eid = 0;
    }

    /**
     * Whether the graph is a graph with no vertices or not
     * @return true if graph has no vertices
     */
    public boolean isNull() {
        return nodes.isEmpty();
    }

    /**
     * 
     * @return
     */
    public Vertex<V> addVertex() {
        Vertex<V> v = new Vertex<V>();
        if (nodes.add(v))
            return v;
        else return null;
    }

    /**
     * 
     * @return
     */
    public Vertex<V> getVertex(V obj) {
        Vertex<V> v = null;
        for (Vertex<V> node : nodes) {
            if (node.name.equals(obj)) {
                v = node;
                break;
            }
        }
        if (v != null) 
            return v;
        else throw new NoSuchElementException("the vertex you are looking for DNE");
    }

    /**
     * 
     * @return
     */
    public ArrayList<Vertex<V>> getAllVertices() {
        return new ArrayList<Vertex<V>>(nodes);
    }

    /**
     * 
     * @return
     */
    public Vertex<V> getVertex(int id) {
        try {
            return nodes.get(nodes.indexOf(new Vertex<V>(id)));
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("the vertex you are looking for DNE");
        }
    }

    /**
     * 
     * @param origin
     * @param destination
     * @return
     */
    public Edge<E> addEdge(int origin, int destination) {
        try {
            return addEdge(nodes.get(nodes.indexOf(new Vertex<V>(origin))), nodes.get(nodes.indexOf(new Vertex<V>(destination))));
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("either the origin or the destination of this edge does not exist");
        }
    }

    /**
     * 
     * @param origin
     * @param destination
     * @return
     */
    public Edge<E> addEdge(Vertex<V> origin, Vertex<V> destination) {
        if (nodes.contains(origin) && nodes.contains(destination))
            return origin.connect(destination);
        else throw new NoSuchElementException("either the origin or the destination of this edge does not exist");
    }

    /**
     * 
     * @param origin
     * @param destination
     * @return
     */
    public Edge<E> addEdge(V origin, V destination) {
        Vertex<V> o = null, d = null;
        for (Vertex<V> node : nodes) {
            if (node.name.equals(origin))
                o = node;
            if (node.name.equals(destination))
                d = node;
        }
        if (o != null && d != null)
            return addEdge(o, d);
        else throw new NoSuchElementException("either the origin or the destination of this edge does not exist");
    }

    /**
     * 
     * @param origin
     * @param destination
     * @return
     */
    public ArrayList<Edge<E>> getEdges(Vertex<V> origin, Vertex<V> destination) {
        ArrayList<Edge<E>> array = origin.getEdges(destination);
        if (array.isEmpty())
            throw new NoSuchElementException("the edges you are looking for DNE");
        else return array;
    }

    /**
     * 
     * @param origin
     * @param destination
     * @return
     */
    public Edge<E> getEdge(Vertex<V> origin, Vertex<V> destination) {
        ArrayList<Edge<E>> array = getEdges(origin, destination);
        if (array.size() == 1)
            return array.get(0);
        else if (array.isEmpty())
            throw new NoSuchElementException("the edge you are looking for DNE");
        else
            throw new WrongMethodTypeException("you should use method getEdges() instead because there are multiple edges between the origin and destination vertices");
    }

    /**
     * The size of the graph
     * @return the number of vertices in the graph
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Figures out how connected the graph is
     * @return the number of edges in the graph
     */
    public int connectedness() {
        int sum = 0;
        for (Vertex<V> v : nodes) {
            sum += v.degree();
        }
        return sum;
    }

    /**
     * Converts this implementation of a graph to a matrix format. If there are 
     * multiple edges from node a to node b, the matrix will throw an exception.
     * The first index indicates the origin vertex, while the second index indicates
     * the destination vertex. Null values indicate no edge is present.
     * @return an Integer matrix version of this graph
     */
    public Integer[][] matrix() {
        Integer[][] matrix = new Integer[size()][size()];

        for (Vertex<V> node : nodes) {
            boolean[] visited = new boolean[size()];
            for (Edge<E> edge : node.edges) {
                if (!visited[edge.destination.id]) {
                    matrix[node.id][edge.destination.id] = Integer.valueOf((int)edge.weight);
                    visited[edge.destination.id] = true;
                } else throw new IllegalArgumentException("vertex "+node.id+" has multiple edges pointing to vertex "+edge.destination.id);
            }
        }
        return matrix;
    }


    /*
     * 
     */
    private class Vertex<V> {

        // list of all edges from this vertex
        private LinkedList<Edge<E>> edges;

        // identifier of vertex
        private V name;
        private final int id;

        /**
         * 
         */
        private Vertex() {
            name = null;
            id = vid++;
        }

        /**
         * 
         * @param name
         */
        private Vertex(V name) {
            this.name = name;
            id = vid++;
        }

        /**
         * 
         */
        private Vertex(int id) {
            this.id = id;
        }

        /**
         * 
         */
        public Vertex<V> defineVertex(V obj) {
            name = obj;
            return this;
        }

        /**
         * 
         * @return
         */
        public boolean delete() {
            return nodes.remove(this);
        }

        /**
         * 
         * @return
         */
        public ArrayList<Edge<E>> getEdges(Vertex<V> destination) {
            ArrayList<Edge<E>> array = new ArrayList<>();
            for (Edge<E> edge : edges) {
                if (edge.destination.equals(destination))
                    array.add(edge);
            }
            return array;
        }

        /**
         * 
         * @return
         */
        public Edge<E> getEdge(int id) {
            return edges.get(edges.indexOf(new Edge<E>(id)));
        }

        /**
         * 
         * @param v
         * @param name
         * @return
         */
        private Edge<E> connect(Vertex<V> v, E name, int weight) {
            Edge<E> e = connect(v, weight);
            e.name = name;
            return e;
        }

        /**
         * 
         * @param v
         * @param name
         * @return
         */
        private Edge<E> connect(Vertex<V> v, E name) {
            Edge<E> e = connect(v);
            e.name = name;
            return e;
        }

        /**
         * 
         * @param v
         * @param weight
         * @return
         */
        private Edge<E> connect(Vertex<V> v, int weight) {
            Edge<E> e = connect(v);
            e.weight = weight;
            return e;
        }

        /**
         * 
         * @param v
         * @return
         */
        public Edge<E> connect(Vertex<V> v) {
            Edge<E> e = new Edge(this, v);
            edges.add(e);
            return e;
        }

        /**
         * 
         * @param v
         * @return
         */
        public boolean disconnect(Vertex<V> v) {
            for (Edge<E> edge : edges) {
                if (edge.destination.equals(v))
                    disconnect(edge);
            }
            return true;
        }

        /**
         * 
         * @param e
         * @return
         */
        public boolean disconnect(Edge<E> e) {
            return edges.remove(e);
        }

        /**
         * 
         * @return
         */
        public int id() {
            return id;
        }

        /**
         * 
         * @return
         */
        public V definition() {
            return name;
        }

        /**
         * How many vertices are adjacent to this vertex
         * @return the out-degree of the graph
         */
        public int degree() {
            return edges.size();
        }

        /**
         * 
         * @param o
         * @return 
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Graph<?,?>.Vertex<?>) {
                Graph<?,?>.Vertex<?> v = (Graph<?,?>.Vertex<?>)o;
                return id == v.id;
            } else return false;
        }

    }

    /*
     * 
     */
    private class Edge<E> {

        // the origin and destination of this edge
        private Vertex<V> origin, destination;

        // the weight of this edge
        private double weight;

        // identifier of edge
        private E name;
        private final int id;

        /**
         * 
         * @param id
         */
        private Edge(int id) {
            weight = 0;
            origin = null;
            destination = null;
            name = null;
            this.id = id;
        }

        /**
         * 
         * @param weight
         * @param origin
         * @param destination
         */
        private Edge(E name, double weight, Vertex<V> origin, Vertex<V> destination) {
            this.weight = weight;
            this.origin = origin;
            this.destination = destination;
            this.name = name;
            id = eid++;
        }

        /**
         * 
         * @param origin
         * @param destination
         */
        private Edge(E name, Vertex<V> origin, Vertex<V> destination) {
            this.origin = origin;
            this.destination = destination;
            weight = 0;
            this.name = name;
            id = eid++;
        }

        /**
         * 
         * @param origin
         * @param destination
         */
        private Edge(double weight, Vertex<V> origin, Vertex<V> destination) {
            this.origin = origin;
            this.destination = destination;
            this.weight = weight;
            name = null;
            id = eid++;
        }

        /**
         * 
         * @param origin
         * @param destination
         */
        private Edge(Vertex<V> origin, Vertex<V> destination) {
            this.origin = origin;
            this.destination = destination;
            weight = 0;
            name = null;
            id = eid++;
        }

        /**
         * 
         * @param weight
         * @return
         */
        public Edge<E> weightEdge(double weight) {
            this.weight = weight;
            return this;
        }

        /**
         * 
         * @param obj
         * @return
         */
        public Edge<E> defineEdge(E obj) {
            name = obj;
            return this;
        }

        /**
         * 
         * @param destination
         * @return
         */
        public Edge<E> destinationEdge(Vertex<V> destination) {
            this.destination = destination;
            return this;
        }

        /**
         * 
         * @return
         */
        public boolean delete() {
            return this.origin.edges.remove(this);
        }

        /**
         * 
         * @return
         */
        public int id() {
            return id;
        }

        /**
         * 
         * @return
         */
        public E definition() {
            return name;
        }

        /**
         * 
         * @return
         */
        public Vertex<V> origin() {
            return origin;
        }

        /**
         * 
         * @return
         */
        public Vertex<V> destination() {
            return destination;
        }

        /**
         * 
         * @return
         */
        public double weight() {
            return weight;
        }

        /**
         * 
         * @param o
         * @return 
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Graph<?,?>.Edge<?>) {
                Graph<?,?>.Edge<?> e = (Graph<?,?>.Edge<?>)o;
                return id == e.id;
            } else return false;
        }

    }

}