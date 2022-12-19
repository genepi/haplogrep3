package genepi.haplogrep3.web.handlers.phylogenies;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class PhylogeniesIndexHandler extends AbstractHandler {

	public static final String PATH = "/phylogenies";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/phylogenies/index.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	public void handle(Context context) throws Exception {

		Page page = new Page(context, TEMPLATE);
		page.put("categories", treeRepository.getCategories());
		page.put("trees", treeRepository);
		page.render();

	}

	@Override
	public String getPath() {
		return configuration.getBaseUrl() + PATH;
	}

	@Override
	public HandlerType getType() {
		return TYPE;
	}

}
