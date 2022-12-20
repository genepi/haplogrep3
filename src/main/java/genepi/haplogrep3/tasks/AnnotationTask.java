package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import core.Polymorphism;
import core.SampleFile;
import core.TestSample;
import genepi.annotate.util.MapLocusGFF3;
import genepi.annotate.util.SequenceUtil;
import genepi.haplogrep3.model.AnnotatedPolymorphism;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.util.PolymorphismHelper;
import genepi.haplogrep3.util.TestSampleHelper;
import qualityAssurance.issues.QualityIssue;
import search.SearchResultDetailed;

public class AnnotationTask {

	private SampleFile sampleFile;

	private Phylotree phylotree;

	private List<AnnotatedSample> samples = new Vector<AnnotatedSample>();

	private Map<String, String> codonTable;

	private MapLocusGFF3 maplocus;

	private String refSequence;

	public AnnotationTask(SampleFile sampleFile, Phylotree phylotree) {
		this.sampleFile = sampleFile;
		this.phylotree = phylotree;
	}

	public void loadFiles() throws IOException {
		codonTable = SequenceUtil.loadCodonTableLong(phylotree.getAacTable());
		maplocus = new MapLocusGFF3(phylotree.getGff());
		refSequence = phylotree.getReference().getSequence();
	}

	public void run() throws IOException {

		loadFiles();

		// samples from haplogrep
		ArrayList<TestSample> samplesH = sampleFile.getTestSamples();
		TestSampleHelper.sortBySampleId(samplesH);

		for (int i = 0; i < samplesH.size(); i++) {

			TestSample sample = samplesH.get(i);
			SearchResultDetailed detailedResults = sample.getTopResult().getSearchResult().getDetailedResult();

			AnnotatedSample annotatedSample = new AnnotatedSample(sample);
			annotatedSample.setAnnotatedPolymorphisms(
					getAminoAcidsFromPolys(sample.getSample().getPolymorphisms(), detailedResults));
			annotatedSample
					.setExpectedMutations(getExpectedMutations(sample.getSample().getPolymorphisms(), detailedResults));

			annotatedSample.setRemainingMutations(getRemainingPolymorphisms(detailedResults));

			samples.add(annotatedSample);

			ArrayList<QualityIssue> issues = sampleFile.getQualityAssistent().getIssues(sample);
			parseIssues(annotatedSample, issues);
		}
	}

	public List<AnnotatedSample> getAnnotatedSamples() {
		return samples;
	}

	public List<AnnotatedPolymorphism> getAminoAcidsFromPolys(List<Polymorphism> polymorphisms,
			SearchResultDetailed detailedResults) {

		List<AnnotatedPolymorphism> annotatedPolymorphisms = new Vector<AnnotatedPolymorphism>();
		PolymorphismHelper.sortByPosition(polymorphisms);

		for (int i = 0; i < polymorphisms.size(); i++) {

			Polymorphism polymorphism = polymorphisms.get(i);

			AnnotatedPolymorphism annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
			annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
			annotatedPolymorphisms.add(annotatedPolymorphism);

		}

		return annotatedPolymorphisms;

	}

	protected List<AnnotatedPolymorphism> getExpectedMutations(List<Polymorphism> polymorphisms,
			SearchResultDetailed detailedResults) {

		List<AnnotatedPolymorphism> expectedMutations = new Vector<AnnotatedPolymorphism>();
		PolymorphismHelper.sortByPosition(polymorphisms);

		for (int i = 0; i < detailedResults.getExpectedPolys().size(); i++) {

			Polymorphism polymorphism = detailedResults.getExpectedPolys().get(i);

			AnnotatedPolymorphism annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
			annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
			annotatedPolymorphism.setFound(detailedResults.getFoundPolys().contains(polymorphism));
			expectedMutations.add(annotatedPolymorphism);
		}

		return expectedMutations;

	}

	protected List<AnnotatedPolymorphism> getRemainingPolymorphisms(SearchResultDetailed detailedResults) {

		List<AnnotatedPolymorphism> expectedMutations = new Vector<AnnotatedPolymorphism>();
		PolymorphismHelper.sortByPosition(detailedResults.getRemainingPolysInSample());

		for (int i = 0; i < detailedResults.getRemainingPolysInSample().size(); i++) {

			Polymorphism polymorphism = detailedResults.getRemainingPolysInSample().get(i);

			AnnotatedPolymorphism annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
			annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
			annotatedPolymorphism.setType(PolymorphismHelper.getType(polymorphism, phylotree));
			expectedMutations.add(annotatedPolymorphism);

		}

		return expectedMutations;

	}

	public void parseIssues(AnnotatedSample sample, ArrayList<QualityIssue> issues) {
		if (issues == null) {
			return;
		}

		for (QualityIssue issue : issues) {
			switch (issue.getPriority()) {
			case 0:
				sample.addWarning(getIssueMessage(issue));
				break;
			case 1:
				sample.addError(getIssueMessage(issue));
				break;
			default:
				sample.addInfo(getIssueMessage(issue));
				break;
			}
		}

	}

	public String getIssueMessage(QualityIssue issue) {
		return issue.getDescription() + " (" + issue.getIssueType() + ")";
	}

}
