			Shortest Paths in a Network

--This is an implementation of a graph problem. 
--Implemented graph is a weighted Directed graph.
--Network topology can change dynamically based on the state of the links and the routers.
--For example, a link may go down when the corresponding cable is cut, and a vertex may go down when the corresponding router crashes.
--In addition to these transient changes, changes to a network occur when a link is added or removed.
--Sample vertices and the edges are given in "network.txt".

Instructions to execute the application:

1. javac *.java  			(Compiles all the files in the folder)
2. java Graph network.txt 	(Builds graph)
3. Now various operations can be performed and the sample queries are available in queries.txt
4. Sample output for the queries are available in output.txt

Queries:

--print (prints the graph)
--path <source> <destination> (gives shortest path between any two vertices)
--addedge <source> <destination> <weight>
--edgeup  <source> <destination>
--edgedown<source> <destination>
--deleteedge<source> <destination>
--vertexup  <vertex name>
--vertexdown<vertex name>
--reachable (gives the available vertices and edges)