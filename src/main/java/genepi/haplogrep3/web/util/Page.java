package genepi.haplogrep3.web.util;

import java.util.HashMap;

import genepi.haplogrep3.App;
import io.javalin.http.Context;

public class Page extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	protected Context context;

	protected String template;

	public Page(Context context, String template) {
		this();
		this.context = context;
		this.template = template;
	}

	public Page() {
		put("application", App.NAME);
		put("version", App.VERSION);
		put("baseUrl", App.getDefault().getConfiguration().getBaseUrl());
		put("url", App.getDefault().getConfiguration().getUrl());
		put("debug", App.isDevelopmentSystem());
		put("selfContained", false);
		put("minimal", false);
	}

	public void render() {

		if (context == null) {
			throw new RuntimeException("Rendering of page not possible: no context set.");
		}

		if (template == null) {
			throw new RuntimeException("Rendering of page not possible: no template set.");
		}

		context.render(template, this);
	}

}
