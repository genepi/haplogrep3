package genepi.haplogrep3.commands;

import java.io.File;

import core.Haplogroup;
import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;
import picocli.CommandLine.Option;

public class DistanceCommand extends AbstractCommand {

	@Option(names = { "--file1" }, description = "input haplogroups", required = true)
	private String file1;

	@Option(names = { "--file2" }, description = "input haplogroups", required = true)
	private String file2;

	@Option(names = { "--tree" }, description = "Tree Id", required = true)
	private String phylotreeId;

	@Option(names = { "--output" }, description = "output haplogroups including distance", required = true)
	private String output;

	@Override
	public Integer call() {

		PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

		File file1File = new File(file1);
		if (!file1File.exists()) {
			System.out.println("Error: File1 '" + file1File.getAbsolutePath() + "' not found.");
			return 1;
		}

		File file2File = new File(file2);
		if (!file2File.exists()) {
			System.out.println("Error: File2 '" + file2File.getAbsolutePath() + "' not found.");
			return 1;
		}

		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			System.out.println("Error: Tree " + phylotreeId + " not found.");
			return 1;
		}

		CsvTableReader reader1 = new CsvTableReader(file1, ',');
		CsvTableReader reader2 = new CsvTableReader(file2, ',');

		CsvTableWriter writer = new CsvTableWriter(output, ';');

		int count = 0;

		String[] columns = new String[] { "sample", "clade1", "quality1", "clade2", "quality2", "distance" };
		writer.setColumns(columns);

		while (reader1.next()) {
			count++;

			if (!reader2.next()) {
				System.out.println("Error: File 2 has not the same number of liens as File 1");
				return 1;
			}

			String sample1 = reader1.getString("sample");
			String sample2 = reader2.getString("sample");

			if (!sample1.equals(sample2)) {
				System.out.println(
						"Error: different samples in line " + count + ": '" + sample1 + "' vs. '" + sample2 + "'");
				return 1;
			}

			writer.setString("sample", sample1);

			String clade1 = reader1.getString("clade");
			writer.setString("clade1", clade1);
			String clade2 = reader2.getString("clade");
			writer.setString("clade2", clade2);

			double quality1 = reader1.getDouble("quality");
			writer.setDouble("quality1", quality1);
			double quality2 = reader2.getDouble("quality");
			writer.setDouble("quality2", quality2);

			Haplogroup groupClade1 = new Haplogroup(clade1);
			Haplogroup groupClade2 = new Haplogroup(clade2);
			phylotree.Phylotree haplogrepPhylotree = phylotree.getPhylotreeInstance();

			int distance = 0;
			try {
				distance = haplogrepPhylotree.getDistanceBetweenHaplogroups(groupClade1, groupClade2);
			} catch (Exception e) {
				System.err.println("Error: Line " + count + " includes at least one unknown haplogroup: " + clade1
						+ " or " + clade2 + ".");
				System.exit(-1);
			}
			writer.setInteger("distance", distance);
			writer.next();

		}

		reader1.close();
		reader2.close();
		writer.close();

		System.out.println("Done.");
		System.out.println("File written to " + output + ".");

		return 0;
	}

}