package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

public class IncludeStyleFunction implements Function<String, String> {

	@Override
	public String apply(String href) {
		return "<link rel=\"stylesheet\" href=\"" + href + "\">";
	}
	
}