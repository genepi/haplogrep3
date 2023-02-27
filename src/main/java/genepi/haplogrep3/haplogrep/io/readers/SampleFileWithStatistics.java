package genepi.haplogrep3.haplogrep.io.readers;

import core.SampleFile;

public class SampleFileWithStatistics {

	private SampleFile sampleFile;
	
	private SampleFileStatistics statistics;
	
	public SampleFileWithStatistics(SampleFile sampleFile, SampleFileStatistics statistics) {
		
		this.sampleFile = sampleFile;
		this.statistics = statistics;
	}
	
	
	public SampleFile getSampleFile() {
		return sampleFile;
	}
	
	
	public SampleFileStatistics getStatistics() {
		return statistics;
	}
	
}
