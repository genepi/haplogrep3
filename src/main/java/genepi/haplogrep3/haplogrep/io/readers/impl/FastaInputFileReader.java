package genepi.haplogrep3.haplogrep.io.readers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.readers.AbstractInputFileReader;
import genepi.haplogrep3.model.Phylotree;
import importer.FastaImporter;

public class FastaInputFileReader extends AbstractInputFileReader {

	public FastaInputFileReader() {
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
		return sampleFile;

	}

}
