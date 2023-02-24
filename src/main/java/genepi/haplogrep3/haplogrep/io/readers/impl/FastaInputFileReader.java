package genepi.haplogrep3.haplogrep.io.readers.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.readers.AbstractInputFileReader;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileWithStatistics;
import genepi.haplogrep3.model.Phylotree;
import importer.FastaImporter;

public class FastaInputFileReader extends AbstractInputFileReader {

	private boolean skipAlignmentRules = false;

	public FastaInputFileReader(boolean skipAlignmentRules) {
		this.skipAlignmentRules = skipAlignmentRules;
	}

	public boolean accepts(List<File> files, Phylotree phylotree) {
		return hasFileExtensions(files, ".fasta", ".fasta.gz", ".fa", ".fa.gz");
	}

	public SampleFileWithStatistics read(List<File> files, Phylotree phylotree) throws Exception {

		if (SystemUtils.IS_OS_WINDOWS) {
			throw new IOException("Fasta is no supported on Windows");
		}

		ArrayList<String> lines = new ArrayList<String>();
		FastaImporter importer = new FastaImporter();
		for (File file : files) {
			lines.addAll(importer.load(file, phylotree.getReference()));
		}

		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());

		if (!skipAlignmentRules && phylotree.getAlignmentRules() != null) {

			phylotree.Phylotree haplogrepPhylotree = phylotree.getPhylotreeInstance();

			sampleFile.applyNomenclatureRules(haplogrepPhylotree, phylotree.getAlignmentRules());

		}

		return new SampleFileWithStatistics(sampleFile, null);

	}

}
