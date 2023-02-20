package genepi.haplogrep3.haplogrep.io;

import core.Haplogroup;
import genepi.haplogrep3.model.Phylotree;
import genepi.io.table.writer.CsvTableWriter;

public class HaplogroupClustering {

	static String[] TOPLEVEL_HAPLOGROUPS_TOP_DOWN = new String[] { "L0", "L1", "L5", "L2", "L6", "L4", "L3", "M", "C", "Z", "E",
			"G", "Q", "D", "N", "Y", "A", "O", "S", "F", "B", "P", "I", "W", "X", "R", "HV", "V", "H", "J", "T", "U",
			"K" };
	
	static String[] TOPLEVEL_HAPLOGROUPS_DOWN_TOP = new String[] { "K", "U", "T", "J", "H", "V", "HV", "R", "X", "W", "I", "P",
			"B", "F", "S", "O", "A", "Y", "N", "D", "Q", "G", "E", "Z", "C", "M", "L3", "L4", "L6", "L2", "L5", "L1",
			"L0" };

	public HaplogroupClustering(Phylotree phylotree, String filename) {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t');

		String[] columnsWrite = { "Haplogroup", "Super Haplogroup" };
		writer.setColumns(columnsWrite);

		for (Haplogroup haplogroup : phylotree.getHaplogroups()) {

			for (String topLevelHaplogroup : TOPLEVEL_HAPLOGROUPS_TOP_DOWN) {
				boolean hit = new Haplogroup(topLevelHaplogroup).isSuperHaplogroup(phylotree.getPhylotreeInstance(),
						haplogroup);

				if (hit) {
					writer.setString("Haplogroup", haplogroup.toString());
					writer.setString("Super Haplogroup", topLevelHaplogroup);
					writer.next();
					break;
				}

			}

		}

		System.out.println("Done");
		writer.close();

	}

}
