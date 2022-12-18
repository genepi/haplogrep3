package genepi.haplogrep3.web.handlers;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class IndexPageHandler extends AbstractHandler {

	public static final String PATH = "/";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/index.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	public void handle(Context context) throws Exception {

		Page page = new Page(context, TEMPLATE);
		page.put("trees", treeRepository.getAll());
		page.put("examples", configuration.getExamples());
		page.put("distances", Distance.values());
		page.render();

	}

	@Override
	public String getPath() {
		return PATH;
	}

	@Override
	public HandlerType getType() {
		return TYPE;
	}

}
