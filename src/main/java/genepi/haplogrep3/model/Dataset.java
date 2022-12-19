package genepi.haplogrep3.model;

import genepi.io.FileUtil;

public class Dataset {

	private String id;

	private String name;

	private String file;

	private String tree;
	
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
	
	public String getTree() {
		return tree;
	}
	
	public void setTree(String tree) {
		this.tree = tree;
	}

	public void updateParent(String parent) {
		this.file = FileUtil.path(parent, file);
	}

}
