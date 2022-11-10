package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

import com.google.gson.Gson;

public class ToJsonFunction implements Function<Object, String> {
	
	private Gson gson = new Gson();

	@Override
	public String apply(Object object) {
		return gson.toJson(object);
	}
	
}