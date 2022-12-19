package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import core.Mutations;
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
			annotatedSample.setExpectedMutations(
					getNotFoundPolymorphisms(sample.getSample().getPolymorphisms(), detailedResults));
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
		int deletionCount = 0;

		List<AnnotatedPolymorphism> annotatedPolymorphisms = new Vector<AnnotatedPolymorphism>();
		PolymorphismHelper.sortByPosition(polymorphisms);

		for (int i = 0; i < polymorphisms.size(); i++) {

			Polymorphism polymorphism = polymorphisms.get(i);

			// Iterator<Node<MapLocusItem>> result =
			// maplocus.findByPosition(polymorphism.getPosition());
			// if (result.hasNext()) {
			//if (!polymorphism.isBackMutation() && polymorphism.getMutation() != Mutations.N) {
				// MapLocusItem item = result.next().getValue();
				/*
				 * String aac = ""; try { aac = SequenceUtil.getAAC(refSequence, codonTable,
				 * item, polymorphism.getPosition(), polymorphism.getMutation().name()); } catch
				 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
				 */

				AnnotatedPolymorphism annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
				annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				annotatedPolymorphisms.add(annotatedPolymorphism);

				/*
				 * if (polymorphism.getMutation().toString().equals("DEL")) { deletionCount++;
				 * if (deletionCount % 3 == 0) { if (polymorphisms.get(i - 2).getPosition() ==
				 * polymorphisms.get(i).getPosition() - 2) { AnnotatedPolymorphism
				 * annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
				 * annotatedPolymorphism.setAac(item.getShorthand() + ":" + aac);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism)); if
				 * (detailedResults != null) { annotatedPolymorphism
				 * .setFound(detailedResults.getFoundPolys().contains(polymorphism)); }
				 * annotatedPolymorphisms.add(annotatedPolymorphism); } } else {
				 * AnnotatedPolymorphism annotatedPolymorphism = new
				 * AnnotatedPolymorphism(polymorphism); annotatedPolymorphism.setAac(null); if
				 * (detailedResults != null) {
				 * annotatedPolymorphism.setFound(detailedResults.getFoundPolys().contains(
				 * polymorphism)); }
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				 * annotatedPolymorphisms.add(annotatedPolymorphism); } } else if (aac.length()
				 * > 0 && !aac.contains("?")) { AnnotatedPolymorphism annotatedPolymorphism =
				 * new AnnotatedPolymorphism(polymorphism);
				 * annotatedPolymorphism.setAac(item.getShorthand() + ":" + aac);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism)); if
				 * (detailedResults != null) {
				 * annotatedPolymorphism.setFound(detailedResults.getFoundPolys().contains(
				 * polymorphism)); } annotatedPolymorphisms.add(annotatedPolymorphism); } else {
				 * AnnotatedPolymorphism annotatedPolymorphism = new
				 * AnnotatedPolymorphism(polymorphism); annotatedPolymorphism.setAac(null);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism)); if
				 * (detailedResults != null) {
				 * annotatedPolymorphism.setFound(detailedResults.getFoundPolys().contains(
				 * polymorphism)); } annotatedPolymorphisms.add(annotatedPolymorphism); }
				 */
				// }
			//}
		}

		return annotatedPolymorphisms;

	}

	protected List<AnnotatedPolymorphism> getNotFoundPolymorphisms(List<Polymorphism> polymorphisms,
			SearchResultDetailed detailedResults) {
		int deletionCount = 0;

		List<AnnotatedPolymorphism> expectedMutations = new Vector<AnnotatedPolymorphism>();
		PolymorphismHelper.sortByPosition(polymorphisms);

		for (int i = 0; i < detailedResults.getExpectedPolys().size(); i++) {

			Polymorphism polymorphism = detailedResults.getExpectedPolys().get(i);
			if (polymorphisms.contains(polymorphism)) {
				continue;
			}

			// Iterator<Node<MapLocusItem>> result =
			// maplocus.findByPosition(polymorphism.getPosition());
			// if (result.hasNext()) {
			//if (!polymorphism.isBackMutation() && polymorphism.getMutation() != Mutations.N) {
				// MapLocusItem item = result.next().getValue();
				/*
				 * String aac = ""; try { aac = SequenceUtil.getAAC(refSequence, codonTable,
				 * item, polymorphism.getPosition(), polymorphism.getMutation().name()); } catch
				 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
				 */

				AnnotatedPolymorphism annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
				annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				expectedMutations.add(annotatedPolymorphism);

				/*
				 * if (polymorphism.getMutation().toString().equals("DEL")) { deletionCount++;
				 * if (deletionCount % 3 == 0) { if (detailedResults.getExpectedPolys().get(i -
				 * 2) .getPosition() == detailedResults.getExpectedPolys().get(i).getPosition()
				 * - 2) { AnnotatedPolymorphism annotatedPolymorphism = new
				 * AnnotatedPolymorphism(polymorphism);
				 * annotatedPolymorphism.setAac(item.getShorthand() + ":" + aac);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				 * annotatedPolymorphism.setFound(true);
				 * expectedMutations.add(annotatedPolymorphism); } } else {
				 * AnnotatedPolymorphism annotatedPolymorphism = new
				 * AnnotatedPolymorphism(polymorphism); annotatedPolymorphism.setAac(null);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				 * annotatedPolymorphism.setFound(true);
				 * expectedMutations.add(annotatedPolymorphism); } } else if (aac.length() > 0
				 * && !aac.contains("?")) { AnnotatedPolymorphism annotatedPolymorphism = new
				 * AnnotatedPolymorphism(polymorphism);
				 * annotatedPolymorphism.setAac(item.getShorthand() + ":" + aac);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				 * annotatedPolymorphism.setFound(true);
				 * expectedMutations.add(annotatedPolymorphism); } else { AnnotatedPolymorphism
				 * annotatedPolymorphism = new AnnotatedPolymorphism(polymorphism);
				 * annotatedPolymorphism.setAac(null);
				 * annotatedPolymorphism.setNuc(PolymorphismHelper.getLabel(polymorphism));
				 * annotatedPolymorphism.setFound(true);
				 * expectedMutations.add(annotatedPolymorphism); }
				 */
				// }
			//}
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
