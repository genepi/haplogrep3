package genepi.haplogrep3.haplogrep.io;

import core.Haplogroup;
import genepi.haplogrep3.model.Phylotree;
import genepi.io.table.writer.CsvTableWriter;

public class HaplogroupClustering {

	static String[] TOPLEVEL_HAPLOGROUPS_TOP_DOWN = new String[] { "L0", "L1", "L5", "L2", "L6", "L4", "L3", "M", "C",
			"Z", "E", "G", "Q", "D", "N", "Y", "A", "O", "S", "F", "B", "P", "I", "W", "X", "R", "HV", "V", "H", "J",
			"T", "U", "K"};
	
	public HaplogroupClustering(Phylotree phylotree, String filename) {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t');

		String[] columnsWrite = { "Haplogroup", "Super Haplogroup", "Distance" };
		writer.setColumns(columnsWrite);

		phylotree.Phylotree phylotreeInstance = phylotree.getPhylotreeInstance();

		for (Haplogroup haplogroup : phylotree.getHaplogroups()) {

			int distanceTmp = -1;
			String topLevelTmp = null;

			for (String topLevelHaplogroup : TOPLEVEL_HAPLOGROUPS_TOP_DOWN) {

				boolean hit = new Haplogroup(topLevelHaplogroup).isSuperHaplogroup(phylotreeInstance, haplogroup);

				if (hit) {

					try {

						int distance = phylotreeInstance
								.getDistanceBetweenHaplogroups(new Haplogroup(topLevelHaplogroup), haplogroup);
						
						if (distanceTmp == -1 || distance <= distanceTmp) {

							topLevelTmp = topLevelHaplogroup;
							distanceTmp = distance;

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			// TODO Haplogroups like H2a2a1 dont have a super haplogroup. Problem with
			// parsing?
			if (topLevelTmp != null) {
				writer.setString("Haplogroup", haplogroup.toString());
				writer.setString("Super Haplogroup", topLevelTmp);
				writer.setInteger("Distance", distanceTmp);
				writer.next();
			}

		}

		System.out.println("Done");
		writer.close();

	}

}
