package genepi.haplogrep3.haplogrep.io.readers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.readers.AbstractInputFileReader;
import genepi.haplogrep3.model.Phylotree;
import importer.VcfImporter;
import util.ExportUtils;
import vcf.Sample;

public class VcfInputFileReader extends AbstractInputFileReader {

	private boolean chip = false;

	private double hetLevel = 0.9;

	public VcfInputFileReader(boolean chip, double hetLevel) {
		this.chip = chip;
		this.hetLevel = hetLevel;
	}
	
	public boolean accepts(List<File> files, Phylotree phylotree) {
		return hasFileExtensions(files, ".vcf", ".vcf.gz");
	}

	public SampleFile read(List<File> files, Phylotree phylotree) throws Exception {

		ArrayList<String> lines = new ArrayList<String>();
		VcfImporter importerVcf = new VcfImporter();
		for (File file : files) {
			HashMap<String, Sample> samples = importerVcf.load(file, chip);
			lines = ExportUtils.vcfTohsd(samples, Double.valueOf(hetLevel));

		}

		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());
		return sampleFile;

	}

	public void setChip(boolean chip) {
		this.chip = chip;
	}

	public void setHetLevel(double hetLevel) {
		this.hetLevel = hetLevel;
	}

}
