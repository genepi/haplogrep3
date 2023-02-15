package genepi.haplogrep3.model;

import java.util.HashMap;
import java.util.Map;

import core.Polymorphism;

public class AnnotatedPolymorphism {

	private String aac = "";

	private String nuc = "";

	private boolean found = false;

	private String type = "";

	private Map<String, String> annotations = new HashMap<>();

	private int position;

	private String ref;

	private String alt;

	public AnnotatedPolymorphism(Polymorphism polymorphism) {
		position = polymorphism.getPosition();
		ref = polymorphism.getReferenceBase().getMutation().toString();
		alt = polymorphism.getMutation().toString();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Map<String, String> annotations) {
		this.annotations = annotations;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getAlt() {
		return alt;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getRef() {
		return ref;
	}

}
