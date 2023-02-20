package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

import genepi.haplogrep3.web.util.AbstractWebApp;
import genepi.haplogrep3.web.util.BasisTemplateFileRenderer;
import io.marioslab.basis.template.Template;
import io.marioslab.basis.template.TemplateContext;

public class IncludeScriptFunction implements Function<String, String> {

	private BasisTemplateFileRenderer renderer;

	public IncludeScriptFunction(BasisTemplateFileRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public String apply(String url) {

		String src = url;

		if (renderer.isSelfContained() && !isExternalUrl(url)) {

			System.out.println("  Include javascript " + url + "...");
			Template template = renderer.loadTemplate(AbstractWebApp.ROOT_DIR + url);
			String content = template.render(new TemplateContext());
			src = Base64Util.encodeBase64("text/javascript", content);
		}

		return "<script src=\"" + src + "\"></script>";
	}

	protected boolean isExternalUrl(String url) {
		return url.startsWith("https://") || url.startsWith("http://") || url.startsWith("http://");
	}

}