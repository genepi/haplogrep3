package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

public class ToNumberFunction implements Function<Object, Number> {

	@Override
	public Number apply(Object number) {
		
		String string = number.toString();
		return Double.parseDouble(string);

	}

}