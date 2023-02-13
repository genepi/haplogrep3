package genepi.haplogrep3.web.handlers.groups;

import java.util.Set;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.haplogrep.io.Group;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.PhylotreeGraphBuilder;
import genepi.haplogrep3.web.handlers.phylogenies.PhylogeniesIndexHandler;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import genepi.haplogrep3.web.util.graph.Graph;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class GroupsShowHandler extends AbstractHandler {

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}/groups/{group}/{label}";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/groups/show.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	public void handle(Context context) throws Exception {

		String phylotreeId = context.pathParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		String groupName = context.pathParam("group");

		Group group = phylotree.getGroups().getGroupByName(groupName);

		String label = context.pathParam("label");

		Set<String> haplogroups = group.getHaplogroupsByLabel(label);

		Graph graph = PhylotreeGraphBuilder.build(phylotree, haplogroups);

		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("group", group);
		page.put("label", label);
		page.put("clades", haplogroups);
		page.put("graph", graph);
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
