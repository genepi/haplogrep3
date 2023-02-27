package genepi.haplogrep3.model;

import java.util.Collections;
import java.util.Comparator;
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

		List<Cluster> clusters = phylotree.getClusters();

		Map<String, ClusterCount> frequencies = new HashMap<>();

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

			ClusterCount clusterCount = frequencies.get(label);

			if (clusterCount == null) {
				Cluster cluster = phylotree.getClusterByLabel(label);
				clusterCount = new ClusterCount();
				clusterCount.label = label;
				clusterCount.value = 1;
				clusterCount.color = cluster.getColor();
				frequencies.put(label, clusterCount);

			} else {
				clusterCount.value++;
			}
		}

		List<ClusterCount> sortedFrequencies = new Vector<ClusterCount>(frequencies.values());
		Collections.sort(sortedFrequencies, new Comparator<ClusterCount>() {
			@Override
			public int compare(ClusterCount o1, ClusterCount o2) {
				return -Integer.compare(o1.value, o2.value);
			}
		});

		List<String> clades = new Vector<String>();
		List<Integer> values = new Vector<Integer>();
		List<String> colors = new Vector<String>();
		for (ClusterCount clusterCount: sortedFrequencies) {
			clades.add(clusterCount.label);
			values.add(clusterCount.value);
			colors.add(clusterCount.color);
		}
		
		HashMap<String, Object> object = new HashMap<String, Object>();
		object.put("name", "Clusters");
		object.put("clades", clades);
		object.put("values", values);
		object.put("colors", colors);

		return object;

	}

	class ClusterCount {

		String label;

		String color;

		int value;

	}

}
