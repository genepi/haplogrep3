package genepi.haplogrep3.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SampleFile;
import core.TestSample;
import genepi.haplogrep3.model.Phylotree;
import importer.FastaImporter;
import util.ExportUtils;

public class AlignTask {

	private boolean success = true;

	private String error = null;

	private Phylotree phylotree;

	private List<File> files;

	private String filename;

	public AlignTask(Phylotree phylotree, List<File> files, String filename) {
		this.phylotree = phylotree;
		this.files = files;
		this.filename = filename;
	}

	public void run() throws Exception {

		if (files == null || files.isEmpty()) {
			setError("No files uploaded.");
			return;
		}

		ArrayList<String> lines = new ArrayList<String>();
		FastaImporter importer = new FastaImporter();
		for (File file : files) {
			lines.addAll(importer.load(file, phylotree.getReference()));
		}

		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());
		ArrayList<TestSample> samplesH = sampleFile.getTestSamples();
		ExportUtils.generateFasta(samplesH, filename, phylotree.getReference());

		// ExportUtils creates a different filename --> rename it to filename user
		// provided.
		String newFilename = filename + "_haplogrep2.fasta";
		new File(newFilename).renameTo(new File(filename));

		success = true;

	}

	public boolean isSuccess() {
		return success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
		success = false;
	}

}
