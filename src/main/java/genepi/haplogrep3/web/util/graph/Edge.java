package genepi.haplogrep3.web.util.graph;

public class Edge {

	private int from;

	private int to;

	private String label;

	public Edge(Node from, Node to, String label) {
		this.from = from.getId();
		this.to = to.getId();
		this.label = label;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
