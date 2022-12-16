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

	public ExportSequenceTask(List<AnnotatedSample> samples, String filename, Reference reference) {
		this.samples = samples;
		this.filename = filename;
		this.reference = reference;
	}

	public void run() throws IOException {

		List<TestSample> testSamples = new Vector<TestSample>();
		for (AnnotatedSample sample : samples) {
			testSamples.add(sample.getTestSample());
		}

		ExportUtils.generateFasta(testSamples, reference, filename);
		ExportUtils.generateFastaMSA(testSamples, reference, filename);

	}

}
