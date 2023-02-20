package genepi.haplogrep3.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import genepi.haplogrep3.App;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.StaticFileConfig;
import io.javalin.plugin.rendering.JavalinRenderer;

public abstract class AbstractWebApp {

	private static final String[] VIEW_EXTENSIONS = { ".html", ".js" };

	public static final String ROOT_DIR = "/web/public";

	private int port;

	private Javalin server;

	public AbstractWebApp(int port) {

		this.port = port;

	}

	public void start() {

		server = Javalin.create();
		defaultRoutes();
		routes();

		if (App.isDevelopmentSystem()) {

			// load templates and static files from external files not from classpath
			// auto reloading possible, no restart needed, ....
			server._conf.addStaticFiles(new Consumer<StaticFileConfig>() {

				@Override
				public void accept(StaticFileConfig config) {
					config.hostedPath = App.getDefault().getConfiguration().getBaseUrl();
					config.directory = "src/main/resources" + ROOT_DIR;
					config.location = Location.EXTERNAL;
				}
			});
			JavalinRenderer.register(new BasisTemplateFileRenderer("src/main/resources", Location.EXTERNAL, this),
					VIEW_EXTENSIONS);

		} else {
			server._conf.addStaticFiles(new Consumer<StaticFileConfig>() {

				@Override
				public void accept(StaticFileConfig config) {
					config.hostedPath = App.getDefault().getConfiguration().getBaseUrl();
					config.directory = ROOT_DIR;
					config.location = Location.CLASSPATH;
				}
			});
			JavalinRenderer.register(new BasisTemplateFileRenderer("", Location.CLASSPATH, this), VIEW_EXTENSIONS);

		}

		server.start(port);

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	protected void defaultRoutes() {
		server.error(404, errorHandler());
		server.exception(Exception.class, errorHandler());
	}

	abstract protected AbstractErrorHandler errorHandler();

	abstract protected void routes();

	Map<String, AbstractHandler> namedRoutes = new HashMap<String, AbstractHandler>();
	Map<String, String> pathRoutes = new HashMap<String, String>();

	public void route(String route, AbstractHandler handler) {
		server.addHandler(handler.getType(), handler.getPath(), handler);
		namedRoutes.put(route, handler);
		pathRoutes.put(handler.getPath(), route);
	}

	public void staticFileTemplate(String filename) {
		server.addHandler(HandlerType.GET, filename, new Handler() {

			@Override
			public void handle(Context context) throws Exception {
				String template = ROOT_DIR + filename;
				Page page = new Page(context, template);
				page.render();
			}
		});
	}

	public AbstractHandler getHandlerByName(String name) {
		return namedRoutes.get(name);
	}

	public String getNameByPath(String path) {
		return pathRoutes.get(path);
	}

}
