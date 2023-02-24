package genepi.haplogrep3.haplogrep.io.readers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import genepi.haplogrep3.haplogrep.io.readers.impl.FastaInputFileReader;
import genepi.haplogrep3.haplogrep.io.readers.impl.HsdInputFileReader;
import genepi.haplogrep3.haplogrep.io.readers.impl.VcfInputFileReader;
import genepi.haplogrep3.model.Phylotree;

public class InputFileReaderFactory {

	private boolean chip = false;

	private double hetLevel = 0.9;
	
	private boolean skipAlignmentRules = false;

	protected List<AbstractInputFileReader> getSupportedReaders() {
		List<AbstractInputFileReader> readers = new Vector<AbstractInputFileReader>();
		readers.add(new HsdInputFileReader());
		readers.add(new VcfInputFileReader(chip, hetLevel));
		readers.add(new FastaInputFileReader(skipAlignmentRules));
		return readers;
	}

	public SampleFileWithStatistics read(List<File> files, Phylotree phylotree) throws Exception {

		for (AbstractInputFileReader reader : getSupportedReaders()) {
			if (reader.accepts(files, phylotree)) {
				return reader.read(files, phylotree);
			}
		}

		throw new IOException("Unsupported file format");
	}

	public void setChip(boolean chip) {
		this.chip = chip;
	}

	public void setHetLevel(double hetLevel) {
		this.hetLevel = hetLevel;
	}
	
	public void setSkipAlignmentRules(boolean skipAlignmentRules) {
		this.skipAlignmentRules = skipAlignmentRules;
	}

}
