package genepi.haplogrep3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import genepi.haplogrep3.config.Configuration;
import importer.FastaImporter;

public class PhylotreeRepository {

	private List<Phylotree> trees;

	public PhylotreeRepository() {
		trees = new Vector<Phylotree>();
	}

	public synchronized void loadFromConfiguration(Configuration configuration)
			throws FileNotFoundException, IOException {

		FastaImporter fastaImporter = new FastaImporter();

		trees = new Vector<Phylotree>();

		// TODO: reference is not yet in a file. use filename to fast in next versions
		for (String filename : configuration.getPhylotrees()) {
			System.out.println("Load tree from file " + filename);
			Phylotree phylotree = Phylotree.load(new File(filename));
			
			if (phylotree.getReferenceID().equalsIgnoreCase("SARSCOV2")) {
				phylotree.setReference(fastaImporter.loadSARSCOV2());
			} else if (phylotree.getReferenceID().equalsIgnoreCase("RCRS")) {
				phylotree.setReference(fastaImporter.loadrCRS());
			} else if (phylotree.getReferenceID().equalsIgnoreCase("RSRS")) {
				phylotree.setReference(fastaImporter.loadRSRS());
			} else {
				throw new IOException("Loading phylotree " + phylotree.getId() + " failed. Reference "
						+ phylotree.getReferenceID() + " unknown.");
			}
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
