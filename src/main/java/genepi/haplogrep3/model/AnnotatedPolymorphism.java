package genepi.haplogrep3.model;

import core.Polymorphism;

public class AnnotatedPolymorphism {

	private String aac = "";

	private String nuc = "";

	private boolean found = false;

	public AnnotatedPolymorphism(Polymorphism polymorphism) {

	}

	public String getAac() {
		return aac;
	}

	public void setAac(String aac) {
		this.aac = aac;
	}

	public String getNuc() {
		return nuc;
	}

	public void setNuc(String nuc) {
		this.nuc = nuc;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

}
