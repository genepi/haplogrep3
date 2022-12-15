package genepi.haplogrep3.model;

import java.text.DecimalFormat;
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

	private List<AnnotatedPolymorphism> expectedMutations = new Vector<AnnotatedPolymorphism>();

	private List<AnnotatedPolymorphism> annotatedPolymorphisms = new Vector<AnnotatedPolymorphism>();

	public AnnotatedSample(TestSample tsample) {

		sample = tsample.getSampleID();
		RankedResult topResult = tsample.getTopResult();
		SearchResultDetailed detailedResult = topResult.getSearchResult().getDetailedResult();

		clade = topResult.getHaplogroup().toString();
		clade = clade.split("_")[0];
		quality = topResult.getDistance();
		ns = PolymorphismHelper.getNCount(detailedResult.getRemainingPolysInSample());
		mixCount = PolymorphismHelper.getMixCount(detailedResult.getRemainingPolysInSample());
		coverage = PolymorphismHelper.getRangeLength(tsample.getSample().getSampleRanges().toString());

		String[] rangesWithSpaces = tsample.getSample().getSampleRanges().toString().split(";");
		ranges = new String[rangesWithSpaces.length];
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = rangesWithSpaces[i].trim();
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

	@Override
	public String toString() {
		return sample;
	}

}
