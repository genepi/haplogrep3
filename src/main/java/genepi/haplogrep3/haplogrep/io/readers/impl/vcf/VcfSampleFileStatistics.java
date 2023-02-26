package genepi.haplogrep3.haplogrep.io.readers.impl.vcf;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import core.Reference;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileStatistics;
import genepi.haplogrep3.haplogrep.io.readers.StatisticCounter;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class VcfSampleFileStatistics extends SampleFileStatistics {

	private static final double VARIANT_CALL_RATE = 0.90;

	private static final double SAMPLE_CALL_RATE = 0.5;

	private int variants = 0;

	private int samples = 0;

	private int matches = 0;

	private int invalidAlleles = 0;

	private int outOfRange = 0;

	private int indels = 0;

	private int duplicates = 0;

	private int filterFlags = 0;

	private int strandFlips = 0;

	private int multiallelics = 0;

	private int lowSampleCallRate = 0;

	private int lowVariantCallRate = 0;

	private Reference reference;

	private int[] snpsPerSampleCount;

	private List<File> files;

	public VcfSampleFileStatistics(List<File> files, Reference reference) {
		this.files = files;
		this.reference = reference;

		for (File file : files) {
			processVcfFile(file, reference);
		}
	}

	private void processVcfFile(File file, Reference reference) {

		final VCFFileReader vcfReader = new VCFFileReader(file, false);
		VCFHeader vcfHeader = vcfReader.getFileHeader();

		samples += vcfHeader.getGenotypeSamples().size();

		snpsPerSampleCount = new int[samples];
		for (int i = 0; i < samples; i++) {
			snpsPerSampleCount[i] = 0;
		}

		for (final VariantContext vc : vcfReader) {

			variants++;

			int position = vc.getStart();
			String refAllele = reference.getReferenceBase(position);
			String refAlleleSample = vc.getReference().getBaseString();

			if (refAllele == null) {
				outOfRange++;
				continue;
			}

			if (refAllele.equals(refAlleleSample)) {
				matches++;
			}

			if (isStrandFlip(refAllele, refAlleleSample)) {
				strandFlips++;
				continue;
			}

			if (isFiltered(vc)) {

				if (vc.getFilters().contains("DUP")) {
					duplicates++;
				} else {
					filterFlags++;
				}
				continue;
			}

			if (vc.getAlternateAlleles().size() > 1) {
				multiallelics++;
				continue;
			}

			if (vc.isIndel() || vc.isComplexIndel()) {
				indels++;
				continue;
			}

			if (1 - (vc.getNoCallCount() / (double) vc.getNSamples()) < VARIANT_CALL_RATE) {
				lowVariantCallRate++;
			}

			int i = 0;
			for (String sample : vcfHeader.getSampleNamesInOrder()) {
				Genotype genotype = vc.getGenotype(sample);
				if (genotype.isCalled()) {
					snpsPerSampleCount[i] += 1;
				}
				i++;
			}

		}

		for (int i = 0; i < snpsPerSampleCount.length; i++) {
			int snps = snpsPerSampleCount[i];
			double sampleCallRate = (double) snps / matches;
			if (sampleCallRate < SAMPLE_CALL_RATE) {
				lowSampleCallRate++;
			}
		}

		vcfReader.close();

	}

	public boolean isFiltered(VariantContext vc) {
		Set<String> filters = vc.getFilters();
		return vc.isFiltered() && !filters.contains("PASS") && !filters.contains(".") && !filters.contains("fa");
	}

	@Override
	public boolean isFailed() {
		return invalidAlleles > 0;
	}

	@Override
	public List<StatisticCounter> getCounters() {

		List<StatisticCounter> statistics = new Vector<StatisticCounter>();

		double referenceOverlap = (matches / (double) variants) * 100;

		statistics.add(new StatisticCounter("Files", files.size()));
		statistics.add(new StatisticCounter("Samples", samples));
		statistics.add(new StatisticCounter("Input Variants", variants));
		statistics.add(new StatisticCounter("Reference Overlap (%)", referenceOverlap));
		statistics.add(new StatisticCounter("Strand Flips", strandFlips, 0));
		statistics.add(new StatisticCounter("Out Of Range Variants", outOfRange));
		statistics.add(new StatisticCounter("Multiallelic Variants", multiallelics));
		statistics.add(new StatisticCounter("Indel Variants", indels));
		statistics.add(new StatisticCounter("VCF Filtered Variants", filterFlags));
		statistics.add(new StatisticCounter("Duplicate Variants", duplicates));
		statistics.add(new StatisticCounter("Low Sample Call Rate", lowSampleCallRate));
		// TODO: use VARIANT_CALL_RATE instead of 90% and extract labels to constants
		statistics.add(new StatisticCounter("Variant Call Rate < 90%", lowVariantCallRate));

		return statistics;
	}

	public static boolean isStrandFlip(String reference, String refAlleleSample) {

		if (refAlleleSample.equals("A")) {
			return reference.equals("T");
		} else if (refAlleleSample.equals("T")) {
			return reference.equals("A");
		} else if (refAlleleSample.equals("C")) {
			return reference.equals("G");
		} else if (refAlleleSample.equals("G")) {
			return reference.equals("C");
		}

		return false;

	}

}
