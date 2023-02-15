package genepi.haplogrep3.haplogrep.io;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class Group {

	private String name;

	private TreeMap<String, Set<String>> labels = new TreeMap<>();

	public Group(String name) {
		this.name = name;
	}

	public void addLabel(String haplogroup, String label) {
		Set<String> haplogroups = labels.get(label);
		if (haplogroups == null) {
			haplogroups = new HashSet<>();
			labels.put(label, haplogroups);
		}
		haplogroups.add(haplogroup);

	}

	public String getLabel(String haplogroup) {
		for (String label : labels.keySet()) {
			if (labels.get(label).contains(haplogroup)) {
				return label;
			}
		}
		return "unkown";
	}

	public List<String> getLabels() {
		return new Vector<String>(labels.keySet());
	}

	public String getName() {
		return name;
	}

	public Set<String> getHaplogroupsByLabel(String label) {
		if (labels.containsKey(label)) {
			return labels.get(label);
		}
		return new HashSet<String>();
	}

}
