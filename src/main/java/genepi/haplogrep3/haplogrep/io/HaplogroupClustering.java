package genepi.haplogrep3.haplogrep.io;

import java.util.List;

import core.Haplogroup;
import genepi.haplogrep3.model.Cluster;
import genepi.haplogrep3.model.Phylotree;
import genepi.io.table.writer.CsvTableWriter;

public class HaplogroupClustering {

	// static String[] TOPLEVEL_HAPLOGROUPS_TOP_DOWN = new String[] { "L0", "L1",
	// "L5", "L2", "L6", "L4", "L3", "M", "C",
	// "Z", "E", "G", "Q", "D", "N", "Y", "A", "O", "S", "F", "B6", "P", "I", "W",
	// "X", "R", "HV", "V", "H", "J",
	// "T", "U", "K"};

	public HaplogroupClustering(Phylotree phylotree, List<Cluster> clusters, String filename) {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t');

		String[] columnsWrite = { "Haplogroup", "Super Haplogroup" };
		writer.setColumns(columnsWrite);

		for (Haplogroup haplogroup : phylotree.getHaplogroups()) {

			try {
				String topLevelHaplogroup = phylotree.getNearestCluster(clusters, haplogroup.toString());

				// TODO Haplogroups like H2a2a1 dont have a super haplogroup. Problem with
				// parsing?
				if (topLevelHaplogroup != null) {
					writer.setString("Haplogroup", haplogroup.toString());
					writer.setString("Super Haplogroup", topLevelHaplogroup);
					writer.next();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		System.out.println("Done");
		writer.close();

	}

}
