package genepi.haplogrep3.model;

import genepi.io.FileUtil;

public class Dataset {

	private String id;

	private String name;

	private String file;

	private String tree;

	private boolean chip = false;

	private boolean additionalOutput = false;

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

	public boolean isChip() {
		return chip;
	}

	public void setChip(boolean chip) {
		this.chip = chip;
	}

	public boolean isAdditionalOutput() {
		return additionalOutput;
	}

	public void setAdditionalOutput(boolean additionalOutput) {
		this.additionalOutput = additionalOutput;
	}

	public void updateParent(String parent) {
		this.file = FileUtil.path(parent, file);
	}

}
