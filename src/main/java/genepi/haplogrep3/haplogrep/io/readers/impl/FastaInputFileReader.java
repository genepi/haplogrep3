package genepi.haplogrep3.haplogrep.io.readers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.readers.AbstractInputFileReader;
import genepi.haplogrep3.model.Phylotree;
import importer.FastaImporter;
import phylotree.PhylotreeManager;

public class FastaInputFileReader extends AbstractInputFileReader {

	private boolean skipAlignmentRules = false;

	public FastaInputFileReader(boolean skipAlignmentRules) {
		this.skipAlignmentRules = skipAlignmentRules;
	}

	public boolean accepts(List<File> files, Phylotree phylotree) {
		return hasFileExtensions(files, ".fasta", ".fasta.gz");
	}

	public SampleFile read(List<File> files, Phylotree phylotree) throws Exception {

		ArrayList<String> lines = new ArrayList<String>();
		FastaImporter importer = new FastaImporter();
		for (File file : files) {
			lines.addAll(importer.load(file, phylotree.getReference()));
		}

		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());

		if (!skipAlignmentRules && phylotree.getAlignmentRules() != null) {

			phylotree.Phylotree haplogrepPhylotree = PhylotreeManager.getInstance().getPhylotree(phylotree.getTree(),
					phylotree.getWeights(), phylotree.getReference());

			sampleFile.applyNomenclatureRules(haplogrepPhylotree, phylotree.getAlignmentRules());

		}

		return sampleFile;

	}

}
