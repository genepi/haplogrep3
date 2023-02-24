package genepi.haplogrep3.haplogrep.io.readers.impl.vcf;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import core.Reference;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileStatistics;
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

			if (vc.getAlternateAlleles().size() > 1) {
				multiallelics++;
				continue;
			}

			if (vc.isIndel() || vc.isComplexIndel()) {
				indels++;
				continue;
			}

			if (vc.isFiltered()) {
				if (vc.getFilters().contains("DUP")) {
					duplicates++;
				} else {
					filterFlags++;
				}
				continue;
			}

			if (refAllele.equals(refAlleleSample)) {
				matches++;
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

	@Override
	public boolean isFailed() {
		return invalidAlleles > 0;
	}

	@Override
	public Map<String, Object> getCounters() {
		Map<String, Object> statistics = new HashMap<String, Object>();
		statistics.put("Files", files.size());
		statistics.put("Samples", samples);
		statistics.put("Variants", variants);
		statistics.put("Reference Overlap (%)", (double) matches / variants);
		statistics.put("Out Of Range Variants", outOfRange);
		statistics.put("Multiallelic Variants", multiallelics);
		statistics.put("Indel Variants", indels);
		statistics.put("Flagged Variants", filterFlags);
		statistics.put("Duplicate Variants", duplicates);
		statistics.put("Low Sample Call Rate", lowSampleCallRate);
		statistics.put("Variant Call Rate < 90%", lowVariantCallRate);
		return statistics;
	}

	public static boolean strandFlip(String reference, String refAlleleSample) {

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
