package genepi.haplogrep3.model;

public class Cluster {

	private String label;

	private String[] nodes = new String[0];

	private String colour;

	public Cluster(String label, String[] nodes, String colour) {

		this.label = label;

		this.nodes = nodes;
		
		this.colour = colour;

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String[] getNodes() {
		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

}
