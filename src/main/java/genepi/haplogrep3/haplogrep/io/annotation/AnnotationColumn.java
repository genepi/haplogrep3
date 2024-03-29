package genepi.haplogrep3.haplogrep.io.annotation;

public class AnnotationColumn {

	private String name;

	private String column;

	private boolean show = false;
	
	private String export = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShow() {
		return show;
	}

	public void setExport(String export) {
		this.export = export;
	}
	
	public String getExport() {
		return export;
	}
	
}