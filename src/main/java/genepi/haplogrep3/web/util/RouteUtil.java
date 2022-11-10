package genepi.haplogrep3.web.util;

import java.util.Map;

public class RouteUtil {

	public static String path(String path, Map<String, Object> params) {

		String replacedPath = path;
		for (String key : params.keySet()) {
			replacedPath = replacedPath.replaceAll("\\{" + key + "\\}", params.get(key).toString());
		}

		return replacedPath;
	}

}
