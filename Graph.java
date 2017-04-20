
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.TreeMap;
//import java.util.Collection;
//import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Graph
{
	public static final double INFINITY = Double.MAX_VALUE;
	private Map<String, Vertex> vertex_map = new TreeMap<String, Vertex>();
	
	// Builds graph while adding vertices from the network file
	public void build_graph(String source_name, String dest_name, double weight, boolean availability) {
		Vertex i = getVertex(source_name, availability);
		Vertex j = getVertex(dest_name, availability);
		i.adjacency.add(new Edge(i, j, weight, availability));
		j.adjacency.add(new Edge(j, i, weight, availability));
	}
	
	// Finds a vertex if doesnt exist, adds one
	private Vertex getVertex(String source_name, boolean availability) {
		Vertex p = vertex_map.get(source_name);
		if (p == null){
			p = new Vertex(source_name, availability);
			vertex_map.put(source_name, p);
			}
		
		return p;
	}
	
	// Adds a new weighted directed edge
	public void add_edge(String source, String dest, double weight, boolean availability){
		Vertex cur = getVertex(source, availability);
		Vertex des = getVertex(dest, availability);
		Iterator it = cur.adjacency.listIterator();
		while (it.hasNext()) {
			Edge edge = (Edge) it.next();
			if (dest.equals(edge.getDesti())) {
				it.remove();
			}
		}
		cur.adjacency.add(new Edge(cur, des, weight, availability));
		
	}
	
	// Deletes an edge
	public void del_edge(String source, String dest){
		Vertex src = vertex_map.get(source);
		Iterator it = src.adjacency.listIterator();
		while(it.hasNext()){
			Edge edge = (Edge) it.next();
			if (dest.equals(edge.getDesti())){
				it.remove();
			}
		}
	}
	
	// Makes an edge available 
	public void up_edge(String source, String dest){
		Vertex src = vertex_map.get(source);
		Iterator it = src.adjacency.listIterator();
		while(it.hasNext()){
			Edge edge = (Edge) it.next();
			if (dest.equals(edge.getDesti())){
				edge.setAvail(true);
			}
		}
		
	}
	
	// Makes an edge unavailable
	public void down_edge(String source, String dest){
		Vertex src = vertex_map.get(source);
		Iterator it = src.adjacency.listIterator();
		while(it.hasNext()){
			Edge edge = (Edge) it.next();
			if (dest.equals(edge.getDesti())){
				edge.setAvail(false);
			}
		}
		
	}
	
	// Makes a vertex available
	public void vertex_up(String vertex){
		Vertex v = vertex_map.get(vertex);
		v.setAvailability(true);
	}
	
	// Makes a vertex unavailable
	public void vertex_down(String vertex){
		Vertex v = vertex_map.get(vertex);
		v.setAvailability(false);
	}
	
	private void clear_all() {
		for (Vertex ver : vertex_map.values())
			ver.reset();
	}
	
	
	/* Captures all vertices available and calculates shortest path using Dijkstra's Algorithm
	 * Running time for finding shortest path is O(|V|+|E| log V)
	 */
	
	private double shortest_path(String src, String dest) {
		clear_all();
		int vertices_count = 0;
		for (Vertex v : vertex_map.values()) {
			if (v.availability == true) {
				vertices_count++;
			}
		}
		Vertex srce = vertex_map.get(src);
		srce.dist = 0;
		srce.prev = null;
		
		Vertex[] vert = new Vertex[vertices_count];
		int i = 0;
		for (Vertex v : vertex_map.values()) {
			if (v.availability == true) {
				vert[i] = v;
				v.setPos(i);
				i++;
			}
		}
		

		build_min_heap(vert, vertices_count);
		int vert_size = vertices_count;
		while (vert_size != 0) {
			Vertex U = extractMin(vert, vert_size);
			vert_size--;
			Iterator it = U.adjacency.listIterator();
			while (it.hasNext()) {
				Edge edge = (Edge) it.next();
				if (edge.isAvail() == true) {
					String des = edge.getDesti();
					Vertex desti = vertex_map.get(des);
					if (desti.isAvailable()) {
						if (desti.dist > (U.dist + edge.getWei())) {
							double w = U.dist + edge.getWei();
							desti.dist = w;
							desti.setPrev(U);
							int position = desti.getPos();
							HeapdecreaseKey(vert, position, desti);
						}
					}
				}
			}
		}
		
		return vertex_map.get(dest).dist;
	}
	
	private void HeapdecreaseKey(Vertex[] vert, int position, Vertex desti) {		
		vert[position] = desti;
		while (position > 0 && vert[(position - 1) / 2].dist > vert[position].dist) {
			Vertex temp = vert[(position - 1) / 2];
			Vertex antemp = vert[position];
			temp.setPos(position);
			vert[position] = temp;
			antemp.setPos((position - 1) / 2);
			vert[(position - 1) / 2] = antemp;
			position = (position - 1) / 2;
		}
		
	}
	
	/*
	 * Extracting minimum distance from source to destination to find shortest path
	 */
	private Vertex extractMin(Vertex[] vert, int vert_size) {
		int lastind = vert_size - 1;
		if (vert_size < 1) {
			System.out.println("Heap Underflow");
			System.exit(0);
		}
		Vertex min = vert[0];
		Vertex dif = vert[lastind];
		dif.setPos(0);
		vert[0] = vert[lastind];
		min.setPos(lastind);
		vert[lastind] = min;
		min_heapify(vert, 0, lastind);
		
		return min;
	}
	
	// Implementing priority queue using minheap
	private void build_min_heap(Vertex[] vert, int index) {
		int idx = index / 2;
		for (int j = idx - 1; j >= 0; j--) {
			min_heapify(vert, j, index);
		}
		
	}
	
	
	private void min_heapify(Vertex[] vert, int j, int index) {
		int left, right, small;
		left = 2 * j + 1;
		right = 2 * j + 2;
		if ( left < index && vert[left].dist < vert[j].dist) {
			small = left;
		} else
			small = j;
		if (right < index && vert[right].dist < vert[small].dist) {
			small = right;
		}
		if (small != j) {
			Vertex temp = vert[small];
			Vertex an = vert[j];
			an.setPos(small);
			vert[small] = an;
			temp.setPos(j);
			vert[j] = temp;
			min_heapify(vert, small, index);
		}
		
	}
	
	// Prints the graph in sorted order
	
	private void print() {
		for (Vertex v : vertex_map.values()) {
			Comparator<Edge> cmp=new Comparator<Edge>(){
				public int compare(Edge edg1, Edge edg2) {
					int c = edg1.getDesti().compareTo(edg2.getDesti());
					return c;
				}
			};
			Collections.sort(v.adjacency, cmp);
		}
		for (Vertex ver : vertex_map.values()) {
			if (ver.availability == false) {
				System.out.println(ver.name + " " + "Down");
			} else {
				System.out.println(ver.name);
			}
			Iterator it = ver.adjacency.listIterator();
			while (it.hasNext()) {
				Edge edge = new Edge();
				edge = (Edge) it.next();
				String dest = edge.getDesti();
				double wei = Math.round(edge.getWei() * 100D)/100D;
				
				if (edge.isAvail() == false) {
					System.out.println(" " + dest + " " + wei + " " + "Down");
				} else {
					System.out.println(" " + dest + " " + wei);
				}
			}
		}
		
	}
	
	/*Bread first search to build a breadth first tree from the available vertices and
	 * check if they are reachable.
	 * Running time is O(|V|+|E|)
	 */
	private void BFS(Vertex v) {
		Map<String, Vertex> tmap = new TreeMap<String, Vertex>();
		clear_all();
		v.setColor("grey");;
		v.setDist(0);
		v.setPrev(null);
		PriorityQueue<String> q = new PriorityQueue<String>();
		q.add(v.name);
		while (!q.isEmpty()) {
			String u = q.remove();
			Vertex U = vertex_map.get(u);
			Iterator it = U.adjacency.listIterator();
			while (it.hasNext()) {
				Edge edge = (Edge) it.next();
				Vertex ver = vertex_map.get(edge.getDesti());
				if (!ver.name.equals(U.name) && ver.availability) {
					if (ver.getColor().equals("White")) {
						ver.setColor("Gray");
						ver.dist = U.getDist() + 1.0;
						ver.setPrev(U);
						q.add(ver.name);
						tmap.put(ver.name, ver);
					}
				}
			}
			U.setColor("Black");
		}
		for (Vertex w : tmap.values()) {
			System.out.println("  " + w.name);
		}
	}
	/*
	 * To print the shortest path between two vertices
	 */
	private void print_path(String dest) {
		Vertex destin = vertex_map.get(dest);
		if (destin.prev != null) {
			print_path(destin.prev.name);
		}
		System.out.print(destin.name+" ");
		
	}

	private void reachable() {
		for (Vertex v : vertex_map.values()) {

			if (v.isAvailable()) {
				System.out.println(v.name);
				BFS(v);
			}
		}
		
	}

	public static void main(String[] args) {
		Graph graf = new Graph();
		boolean available = true;
		try{
		FileReader file_input = new FileReader(args[0]);
		BufferedReader graph_file = new BufferedReader(file_input);
		String each_line;
		while ((each_line = graph_file.readLine()) != null){
			StringTokenizer st = new StringTokenizer(each_line);
			try{
				if (st.countTokens() != 3){
					System.out.println("wrong number of input arguments, skipping the input");
					continue;
				}
				String source = st.nextToken();
				String destination = st.nextToken();
				Float weight = Float.parseFloat(st.nextToken());
				graf.build_graph(source, destination, weight, available);
			}
			catch(NumberFormatException e){
				
			}
		}
		
		}
		catch(IOException e) {
			System.err.println(e);
		}
		System.out.println("Reading file...");
		System.out.println(graf.vertex_map.size() + " vertices");
		
		while(true){
			Scanner inp = new Scanner(System.in);
			System.out.println("Enter the query :");
			String query = inp.nextLine();
			StringTokenizer st = new StringTokenizer(query);
			if (st.countTokens() == 4){
				String func = st.nextToken();
				String src = st.nextToken();
				String dest = st.nextToken();
				Double weight = Double.parseDouble(st.nextToken());
				if (func.equals("addedge")){
					graf.add_edge(src, dest, weight, available);
				}
				
			}
			else if (st.countTokens() == 3){
				String func = st.nextToken();
				String src = st.nextToken();
				String dest = st.nextToken();
				if (func.equals("edgedown")){
					graf.down_edge(src, dest);
				}
				else if (func.equals("edgeup")){
					graf.up_edge(src, dest);
				}
				else if (func.equals("deleteedge")){
					graf.del_edge(src, dest);
				}
				else if (func.equals("path")){
					double distance = graf.shortest_path(src, dest);
					System.out.println(distance);
					graf.print_path(dest);
					BigDecimal a = new BigDecimal(distance);
//					System.out.println(a);
					BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
//					System.out.println();
					System.out.println(" " + roundOff);
				}
			}
			else if (st.countTokens() == 2){
				String method = st.nextToken();
				String vertex = st.nextToken();
				if (method.equals("vertexdown")) {
					graf.vertex_down(vertex);
				} else if (method.equals("vertexup")) {
					graf.vertex_up(vertex);
				}
			}
			else if (st.countTokens() == 1){
				String method = st.nextToken();
				if (method.equals("print")) {
					graf.print();
				} else if (method.equals("reachable")) {
					graf.reachable();
				} else if (method.equals("quit")) {
					System.exit(0);
				}
			}
		}
			
	}
   
}
