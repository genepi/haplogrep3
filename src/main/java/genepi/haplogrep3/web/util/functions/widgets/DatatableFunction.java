package genepi.haplogrep3.web.util.functions.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.BiFunction;

import com.google.gson.Gson;

public class DatatableFunction implements BiFunction<String, String, String> {
	
	
	private Gson gson = new Gson();

	@Override
	public String apply(String id, String jsonList) {
		
		List<List<Object>> list = gson.fromJson(jsonList, List.class);
		
		List<Map<String, Object>> values = new Vector<>();
		for (List<Object> pair: list) {
			Map<String, Object> item = new HashMap<>();
			item.put("k", pair.get(0));
			item.put("v", pair.get(1));
			values.add(item);
		}
		
		String config = "{paging: false, info: false, searching: false, columns: [{data: 'k'},{data: 'v'}], data: " +  gson.toJson(values) + "}";
		
		return "$('#" + id + "').DataTable(" + config + ");";
	}
	
}