package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

public class IncludeScriptFunction implements Function<String, String> {

	@Override
	public String apply(String src) {
		return "<script src=\"" + src + "\"></script>";
	}
	
}