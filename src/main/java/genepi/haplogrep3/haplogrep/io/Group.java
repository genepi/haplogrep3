package genepi.haplogrep3.haplogrep.io;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Group {

	private String name;

	private List<String> labels = new Vector<>();

	private List<Set<String>> haplogroups = new Vector<>();

	public Group(String name) {
		this.name = name;
	}

	public void addLabel(String haplogroup, String label) {
		int index = labels.indexOf(label);
		if (index == -1) {
			labels.add(label);
			HashSet<String> set = new HashSet<>();
			set.add(haplogroup);
			haplogroups.add(set);
		} else {
			haplogroups.get(index).add(haplogroup);
		}
	}

	public String getLabel(String haplogroup) {
		for (int i = 0; i < labels.size(); i++) {
			if (haplogroups.get(i).contains(haplogroup)) {
				return labels.get(i);
			}
		}
		return "unkown";
	}

	public String getName() {
		return name;
	}

}
