package genepi.haplogrep3.web.handlers.phylogenies;

import java.util.HashMap;
import java.util.Map;

import core.Haplogroup;
import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.web.handlers.clades.CladesShowHandler;
import genepi.haplogrep3.web.handlers.mutations.MutationsShowHandler;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import genepi.haplogrep3.web.util.RouteUtil;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class PhylogeniesSearchHandler extends AbstractHandler {

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}/search";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/phylogenies/search.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	public void handle(Context context) throws Exception {

		String phylotreeId = context.pathParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		String query = context.queryParam("query");
		
		Haplogroup haplogroup = phylotree.getHaplogroup(query);
		if (haplogroup != null) {
			Map<String, Object> params = new HashMap<>();
			params.put("phylotree", phylotree.getIdWithVersion());
			params.put("clade", haplogroup.toString());
			String path = RouteUtil.path(CladesShowHandler.PATH, params);
			context.redirect(path);
			return;
		}
		
		String[] tiles = query.split("-");
		if (tiles.length == 3) {
			//TODO: check if pos is integer, ref und alt...
			Map<String, Object> params = new HashMap<>();
			params.put("phylotree", phylotree.getIdWithVersion());
			params.put("pos", tiles[0]);
			params.put("ref", tiles[1]);
			params.put("alt", tiles[2]);
			String path = RouteUtil.path(MutationsShowHandler.PATH, params);
			context.redirect(path);
			return;		
		}
		
		
		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("clades", phylotree.getHaplogroups());
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
