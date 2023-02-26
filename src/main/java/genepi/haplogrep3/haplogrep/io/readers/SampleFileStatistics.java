package genepi.haplogrep3.haplogrep.io.readers;

import java.util.List;

public abstract class SampleFileStatistics {

	public SampleFileStatistics() {

	}

	public abstract boolean isFailed();

	public abstract List<StatisticCounter> getCounters();

}
