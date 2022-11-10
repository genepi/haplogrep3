package genepi.haplogrep3.web.util.functions;
import java.text.DecimalFormat;
import java.util.function.Function;

public class PercentageFunction implements Function<Object, String> {

	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##'%'");

	@Override
	public String apply(Object number) {
		
		if (number instanceof Double) {
			
			double percentage = (double) number * 100;
			return DECIMAL_FORMAT.format(percentage);
			
		} else if (number instanceof Float) {
			
			double percentage = (float) number * 100;
			return DECIMAL_FORMAT.format(percentage);
			
		} else {
			
			return "NaN%";
			
		}
		
	}
	
}