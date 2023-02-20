package genepi.haplogrep3.web.util.functions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class ToJsonFunction implements Function<Object, String> {

	private Gson gson = new Gson();

	public ToJsonFunction() {

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			return new JsonPrimitive(Double.parseDouble(df.format(src)));
		});
		gson = builder.create();
	}

	@Override
	public String apply(Object object) {
		return gson.toJson(object);
	}

}