package genepi.haplogrep3.haplogrep.io;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Haplogroup;
import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;

public class LabelsWriter {

	private List<Group> groups = new Vector<>();

	@SuppressWarnings("null")
	public LabelsWriter(List<Haplogroup> haploList, String filename) {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t');
		String cols[] = new String[5];
		cols[0] = "Haplogroup";
		cols[1] = "Clade";
		cols[2] = "Macro-Clade";
		cols[3] = "Phylotree-Subtree";
		cols[4] = "Macro-Haplogroup";

		writer.setColumns(cols);
		
		int countRows=0;
		
		for (Haplogroup haplogroup : haploList) {
			
			if (haplogroup.toString().equals("mt-MRCA"))
				continue;
			
			String clade = getClade(haplogroup.toString());
			String macroclade = getMacroClade(clade);

			writer.setString(cols[0], haplogroup.toString());
			writer.setString(cols[1], clade);
			writer.setString(cols[2], macroclade);
			writer.setString(cols[3], getPhylotreeSubTree(clade));
			writer.setString(cols[4], getMacroHaplogroup(macroclade));
			writer.next();
			countRows++;
		}
		System.out.println("\n" + countRows + " Labels written");
		writer.close();

	}

	private String getPhylotreeSubTree(String clade) {
		String result = "";
		if (clade.startsWith("L"))
			result = clade;
		else if (clade.equals("M7") || clade.equals("M8") || clade.equals("M9")) {
			result = clade;
		} else if (clade.startsWith("D") || clade.startsWith("G") || clade.startsWith("A") || clade.startsWith("X")
				|| clade.startsWith("M") || clade.startsWith("B")) {
			result = clade.substring(0, 1);
		} else if (clade.startsWith("C") || clade.startsWith("Z")) {
			result = "M8";
		} else if (clade.startsWith("Q")) {
			result = "M";	
		} else if (clade.startsWith("E")) {
			result = "M9";
		} else if (clade.equals("N1") || clade.equals("N2") || clade.equals("N9")) {
			result = clade;
		} else if (clade.startsWith("N") || clade.startsWith("O") || clade.startsWith("S")) {
			result = "N";
		} else if (clade.startsWith("I")) {
			result = "N1";
		} else if (clade.startsWith("W")) {
			result = "N2";
		} else if (clade.startsWith("Y")) {
			result = "N9";
		}

		else if (clade.startsWith("R0") || clade.startsWith("R9")) {
			result = clade;
		} else if (clade.startsWith("R") || clade.startsWith("P")) {
			result = "R";
		} else if (clade.startsWith("H") || clade.startsWith("V")) {
			result = "R0";
		} else if (clade.startsWith("J") || clade.startsWith("T")) {
			result = "JT";
		} else if (clade.startsWith("F")) {
			result = "R9";
		} else if (clade.startsWith("U") || clade.startsWith("K")) {
			result = "UK";
		}

		return result;
	}

	private String getMacroHaplogroup(String label) {
		String result = "";
		switch (label) {
		case "Q":
			result = "M";
			break;
		case "C":
			result = "M";
			break;
		case "Z":
			result = "M";
			break;
		case "E":
			result = "M";
			break;
		case "G":
			result = "M";
			break;
		case "D":
			result = "M";
			break;

		case "O":
			result = "N";
			break;
		case "S":
			result = "N";
			break;
		case "I":
			result = "N";
			break;
		case "W":
			result = "N";
			break;
		case "Y":
			result = "N";
			break;
		case "A":
			result = "N";
			break;
		case "X":
			result = "N";
			break;

		case "P":
			result = "R";
			break;
		case "H":
			result = "R";
			break;
		case "HV":
			result = "R";
			break;
		case "V":
			result = "R";
			break;
		case "J":
			result = "R";
			break;
		case "T":
			result = "R";
			break;
		case "F":
			result = "R";
			break;
		case "B":
			result = "R";
			break;
		case "U":
			result = "R";
			break;
		case "K":
			result = "R";
			break;
		default: result=label; break;
		}
		

		if (result.contains("L"))
			result = "L";
		if (result.contains("C")) //CZ
			result = "M";
		if (result.contains("J")) //JT
			result = "R";

		return result;
	}

	private String getMacroClade(String haplogroup) {
		String result = "";
		if (haplogroup.contains("L")) {
			Matcher matches = Pattern.compile("[A-Z]*[0-9]*").matcher(haplogroup);
			if (matches.find()) {
				result = matches.group(0);
			}
		} else {
			Matcher matches = Pattern.compile("[A-z]*").matcher(haplogroup);
			if (matches.find()) {
				result = matches.group(0);
			}
		}
		return result;
	}

	private String getClade(String name) {
		String result = "";
		Matcher matches = Pattern.compile("[A-Z]*[0-9]*").matcher(name);
		if (matches.find()) {
			result = matches.group(0);
		}
		return result;
	}
}
