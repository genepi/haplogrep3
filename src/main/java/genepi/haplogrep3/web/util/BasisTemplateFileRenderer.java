package genepi.haplogrep3.web.util;

import java.util.Map;
import java.util.function.Function;

import com.google.gson.Gson;

import genepi.haplogrep3.web.util.functions.DecimalFunction;
import genepi.haplogrep3.web.util.functions.DoubleFormatFunction;
import genepi.haplogrep3.web.util.functions.IncludeScriptFunction;
import genepi.haplogrep3.web.util.functions.IncludeStyleFunction;
import genepi.haplogrep3.web.util.functions.IsRouteActiveFunction;
import genepi.haplogrep3.web.util.functions.PercentageFunction;
import genepi.haplogrep3.web.util.functions.RouteFunction;
import genepi.haplogrep3.web.util.functions.ToJsonFunction;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.FileRenderer;
import io.marioslab.basis.template.Template;
import io.marioslab.basis.template.TemplateContext;
import io.marioslab.basis.template.TemplateLoader;

public class BasisTemplateFileRenderer implements FileRenderer {

	private String root;

	private TemplateLoader loader;

	private Location location;

	private AbstractWebApp server;

	public BasisTemplateFileRenderer(String root, Location location, AbstractWebApp server) {

		this.root = root;
		this.location = location;
		if (location == Location.EXTERNAL) {
			loader = new TemplateLoader.FileTemplateLoader();
		} else {
			loader = new TemplateLoader.ClasspathTemplateLoader();
		}

		this.server = server;
	}

	public String render(String filePath, Map<String, Object> model, Context context) throws Exception {

		// reload external files on every call (hot reloading for development)
		if (location == Location.EXTERNAL) {
			loader = new TemplateLoader.FileTemplateLoader();
		}

		TemplateContext templateContext = new TemplateContext();
		for (String name : model.keySet()) {
			templateContext.set(name, model.get(name));
		}

		// Add default functions
		templateContext.set("percentage", new PercentageFunction());
		templateContext.set("decimal", new DecimalFunction());
		templateContext.set("formatDouble", new DoubleFormatFunction());
		templateContext.set("includeScript", new IncludeScriptFunction());
		templateContext.set("includeStyle", new IncludeStyleFunction());
		templateContext.set("json", new ToJsonFunction());
		templateContext.set("routeUrl", new RouteFunction(server));

		if (context.handlerType() != HandlerType.BEFORE) {
			String path = context.endpointHandlerPath();
			String route = server.getNameByPath(path);
			templateContext.set("route", route != null ? route : "");
			templateContext.set("isRouteActive", new IsRouteActiveFunction(route != null ? route : ""));
		} else {
			templateContext.set("route", "");
			templateContext.set("isRouteActive", new Function<String, Boolean>() {				
				@Override
				public Boolean apply(String arg0) {
					return false;
				}
			});
		}

		templateContext.set("gson", new Gson());
		// application specific helper

		try {
			Template template = loadTemplate(filePath);
			return template.render(templateContext);
		} catch (Exception e) {
			return "Error in template '" + filePath + "': " + e.toString();
		}

	}

	public Template loadTemplate(String path) {
		String filename = root + "/" + path;
		return loader.load(filename);
	}

}
