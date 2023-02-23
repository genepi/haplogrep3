package genepi.haplogrep3.web.util.functions.widgets;

import java.util.List;
import java.util.Vector;

public class PieChartData {

	private List<Object> labels = new Vector<Object>();

	private List<Object> values = new Vector<Object>();

	private List<Object> colors = new Vector<Object>();

	public PieChartData(List<Object> labels, List<Object> values, List<Object> colors) {
		this.labels = labels;
		this.values = values;
		this.colors = colors;
	}

	public List<Object> getLabels() {
		return labels;
	}

	public void setLabels(List<Object> labels) {
		this.labels = labels;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public List<Object> getColors() {
		return colors;
	}

	public void setColors(List<Object> colors) {
		this.colors = colors;
	}

}
