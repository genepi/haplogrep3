package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

import com.google.gson.Gson;

public class FromJsonFunction implements Function<String, Object> {

	private Gson gson = new Gson();

	@Override
	public Object apply(String json) {
		return gson.fromJson(json, Object.class);
	}

}