package genepi.haplogrep3.haplogrep.io.readers;

import java.util.Map;

public abstract class SampleFileStatistics {

	public SampleFileStatistics() {

	}

	public abstract boolean isFailed();

	public abstract Map<String, Object> getCounters();

}
