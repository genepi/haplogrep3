package genepi.haplogrep3.web.handlers.groups;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Cluster;
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

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}/clusters/{label}";

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

		String label = context.pathParam("label");

		Cluster cluster = phylotree.getClusterByLabel(label);
		if (cluster == null) {
			throw new Exception("Cluster " + label + " not found.");
		}

		Set<String> haplogroups = phylotree.getHaplogroupsByCluster(cluster);
		for (String node: cluster.getNodes()) {
			haplogroups.add(node);
		}
		Graph graph = PhylotreeGraphBuilder.build(phylotree, haplogroups);

		List<String> sortedHaplogroups = new Vector<String>(haplogroups);
		Collections.sort(sortedHaplogroups);

		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("cluster", cluster);
		page.put("clades", sortedHaplogroups);
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
