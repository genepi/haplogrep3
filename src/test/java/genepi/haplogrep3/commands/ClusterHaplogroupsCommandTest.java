package genepi.haplogrep3.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import genepi.io.FileUtil;
import genepi.io.table.reader.CsvTableReader;

public class ClusterHaplogroupsCommandTest {

	public static String PHYLOTREE = "phylotree-fu-rcrs@1.0";

	@Test
	public void testClusteringPhylotreeFU() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClusterHaplogroupsCommand command = new ClusterHaplogroupsCommand();
		command.tree = PHYLOTREE;
		command.output = FileUtil.path(output, "test.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);

	}

}
