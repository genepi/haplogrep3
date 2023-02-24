package genepi.haplogrep3.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class HaplogroupStatistics {

	private List<Map<String, Object>> groups = new Vector<>();

	public HaplogroupStatistics() {

	}

	public HaplogroupStatistics(List<AnnotatedSample> samples, Phylotree phylotree) {

		if (phylotree.hasClusters()) {
			HashMap<String, Object> assignedClusters = assignClusters(samples, phylotree);
			groups.add(assignedClusters);
		}

		HashMap<String, Object> result = countHaplogroups(samples);
		groups.add(result);

	}

	public void setGroups(List<Map<String, Object>> clusters) {
		this.groups = clusters;
	}

	public List<Map<String, Object>> getGroups() {
		return groups;
	}

	protected HashMap<String, Object> countHaplogroups(List<AnnotatedSample> samples) {

		List<String> clades = new Vector<String>();
		List<Integer> values = new Vector<Integer>();

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

		HashMap<String, Object> object = new HashMap<String, Object>();
		object.put("name", "Haplogroup");
		object.put("clades", clades);
		object.put("values", values);

		return object;

	}

	protected HashMap<String, Object> assignClusters(List<AnnotatedSample> samples, Phylotree phylotree) {

		List<String> clades = new Vector<String>();
		List<Integer> values = new Vector<Integer>();
		List<String> colors = new Vector<String>();

		List<Cluster> clusters = phylotree.getClusters();

		for (Cluster cluster : clusters) {
			clades.add(cluster.getLabel());
			values.add(0);
			colors.add(cluster.getColor());
		}

		for (AnnotatedSample sample : samples) {

			String clade = sample.getClade();
			String label = "unknown";
			try {
				String cluster = phylotree.getNearestCluster(clusters, clade);
				if (cluster != null) {
					label = cluster;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

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
		object.put("name", "Clusters");
		object.put("clades", clades);
		object.put("values", values);
		object.put("colors", colors);

		return object;

	}

}
