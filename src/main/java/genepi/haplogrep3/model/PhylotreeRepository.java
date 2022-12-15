package genepi.haplogrep3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import core.Reference;
import genepi.haplogrep3.config.Configuration;

public class PhylotreeRepository {

	private List<Phylotree> trees;

	public PhylotreeRepository() {
		trees = new Vector<Phylotree>();
	}

	public synchronized void loadFromConfiguration(Configuration configuration)
			throws FileNotFoundException, IOException {

		trees = new Vector<Phylotree>();

		for (String filename : configuration.getPhylotrees()) {
			System.out.println("Load tree from file " + filename);
			Phylotree phylotree = Phylotree.load(new File(filename));
			Reference reference = new Reference(phylotree.getFasta());
			phylotree.setReference(reference);
			System.out.println("Tree loaded.");
			trees.add(phylotree);
		}

	}

	public List<Phylotree> getAll() {

		return Collections.unmodifiableList(trees);

	}

	public synchronized Phylotree getById(String id) {

		// TODO: use data-structure with O(1), hashmap or so.

		for (Phylotree tree : trees) {
			if (tree.getId().equals(id)) {
				return tree;
			}
		}
		return null;

	}

}
