package genepi.haplogrep3.web.util;

import org.apache.commons.lang.exception.ExceptionUtils;

import io.javalin.http.Context;

public class ErrorPage extends Page {

	private static final long serialVersionUID = 1L;

	public static final String TEMPLATE = "web/error.view.html";

	public ErrorPage(Context context) {
		super(context, TEMPLATE);
		put("stackTrace", "");
	}

	public void setTitle(String title) {
		put("title", title);
	}

	public void setMessage(String message) {
		put("message", message);
	}

	public void setException(Throwable exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		put("stackTrace", stackTrace);
	}

	public void render() {
		this.context.render(template, this);
	}

}
