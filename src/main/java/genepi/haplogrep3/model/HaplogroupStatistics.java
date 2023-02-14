package genepi.haplogrep3.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import genepi.haplogrep3.haplogrep.io.Group;

public class HaplogroupStatistics {

	private List<Map<String, Object>> groups = new Vector<>();

	public HaplogroupStatistics() {

	}

	public HaplogroupStatistics(List<AnnotatedSample> samples, Phylotree phylotree) {

		List<String> clades = new Vector<String>();
		List<Integer> values = new Vector<Integer>();

		if (phylotree.getGroups() != null) {
			for (Group group : phylotree.getGroups().getGroups()) {

				clades = new Vector<String>();
				values = new Vector<Integer>();

				for (AnnotatedSample sample : samples) {
					String clade = sample.getClade();
					String label = group.getLabel(clade);
					int index = clades.indexOf(label);
					if (index == -1) {
						clades.add(label);
						values.add(1);
					} else {
						int count = values.get(index);
						values.set(index, count + 1);
					}
				}

				HashMap<String, Object> object = new HashMap<String, Object>();
				object.put("name", group.getName());
				object.put("clades", clades);
				object.put("values", values);
				groups.add(object);
			}
		}

		clades = new Vector<String>();
		values = new Vector<Integer>();

		for (AnnotatedSample sample : samples) {
			String clade = sample.getClade();

			int index = clades.indexOf(clade);
			if (index == -1) {
				clades.add(clade);
				values.add(1);
			} else {
				int count = values.get(index);
				values.set(index, count + 1);
			}
		}
		Map<String, Object> object = new HashMap<>();
		object.put("name", "Haplogroup");
		object.put("clades", clades);
		object.put("values", values);
		groups.add(object);

	}

	public void setGroups(List<Map<String, Object>> groups) {
		this.groups = groups;
	}

	public List<Map<String, Object>> getGroups() {
		return groups;
	}

}
