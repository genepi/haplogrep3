package genepi.haplogrep3.web.util.functions.widgets;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.function.BiFunction;

import com.google.gson.Gson;

public class PieChartFunction implements BiFunction<String, String, String> {

	private Gson gson = new Gson();

	@Override
	public String apply(String id, String jsonList) {

		if (jsonList == null || jsonList.trim().isEmpty()) {
			return "";
		}

		try {
			List<List<Object>> list = gson.fromJson(jsonList, List.class);

			List<Object> labels = new Vector<Object>();
			List<Object> values = new Vector<Object>();

			//add to treemap to sort by label name
			TreeMap<Object, Object> map = new TreeMap<Object, Object>();
			for (List<Object> pair : list) {
				map.put(pair.get(0), pair.get(1));
			}
			for (Object label: map.keySet()) {
				labels.add(label);
				values.add(map.get(label));
			}
			
			

			StringBuffer code = new StringBuffer();
			code.append(
					"var Tableau20 = ['#4E79A7', '#A0CBE8', '#F28E2B', '#FFBE7D', '#59A14F', '#8CD17D', '#B6992D', '#F1CE63', '#499894', '#86BCB6', '#E15759', '#FF9D9A', '#79706E', '#BAB0AC', '#D37295', '#FABFD2', '#B07AA1', '#D4A6C8', '#9D7660', '#D7B5A6'];\n"
							+ "");
			code.append("var data = {");
			code.append("labels: " + gson.toJson(labels) + ",");
			code.append("datasets: [{");
			code.append("label: 'Samples',");
			code.append("data: " + gson.toJson(values) + ",");
			code.append("backgroundColor: Tableau20");
			code.append("}]");
			code.append("};");
			code.append("var config = {");
			code.append("type: 'doughnut',");
			code.append("data: data,");
			code.append("options: {");
			code.append("plugins: {");
			code.append("legend: {");
			code.append("position: 'right', labels:{boxWidth: 20}");
			code.append("}");
			code.append("},");
			code.append("animation: {");
			code.append("animateRotate: false");
			code.append("}");
			code.append("},");
			code.append("responsive: true,");
			code.append("maintainAspectRatio: false");
			code.append("};");

			code.append("chart = new Chart(document.getElementById('" + id + "'), config);");

			return code.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}