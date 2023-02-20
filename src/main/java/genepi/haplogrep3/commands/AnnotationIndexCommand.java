package genepi.haplogrep3.commands;

import java.io.File;
import java.io.IOException;

import genepi.haplogrep3.haplogrep.io.annotation.IndexFileWriter;
import picocli.CommandLine.Option;

public class AnnotationIndexCommand extends AbstractCommand {

	@Option(names = { "--file" }, description = "file", required = true)
	private String input;

	@Option(names = { "-s", "--start" }, description = "start column", required = true)
	private int start;

	@Option(names = { "-S", "--skip" }, description = "skip n lines", required = true)
	private int skip;

	@Override
	public Integer call() throws IOException {

		File file = new File(input);

		if (!file.exists()) {
			System.out.println("Error: Inpur file '" + file.getAbsolutePath() + "' not found.");
			return 1;
		}

		File indexFile = new File(input + ".index");

		IndexFileWriter writer = new IndexFileWriter(file);
		writer.buildIndex(indexFile, start, skip);

		return 0;

	}

	public void setFile(String input) {
		this.input = input;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void setStart(int start) {
		this.start = start;
	}

}