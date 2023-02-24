package genepi.haplogrep3.haplogrep.io.readers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.readers.AbstractInputFileReader;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileWithStatistics;
import genepi.haplogrep3.model.Phylotree;
import importer.HsdImporter;

public class HsdInputFileReader extends AbstractInputFileReader {

	public boolean accepts(List<File> files, Phylotree phylotree) {
		return hasFileExtensions(files, ".hsd", ".txt");
	}

	public SampleFileWithStatistics read(List<File> files, Phylotree phylotree) throws Exception {

		ArrayList<String> lines = new ArrayList<String>();
		HsdImporter importer = new HsdImporter();
		for (File file : files) {
			lines.addAll(importer.load(file));
		}

		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());
		return new SampleFileWithStatistics(sampleFile, null);

	}

}
