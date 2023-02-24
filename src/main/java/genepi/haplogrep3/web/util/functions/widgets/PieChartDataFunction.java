package genepi.haplogrep3.web.util.functions.widgets;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.function.BiFunction;

import com.google.gson.Gson;

import genepi.haplogrep3.model.Cluster;
import genepi.haplogrep3.model.Phylotree;

public class PieChartDataFunction implements BiFunction<String, Phylotree, Object> {

	private Gson gson = new Gson();

	@Override
	public Object apply(String jsonList, Phylotree phylotree) {

		if (jsonList == null || jsonList.trim().isEmpty()) {
			return null;
		}

		List<List<Object>> list = gson.fromJson(jsonList, List.class);

		List<Object> labels = new Vector<Object>();
		List<Object> values = new Vector<Object>();
		List<Object> colors = new Vector<Object>();

		// add to treemap to sort by label name
		TreeMap<Object, Object> map = new TreeMap<Object, Object>();
		for (List<Object> pair : list) {
			map.put(pair.get(0), pair.get(1));
		}
		for (Object label : map.keySet()) {
			labels.add(label);
			values.add(map.get(label));
			if (phylotree.hasClusters()) {
				Cluster cluster = phylotree.getClusterByLabel(label.toString());
				if (cluster != null) {
					colors.add(cluster.getColor());
				} else {
					colors.add("#000000");
				}
			} else {
				colors.add("#000000");
			}
		}

		return new PieChartData(labels, values, colors);
	}

}
