package genepi.haplogrep3.web.util.functions;
import java.text.DecimalFormat;
import java.util.function.Function;

public class DoubleFormatFunction implements Function<Double, String> {

	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	@Override
	public String apply(Double number) {

			return DECIMAL_FORMAT.format((double) number);


	}

}