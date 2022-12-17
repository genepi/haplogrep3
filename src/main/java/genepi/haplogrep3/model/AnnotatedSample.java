package genepi.haplogrep3.model;

import java.util.List;
import java.util.Vector;

import core.TestSample;
import genepi.haplogrep3.util.PolymorphismHelper;
import search.SearchResultDetailed;
import search.ranking.results.RankedResult;

public class AnnotatedSample {

	private String sample;

	private String clade;

	private double quality;

	private int ns = 0;

	private int mixCount = 0;

	private int coverage = 0;

	private int expected = 0;

	private int found = 0;

	private String[] ranges = new String[0];

	private String[] otherClades = new String[0];

	private double[] otherQualities = new double[0];

	private List<AnnotatedPolymorphism> expectedMutations = new Vector<AnnotatedPolymorphism>();

	private List<AnnotatedPolymorphism> annotatedPolymorphisms = new Vector<AnnotatedPolymorphism>();

	private transient TestSample testSample;

	public AnnotatedSample(TestSample testSample) {

		this.testSample = testSample;
		sample = testSample.getSampleID();
		RankedResult topResult = testSample.getTopResult();
		SearchResultDetailed detailedResult = topResult.getSearchResult().getDetailedResult();

		clade = topResult.getHaplogroup().toString();
		clade = clade.split("_")[0];
		quality = topResult.getDistance();
		ns = PolymorphismHelper.getNCount(detailedResult.getRemainingPolysInSample());
		mixCount = PolymorphismHelper.getMixCount(detailedResult.getRemainingPolysInSample());
		coverage = PolymorphismHelper.getRangeLength(testSample.getSample().getSampleRanges().toString());

		String[] rangesWithSpaces = testSample.getSample().getSampleRanges().toString().split(";");
		ranges = new String[rangesWithSpaces.length];
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = rangesWithSpaces[i].trim();
		}

		int hits = testSample.getResults().size();
		if (hits > 1) {
			otherClades = new String[hits - 1];
			otherQualities = new double[hits - 1];
			for (int i = 0; i < hits-1; i++) {
				RankedResult result = testSample.getResults().get(i + 1);
				otherClades[i] = result.getHaplogroup().toString();
				otherQualities[i] = result.getDistance();
			}
		}

		expected = detailedResult.getExpectedPolys().size();
		found = detailedResult.getFoundPolys().size();

	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public String getClade() {
		return clade;
	}

	public void setClade(String clade) {
		this.clade = clade;
	}

	public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public int getMixCount() {
		return mixCount;
	}

	public void setMixCount(int mixCount) {
		this.mixCount = mixCount;
	}

	public int getCoverage() {
		return coverage;
	}

	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}

	public void setOtherClades(String[] otherClades) {
		this.otherClades = otherClades;
	}

	public String[] getOtherClades() {
		return otherClades;
	}

	public void setOtherQualities(double[] otherQualities) {
		this.otherQualities = otherQualities;
	}

	public double[] getOtherQualities() {
		return otherQualities;
	}

	public String[] getRanges() {
		return ranges;
	}

	public void setRanges(String[] ranges) {
		this.ranges = ranges;
	}

	public void setAnnotatedPolymorphisms(List<AnnotatedPolymorphism> annotatedPolymorphisms) {
		this.annotatedPolymorphisms = annotatedPolymorphisms;
	}

	public List<AnnotatedPolymorphism> getAnnotatedPolymorphisms() {
		return annotatedPolymorphisms;
	}

	public void setExpected(int expected) {
		this.expected = expected;
	}

	public int getExpected() {
		return expected;
	}

	public void setFound(int found) {
		this.found = found;
	}

	public int getFound() {
		return found;
	}

	public void setExpectedMutations(List<AnnotatedPolymorphism> expectedMutations) {
		this.expectedMutations = expectedMutations;
	}

	public List<AnnotatedPolymorphism> getExpectedMutations() {
		return expectedMutations;
	}

	public TestSample getTestSample() {
		return testSample;
	}

	@Override
	public String toString() {
		return sample;
	}

}
