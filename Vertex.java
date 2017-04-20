
import java.util.LinkedList;
import java.util.List;

public class Vertex {
	public boolean availability;
	public String name;
	public List<Edge> adjacency;
	public Vertex prev;
	public double dist;
	int pos;
	public String color;
	
	
	public Vertex(String nm, boolean availability) {
		name = nm;
		this.availability = availability;
		adjacency = new LinkedList();
		reset();
	}
	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
		color = "White";
	}
	
	
	public Vertex getPrev() {
		return prev;
	}
	public void setPrev(Vertex prev) {
		this.prev = prev;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isAvailable() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Edge> getAdj() {
		return adjacency;
	}
	public void setAdj(List<Edge> adj) {
		this.adjacency = adj;
	}
}
