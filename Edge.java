
public class Edge {
	public Vertex sourc, desti; 
	public double wei; 
	boolean avail;

public Edge(Vertex source_name, Vertex dest_name,double weight, boolean availability){
	sourc = source_name;
	desti = dest_name;
	wei = weight;
	avail = availability;
}

public Edge() {
	// TODO Auto-generated constructor stub
}

public String getSourc() {
	return sourc.name;
}

public void setSourc(Vertex sourc) {
	this.sourc = sourc;
}

public String getDesti() {
	return desti.name;
}

public void setDesti(Vertex desti) {
	this.desti = desti;
}

public double getWei() {
	return wei;
}

public void setWei(double wei) {
	this.wei = wei;
}

public boolean isAvail() {
	return avail;
}

public void setAvail(boolean avail) {
	this.avail = avail;
}
}
