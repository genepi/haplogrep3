package genepi.haplogrep3.haplogrep.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author hansi
 *
 */
public class WeightsWriter {

	public static double WEIGHT_VOC = 2.0;

	public static void writeToTxt(String filename, PhylotreeDto phylotree, VariantsOfConcern variantsOfConcern)
			throws Exception {

		List<HaplogroupDto> haplolists = phylotree.getHaplogroups();

		Map<String, Set<String>> poliesToHaplogroups = new HashMap<>();
		writeTreeRecursive(haplolists, poliesToHaplogroups);

		List<PhylotreePoly> polymorphisms = new Vector<PhylotreePoly>();
		for (String poly : poliesToHaplogroups.keySet()) {
			polymorphisms.add(new PhylotreePoly(poly, 1.0 / poliesToHaplogroups.get(poly).size()));
		}

		// sort and write
		Collections.sort(polymorphisms);

		BufferedWriter weightsWriter = new BufferedWriter(new FileWriter(filename));

		for (PhylotreePoly ph : polymorphisms) {

			int pos = Integer.valueOf(ph.getName().substring(0, ph.getName().length() - 1));

			String mutation = ph.getName().substring(ph.getName().length() - 1, ph.getName().length());

			boolean variantOfConcern = variantsOfConcern.includes(pos, mutation);

			weightsWriter.write(
					ph.getName() + "\t" + ph.getAmount() + "\t" + (ph.getAmount()) + "\t" + variantOfConcern + "\n");

		}

		weightsWriter.close();
	}

	private static void writeTreeRecursive(List<HaplogroupDto> haplogroup, Map<String, Set<String>> poliesToHaplogroups)

			throws IOException {

		for (int i = 0; i < haplogroup.size(); i++) {
			List<String> poly = haplogroup.get(i).getDetails();
			for (int j = 0; j < poly.size(); j++) {
				Set<String> haplogroups = poliesToHaplogroups.get(poly.get(j));
				if (haplogroups == null) {
					haplogroups = new HashSet<String>();
					poliesToHaplogroups.put(poly.get(j), haplogroups);
				}
				String clade = haplogroup.get(i).getName().split("_")[0];
				haplogroups.add(clade);
			}

			writeTreeRecursive(haplogroup.get(i).getHaplogroups(), poliesToHaplogroups);
		}

	}

}
