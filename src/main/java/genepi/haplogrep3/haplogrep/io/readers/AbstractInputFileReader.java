package genepi.haplogrep3.haplogrep.io.readers;

import java.io.File;
import java.util.List;

import core.SampleFile;
import genepi.haplogrep3.model.Phylotree;

public abstract class AbstractInputFileReader {

	public abstract boolean accepts(List<File> files, Phylotree phylotree);

	public abstract SampleFileWithStatistics read(List<File> files, Phylotree phylotree) throws Exception;

	public boolean hasFileExtensions(List<File> files, String... extensions) {
		for (File file : files) {
			boolean ok = false;
			for (String extension : extensions) {
				if (file.getName().endsWith(extension)) {
					ok = true;
				}
			}
			if (!ok) {
				return false;
			}
		}
		return true;
	}

}
