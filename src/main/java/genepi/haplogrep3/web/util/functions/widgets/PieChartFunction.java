package genepi.haplogrep3.web.util.functions.widgets;

import java.util.function.BiFunction;

import com.google.gson.Gson;

public class PieChartFunction implements BiFunction<String, PieChartData, String> {

	private Gson gson = new Gson();

	@Override
	public String apply(String id, PieChartData data) {

		if (data == null) {
			return "";
		}

		try {
			StringBuffer code = new StringBuffer();
			code.append("var data = {");
			code.append("labels: " + gson.toJson(data.getLabels()) + ",");
			code.append("datasets: [{");
			code.append("label: 'Samples',");
			code.append("data: " + gson.toJson(data.getValues()) + ",");
			code.append("backgroundColor: " + gson.toJson(data.getColors()));
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