package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

import genepi.haplogrep3.web.util.AbstractWebApp;
import genepi.haplogrep3.web.util.BasisTemplateFileRenderer;
import io.marioslab.basis.template.Template;
import io.marioslab.basis.template.TemplateContext;

public class IncludeStyleFunction implements Function<String, String> {

	
	
	private BasisTemplateFileRenderer renderer;

	public IncludeStyleFunction(BasisTemplateFileRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public String apply(String url) {

		String href = url;

		if (renderer.isSelfContained() && !isExternalUrl(url)) {

			System.out.println("  Include stylesheet " + url + "...");
			 Template template = renderer.loadTemplate(AbstractWebApp.ROOT_DIR + url);
			String content = template.render( new TemplateContext());
			href = Base64Util.encodeBase64("text/css", content);
		}

		return "<link rel=\"stylesheet\" href=\"" + href + "\">";
	}

	protected boolean isExternalUrl(String url) {
		return url.startsWith("https://") || url.startsWith("http://") || url.startsWith("http://");
	}
	
	
}