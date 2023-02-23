package genepi.haplogrep3.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import genepi.io.FileUtil;
import genepi.io.table.reader.CsvTableReader;

public class ClassifyCommandTest {

	public static String PHYLOTREE = "phylotree-fu-rcrs@1.0";

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

	@Test
	public void testFastaWithAlignmentRules() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/fasta/InsertionTest3.fasta";
		command.phylotreeId = PHYLOTREE;
		command.writeFasta = true;
		command.extendedReport = true;
		command.output = FileUtil.path(output, "InsertionTest3.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);

		CsvTableReader reader = new CsvTableReader(FileUtil.path(output, "InsertionTest3.txt"), '\t');
		assertTrue(reader.next());
		HashSet<String> set = new HashSet<String>();

		for (String polymorphism : reader.getString("Input_Sample").split(" ")) {
			set.add(polymorphism);
		}
		assertFalse(reader.next());
		reader.close();

		assertFalse(set.contains("309.1CCT"));
		assertTrue(set.contains("309.1C"));
		assertTrue(set.contains("309.2C"));
		assertTrue(set.contains("315.1C"));
	}

	@Test
	public void testFastaWithoutAlignmentRules() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/fasta/InsertionTest3.fasta";
		command.phylotreeId = PHYLOTREE;
		command.writeFasta = true;
		command.extendedReport = true;
		command.skipAlignmentRules = true;
		command.output = FileUtil.path(output, "InsertionTest3.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);

		CsvTableReader reader = new CsvTableReader(FileUtil.path(output, "InsertionTest3.txt"), '\t');
		assertTrue(reader.next());
		HashSet<String> set = new HashSet<String>();

		for (String polymorphism : reader.getString("Input_Sample").split(" ")) {
			set.add(polymorphism);
		}
		assertFalse(reader.next());
		reader.close();

		assertTrue(set.contains("309.1CCT"));
		assertFalse(set.contains("309.1C"));
		assertFalse(set.contains("309.2C"));
		assertFalse(set.contains("315.1C"));
	}

	@Test
	public void testHSDtoFASTAWithAlignmentRules() throws Exception {

		String output = "test-data/Phylotree17_FU1a";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		// 1. classify the HSD, write extended report to compare and write FASTA file
		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/hsd/Phylotree17_FU1a.hsd";
		command.phylotreeId = PHYLOTREE;
		command.writeFasta = true;
		command.extendedReport = true;
		command.output = FileUtil.path(output, "Phylotree17_FU1a.txt");

		int exitCode = command.call();

		command = new ClassifyCommand();
		command.input = FileUtil.path(output, "Phylotree17_FU1a.fasta");
		command.phylotreeId = PHYLOTREE;
		command.extendedReport = true;
		command.output = FileUtil.path(output, "Phylotree17_FU1a_fromFasta.txt");

		exitCode = command.call();

		assertEquals(0, exitCode);

		CsvTableReader readerHSDout = new CsvTableReader(FileUtil.path(output, "Phylotree17_FU1a.txt"), '\t');
		assertTrue(readerHSDout.next());
		HashMap<String, String> setHSD = new HashMap<String, String>();

		while (readerHSDout.next()) {
			for (String polymorphism : readerHSDout.getString("Not_Found_Polys").split(" ")) {
				String key= readerHSDout.getString("SampleID");
				if (setHSD.get(key)==null){
					setHSD.put(key, polymorphism);
				}
				else {
					setHSD.put(key, setHSD.get(key) + " "+ polymorphism);
				}
				
			}
		}
		readerHSDout.close();

		CsvTableReader readerFASTAout = new CsvTableReader(FileUtil.path(output, "Phylotree17_FU1a_fromFasta.txt"),
				'\t');
		assertTrue(readerFASTAout.next());

		HashMap<String, String> setFASTAmiss = new HashMap<String, String>();
		HashMap<String, String> setFASTAremaining = new HashMap<String, String>();

		while (readerFASTAout.next()) {
			for (String polymorphism : readerFASTAout.getString("Not_Found_Polys").split(" ")) {
				String key= readerFASTAout.getString("SampleID");
				if (setFASTAmiss.get(key)==null){
					setFASTAmiss.put(key, polymorphism);
				}
				else {
					setFASTAmiss.put(key, setFASTAmiss.get(key) + " "+ polymorphism);
				}
				
			}
			for (String polymorphism : readerFASTAout.getString("Remaining_Polys").split(" ")) {
				String key =  readerFASTAout.getString("SampleID");
				
				if (setFASTAremaining.get(key)==null){
					setFASTAremaining.put(key, polymorphism);
				}
				else {
					setFASTAremaining.put(key, setFASTAremaining.get(key) + " "+ polymorphism);
				}
			}
		}
		readerFASTAout.close();

		for (Map.Entry<String, String> set : setFASTAmiss.entrySet()) {
			String missingFasta = set.getValue();
			String missingHSD = setHSD.get(set.getKey());
			if (!missingFasta.equals(missingHSD)) {
				System.out.print(set.getKey() + "\t" + set.getValue());
				System.out.println("\t" + setHSD.get(set.getKey()) + "\t" + setFASTAremaining.get(set.getKey()));
			}
		}
		System.out.println(setHSD.size());
		assertEquals(setHSD.size(), setFASTAmiss.size());

	}

	@Test
	public void testWithHsdAndQualityControlOutput() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/hsd/H100.hsd";
		command.phylotreeId = PHYLOTREE;
		command.writeQc = true;
		command.output = FileUtil.path(output, "H100.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.txt")));

		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.qc.txt").replaceAll("\\.", ","),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.qc.txt")).replaceAll("\\.", ","));

	}

	@Test
	public void testWithFastaAndQualityControlOutput() throws Exception {

		String output = "test-output";
		FileUtil.deleteDirectory(output);
		FileUtil.createDirectory(output);

		ClassifyCommand command = new ClassifyCommand();
		command.input = "test-data/fasta/H100.fasta";
		command.phylotreeId = PHYLOTREE;
		command.writeQc = true;
		command.output = FileUtil.path(output, "H100.txt");

		int exitCode = command.call();

		assertEquals(0, exitCode);
		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.HM625681.1.txt"),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.txt")));

		assertEquals(FileUtil.readFileAsString("test-data/expected/H100/H100.HM625681.1.qc.txt").replaceAll("\\.", ","),
				FileUtil.readFileAsString(FileUtil.path(output, "H100.qc.txt")).replaceAll("\\.", ","));

	}

}
