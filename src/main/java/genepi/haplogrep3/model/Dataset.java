package genepi.haplogrep3.model;

import genepi.io.FileUtil;

public class Dataset {

	private String id;

	private String name;

	private String file;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void updateParent(String parent) {
		this.file = FileUtil.path(parent, file);
	}

}
