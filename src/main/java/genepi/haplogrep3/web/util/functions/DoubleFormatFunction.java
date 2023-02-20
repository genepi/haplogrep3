package genepi.haplogrep3.web.util.functions;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;

public class DoubleFormatFunction implements Function<Double, String> {
	
	public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", symbols);

	@Override
	public String apply(Double number) {

			return DECIMAL_FORMAT.format((double) number);


	}

}