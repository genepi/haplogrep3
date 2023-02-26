package genepi.haplogrep3.haplogrep.io.readers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import genepi.haplogrep3.haplogrep.io.readers.impl.StatisticCounterType;

public class StatisticCounter {

	private String label = "";

	private String value = "";

	private StatisticCounterType type = StatisticCounterType.INFO;

	public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
	static {
		symbols.setGroupingSeparator(',');
	}

	public static final DecimalFormat DECIMAL_FORMAT_INTEGER = new DecimalFormat("###,###", symbols);

	public static final DecimalFormat DECIMAL_FORMAT_DOUBLE = new DecimalFormat("#.##", symbols);

	public StatisticCounter() {

	}

	public StatisticCounter(String label, Object value) {
		this(label, value, -1);
	}

	public StatisticCounter(String label, Object value, double threshold) {
		this.label = label;

		if (value instanceof Integer) {

			this.value = DECIMAL_FORMAT_INTEGER.format((Integer) value);
			if (threshold != -1 && (Integer) value > threshold) {
				type = StatisticCounterType.WARNING;
			}

		} else if (value instanceof Double || value instanceof Float) {

			this.value = DECIMAL_FORMAT_DOUBLE.format((Double) value);
			if (threshold != -1 && (Integer) value > threshold) {
				type = StatisticCounterType.WARNING;
			}

		} else {

			this.value = value.toString();

		}

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StatisticCounterType getType() {
		return type;
	}

	public void setType(StatisticCounterType type) {
		this.type = type;
	}

}
