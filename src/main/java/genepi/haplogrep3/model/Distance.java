package genepi.haplogrep3.model;

public enum Distance {

	KULCZYNSKI("Kulczynski"), HAMMING("Hamming"), JACCARD("Jaccard"), KIMURA("Kimura");

	private String label;

	private Distance(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
