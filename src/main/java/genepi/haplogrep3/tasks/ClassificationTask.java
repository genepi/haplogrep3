package genepi.haplogrep3.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.Phylotree;
import importer.FastaImporter;
import importer.HsdImporter;

public class ClassificationTask {

	private boolean success = true;

	private String error = null;

	private Phylotree phylotree;

	private List<File> files;

	private List<AnnotatedSample> samples;

	private long start = 0;

	private long end = 0;
	
	private Distance distance;

	public ClassificationTask(Phylotree phylotree, List<File> files, Distance distance) {
		this.phylotree = phylotree;
		this.files = files;
		this.distance = distance;
	}

	public void run() throws Exception {

		start = System.currentTimeMillis();

		samples = new ArrayList<>();
		if (files == null || files.isEmpty()) {
			setError("No files uploaded.");
			return;
		}

		// TODO: create Factory to use FastaImporter, VFC Importer, ...
		System.out.println("Run Fasta Importer");
		ArrayList<String> lines = new ArrayList<String>();
		HsdImporter importer = new HsdImporter();
		for (File file : files) {
			lines.addAll(importer.load(file));
		} 
		
		SampleFile sampleFile = new SampleFile(lines, phylotree.getReference());

		phylotree.classify(sampleFile, distance);

		AnnotationTask annotationTask = new AnnotationTask(sampleFile, phylotree);
		annotationTask.run();
		samples = annotationTask.getAnnotatedSamples();

		end = System.currentTimeMillis();

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

}
