package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import core.Reference;
import core.TestSample;
import genepi.haplogrep3.model.AnnotatedSample;
import util.ExportUtils;

public class ExportHaplogroupsTask {

	private String filename;

	private List<AnnotatedSample> samples;

	private Reference reference;

	private int hits = 1;

	public static enum ExportDataFormat {
		SIMPLE, EXTENDED
	}

	private ExportDataFormat format = ExportDataFormat.SIMPLE;

	public ExportHaplogroupsTask(List<AnnotatedSample> samples, String filename, ExportDataFormat format,
			Reference reference) {
		this.samples = samples;
		this.filename = filename;
		this.format = format;
		this.reference = reference;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public void run() throws IOException {

		List<TestSample> testSamples = new Vector<TestSample>();
		for (AnnotatedSample sample : samples) {
			testSamples.add(sample.getTestSample());
		}

		ExportUtils.createReport(testSamples, reference, filename, format == ExportDataFormat.EXTENDED, hits);

		System.out.println("Written haplogroups to file " + filename);

	}

}
