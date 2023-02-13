package genepi.haplogrep3.web.util.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Graph {

	private String direction = "TD";

	private List<Node> nodes = new Vector<Node>();

	private List<Edge> edges = new Vector<Edge>();

	private Map<String, Node> nodeIndex = new HashMap<String, Node>();

	private Set<String> uniqueEdges = new HashSet<String>();

	public Node addNode(String name) {
		Node node = new Node(nodes.size() + 1, name);
		nodes.add(node);
		nodeIndex.put(name, node);
		return node;
	}

	public void addEdge(String from, String to, String label) {
		if (uniqueEdges.contains(from + "|" + to)) {
			return;
		}
		Node fromNode = nodeIndex.get(from);
		Node toNode = nodeIndex.get(to);

		if (fromNode == null) {
			fromNode = addNode(from);
		}

		if (toNode == null) {
			toNode = addNode(to);
		}

		Edge edge = new Edge(fromNode, toNode, label);
		edges.add(edge);
		uniqueEdges.add(from + "|" + to);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public String toDot(String layout) {

		String quote = "\"";
		StringBuffer buffer = new StringBuffer();
		buffer.append("digraph { ");
		buffer.append("graph [" + layout + "]; ");
		buffer.append("node [shape = box, style=\"rounded,filled\"]; ");

		for (Node node : nodes) {
			buffer.append(node.getId() + " [label=\"" + node.getLabel() + "\", fillcolor=\"" + node.getColor()
					+ "\"]; ");
		}

		for (Edge edge : edges) {
			buffer.append(quote + edge.getFrom() + quote + " -> " + quote + edge.getTo() + quote + " [label=" + quote
					+ edge.getLabel() + quote + "] ;");
		}
		buffer.append("}");
		return buffer.toString();

	}

}
