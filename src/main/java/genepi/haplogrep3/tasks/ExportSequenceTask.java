package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import core.Reference;
import core.TestSample;
import genepi.haplogrep3.model.AnnotatedSample;
import util.ExportUtils;

public class ExportSequenceTask {

	private String filename;

	private List<AnnotatedSample> samples;

	private Reference reference;

	private ExportSequenceFormat format;

	public static enum ExportSequenceFormat {
		FASTA, FASTA_MSA
	}

	public ExportSequenceTask(List<AnnotatedSample> samples, String filename, ExportSequenceFormat format, Reference reference) {
		this.samples = samples;
		this.filename = filename;
		this.reference = reference;
		this.format = format;
	}

	public void run() throws IOException {

		List<TestSample> testSamples = new Vector<TestSample>();
		for (AnnotatedSample sample : samples) {
			testSamples.add(sample.getTestSample());
		}

		switch (format) {
		case FASTA:
			ExportUtils.generateFasta(testSamples, reference, filename);
			break;
		case FASTA_MSA:
			ExportUtils.generateFastaMSA(testSamples, reference, filename);
			break;
		}

	}

}
