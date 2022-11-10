package genepi.haplogrep3.web.util.functions;

import java.util.Map;
import java.util.function.BiFunction;

import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.AbstractWebApp;
import genepi.haplogrep3.web.util.RouteUtil;

public class RouteFunction implements BiFunction<String, Map<String, Object>, String> {

	private AbstractWebApp server;

	public RouteFunction(AbstractWebApp server) {
		this.server = server;
	}

	@Override
	public String apply(String route, Map<String, Object> params) {

		AbstractHandler handler = server.getHandlerByName(route);
		if (handler == null) {
			// TODO: throw TemplateEngine Exception
			return "Route '" + route + "' not found.";
		}

		return RouteUtil.path(handler.getPath(), params);
	}

}