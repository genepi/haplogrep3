package genepi.haplogrep3.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import core.Mutations;
import core.Polymorphism;
import core.TestSample;
import genepi.haplogrep3.model.Phylotree;

public class PolymorphismHelper {

	public static boolean isNotN(Polymorphism polymorphism) {

		return (polymorphism.getMutation() != Mutations.N);

	}

	public static boolean isInSGene(Polymorphism polymorphism) {

		return (polymorphism.getPosition() > 21563 && polymorphism.getPosition() < 25384);

	}

	public static boolean isInSGeneAAC(String aac) {

		return (aac.contains("S:"));

	}

	public static int getNCount(ArrayList<Polymorphism> poly) {
		int countNs = 0;
		for (int i = 0; i < poly.size(); i++) {
			if (poly.get(i).getMutation() == Mutations.N)
				countNs++;
		}
		return countNs;
	}

	public static int getMixCount(ArrayList<Polymorphism> poly) {
		int countMix = 0;
		for (int i = 0; i < poly.size(); i++) {
			if (poly.get(i).getPosition() > 0) {
				switch (poly.get(i).getMutation()) {
				case R:
					countMix++;
					break;
				case Y:
					countMix++;
					break;
				case K:
					countMix++;
					break;
				case M:
					countMix++;
					break;
				case S:
					countMix++;
					break;
				case W:
					countMix++;
					break;
				case H:
					countMix++;
					break;
				default:
					break;
				}
			}
		}
		return countMix;

	}

	public static Integer getRangeLength(String range) {
		int countBasesCovered = 0;
		StringTokenizer st = new StringTokenizer(range, ";");
		while (st.hasMoreElements()) {
			String rangeEntry = st.nextToken();
			int start = 0;
			int stop = 0;
			if (rangeEntry.contains("-")) {
				start = Integer.valueOf(rangeEntry.split("-")[0].trim());
				stop = Integer.valueOf(rangeEntry.split("-")[1].trim());
				countBasesCovered += stop - start + 1;
			} else if (!rangeEntry.trim().equals("0")) {
				countBasesCovered++;
			}
		}
		return countBasesCovered;
	}

	public static double getWarnings(TestSample sample) {
		double quality = sample.getResults().get(0).getDistance();
		BigDecimal bd = new BigDecimal(quality).setScale(2, RoundingMode.HALF_UP);
		double result = bd.doubleValue();
		return result;
	}

	public static String getLabel(Polymorphism polymorphism) {
		if (polymorphism.isBackMutation()) {
			return polymorphism.getPosition() + "!";

		}
		return polymorphism.getPosition() + polymorphism.getMutation().toString();
	}

	public static String getType(Polymorphism polymorphism, Phylotree phylotree) {
		if (phylotree.getPhylotreeInstance().getMutationRate(polymorphism) == 0) {
			if (phylotree.getPhylotreeInstance().isHotspot(polymorphism)) {
				return "hotspot";
			} else {
				return "global private mutation";
			}
		} else {
			return "local private mutation";
		}
	}

	public static void sortByPosition(List<Polymorphism> polymorphisms) {
		polymorphisms.sort(new Comparator<Polymorphism>() {

			@Override
			public int compare(Polymorphism arg0, Polymorphism arg1) {
				return Integer.compare(arg0.getPosition(), arg1.getPosition());
			}
		});

	}

}