package genepi.haplogrep3.model;

public class Cluster implements Comparable<Cluster>{

	private String label;

	private String[] nodes = new String[0];

	private String color;

	public Cluster() {

	}

	public Cluster(String label, String[] nodes, String color) {

		this.label = label;
		this.nodes = nodes;
		this.color = color;

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String[] getNodes() {

		if (nodes == null || nodes.length == 0) {
			return new String[] { label };
		}

		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int compareTo(Cluster o) {
		return label.compareTo(o.label);
	}

}
