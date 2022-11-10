package genepi.haplogrep3.util;

import java.util.Comparator;
import java.util.List;

import core.TestSample;

public class TestSampleHelper {

	public static void sortBySampleId(List<TestSample> samplesH) {
		samplesH.sort(new Comparator<TestSample>() {
			@Override
			public int compare(TestSample arg0, TestSample arg1) {
				return arg0.getSampleID().compareTo(arg1.getSampleID());
			}
		});
	}

}
