package genepi.haplogrep3.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import genepi.haplogrep3.haplogrep.io.readers.InputFileReaderFactory;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileWithStatistics;
import genepi.haplogrep3.haplogrep.io.readers.StatisticCounter;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.Phylotree;

public class ClassificationTask {

	private boolean success = true;

	private String error = null;

	private Phylotree phylotree;

	private List<File> files;

	private List<AnnotatedSample> samples;

	private long start = 0;

	private long end = 0;

	private Distance distance;

	private boolean chip = false;

	private double hetLevel = 0.9;

	private int hits = 1;

	private boolean skipAlignmentRules = false;

	private int samplesOk = 0;

	private int samplesWarning = 0;

	private int samplesError = 0;

	private List<StatisticCounter> counters = new Vector<StatisticCounter>();

	public ClassificationTask(Phylotree phylotree, List<File> files, Distance distance) {
		this.phylotree = phylotree;
		this.files = files;
		this.distance = distance;
	}

	public void setChip(boolean chip) {
		this.chip = chip;
	}

	public void setHetLevel(double hetLevel) {
		this.hetLevel = hetLevel;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public void setSkipAlignmentRules(boolean skipAlignmentRules) {
		this.skipAlignmentRules = skipAlignmentRules;
	}

	public void run() throws Exception {

		start = System.currentTimeMillis();

		samples = new ArrayList<>();
		if (files == null || files.isEmpty()) {
			setError("No files uploaded.");
			return;
		}

		try {

			InputFileReaderFactory reader = new InputFileReaderFactory();
			reader.setChip(chip);
			reader.setHetLevel(hetLevel);
			reader.setSkipAlignmentRules(skipAlignmentRules);

			SampleFileWithStatistics sampleFile = reader.read(files, phylotree);
			if (sampleFile.getStatistics() != null) {
				counters = sampleFile.getStatistics().getCounters();
			}

			phylotree.classify(sampleFile.getSampleFile(), distance, hits, skipAlignmentRules);

			AnnotationTask annotationTask = new AnnotationTask(sampleFile.getSampleFile(), phylotree);
			annotationTask.run();
			samples = annotationTask.getAnnotatedSamples();
			for (AnnotatedSample sample : samples) {
				if (sample.hasErrors()) {
					samplesError++;
				} else if (sample.hasWarnings()) {
					samplesWarning++;
				} else {
					samplesOk++;
				}
			}

			end = System.currentTimeMillis();

		} catch (Exception e) {
			e.printStackTrace();
			setError(e.getMessage());
			return;
		}

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

	public List<AnnotatedSample> getSamples() {
		return samples;
	}

	public long getExecutionTime() {
		return end - start;
	}

	public int getSamplesOk() {
		return samplesOk;
	}

	public int getSamplesError() {
		return samplesError;
	}

	public int getSamplesWarning() {
		return samplesWarning;
	}

	public List<StatisticCounter> getCounters() {
		return counters;
	}

	public StatisticCounter getCounterByLabel(String label) {
		if (counters == null) {
			return null;
		}
		for (StatisticCounter counter : counters) {
			if (counter.getLabel().equals(label)) {
				return counter;
			}
		}
		return null;
	}

}
