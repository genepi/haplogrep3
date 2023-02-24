package genepi.haplogrep3.haplogrep.io.readers.impl.vcf;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Reference;
import genepi.haplogrep3.haplogrep.io.readers.SampleFileStatistics;

public class VcfSampleFileStatistics extends SampleFileStatistics {

	private int snps;

	private int invalidAlleles = 0;

	private int alleleMismatch = 0;

	private int alleleSwitch = 0;

	private int ambigousSnps = 0;

	private int duplicates = 0;

	private int filterFlag = 0;

	private int multiallelicSites = 0;

	private Reference reference;
	
	private List<File> files;

	public VcfSampleFileStatistics(List<File> files, Reference reference) {
		this.files = files;
		this.reference = reference;
		for (File file : files) {
			processVcfFile(file, reference);
		}
	}

	private void processVcfFile(File file, Reference reference) {
		// TODO: count....
		//reference.getAllele(posi); TODO:??
	}

	@Override
	public boolean isFailed() {
		return invalidAlleles > 0;
	}

	@Override
	public Map<String, Object> getCounters() {
		Map<String, Object> statistics = new HashMap<String, Object>();
		statistics.put("Invalid Alleles", 0);
		statistics.put("Files", files.size());
		return statistics;
	}

}
