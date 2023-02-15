package genepi.haplogrep3.haplogrep.io.annotation;

import java.util.List;
import java.util.Vector;

public class AnnotationSettings {

	private String filename;

	private String source;

	private String url;

	private String refAllele;

	private String altAllele;

	private String chr;

	private List<AnnotationColumn> properties = new Vector<AnnotationColumn>();

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRefAllele() {
		return refAllele;
	}

	public void setRefAllele(String refAllele) {
		this.refAllele = refAllele;
	}

	public String getAltAllele() {
		return altAllele;
	}

	public void setAltAllele(String altAllele) {
		this.altAllele = altAllele;
	}

	public List<AnnotationColumn> getProperties() {
		return properties;
	}

	public void setProperties(List<AnnotationColumn> properties) {
		this.properties = properties;
	}

	public String getChr() {
		return chr;
	}

	public void setChr(String chr) {
		this.chr = chr;
	}

	public void init() {

	}

}
