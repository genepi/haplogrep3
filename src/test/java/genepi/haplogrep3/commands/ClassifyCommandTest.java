package genepi.haplogrep3.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import genepi.io.FileUtil;

public class ClassifyCommandTest {

	public static String PHYLOTREE = "phylotree-17-fu-rcrs";

	@Test
	public void testWithHsd() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/hsd/H100.hsd";
		command.phylotreeId = PHYLOTREE;
		command.output = FileUtil.path(output, "H100.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.txt")));
	}
	
	@Test
	public void testWithFasta() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/fasta/H100.fasta";
		command.phylotreeId = PHYLOTREE;
		command.output = FileUtil.path(output, "H100.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.HM625681.1.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.txt")));
	}

	@Test
	public void testWithHsdAndHits() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/hsd/H100.hsd";
		command.phylotreeId = PHYLOTREE;
		command.hits = 10;
		command.output = FileUtil.path(output, "H100.10.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.10.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.10.txt")));
	}

	@Test
	public void testWithHsdAndFastaOutput() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/hsd/H100.hsd";
		command.phylotreeId = PHYLOTREE;
		command.writeFasta = true;
		command.output = FileUtil.path(output, "H100.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.txt")));

		// Reference is now loaded from fasta --> sequence is uppercase. In
		// haplogrep-cmd it was lowercase.
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.fasta").toUpperCase(),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.fasta")).toUpperCase());
	}

}
