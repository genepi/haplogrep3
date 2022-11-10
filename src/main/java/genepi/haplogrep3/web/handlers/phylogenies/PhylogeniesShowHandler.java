package genepi.haplogrep3.web.handlers.phylogenies;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.PhylotreeExporter;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class PhylogeniesShowHandler extends AbstractHandler {

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/phylogenies/show.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	public void handle(Context context) throws Exception {

		String phylotreeId = context.pathParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		PhylotreeExporter exporter = new PhylotreeExporter(phylotree);
		
		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("dotGraph", exporter.getAsString());
		page.put("clades", 	phylotree.getHaplogroups());
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
