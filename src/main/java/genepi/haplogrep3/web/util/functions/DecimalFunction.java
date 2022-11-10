package genepi.haplogrep3.web.util.functions;
import java.text.DecimalFormat;
import java.util.function.Function;

public class DecimalFunction implements Function<Object, String> {

	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###");

	@Override
	public String apply(Object number) {

		if (number instanceof Integer) {

			return DECIMAL_FORMAT.format( (int) number);

		} else if (number instanceof Long) {

			return DECIMAL_FORMAT.format((long) number);

		} else if (number instanceof Double) {

			return DECIMAL_FORMAT.format((double) number);

		} else if (number instanceof Float) {

			return DECIMAL_FORMAT.format((float) number);

		} else {

			return "NaN";

		}

	}

}