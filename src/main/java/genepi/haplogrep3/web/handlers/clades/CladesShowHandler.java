package genepi.haplogrep3.web.handlers.clades;

import java.util.List;
import java.util.Vector;

import core.Haplogroup;
import core.Polymorphism;
import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationColumn;
import genepi.haplogrep3.model.AnnotatedPolymorphism;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.AnnotationTask;
import genepi.haplogrep3.tasks.PhylotreeGraphBuilder;
import genepi.haplogrep3.web.handlers.phylogenies.PhylogeniesIndexHandler;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import genepi.haplogrep3.web.util.graph.Graph;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class CladesShowHandler extends AbstractHandler {

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}/haplogroups/{clade}";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/clades/show.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	public void handle(Context context) throws Exception {

		String phylotreeId = context.pathParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		String clade = context.pathParam("clade");
		Haplogroup haplogroup = phylotree.getHaplogroup(clade);
		if (haplogroup == null) {
			throw new Exception("Clade " + clade + " not found.");
		}

		String cluster = "";
		if (phylotree.hasClusters()) {
			cluster = phylotree.getNearestCluster(phylotree.getClusters(), clade);
		}

		List<Polymorphism> polymorphisms = phylotree.getPolymorphisms(haplogroup);
		AnnotationTask annotationTask = new AnnotationTask(null, phylotree);
		annotationTask.loadFiles();
		List<AnnotatedPolymorphism> annotatedPolymorphisms = annotationTask.getAminoAcidsFromPolys(polymorphisms, null);

		Graph graph = PhylotreeGraphBuilder.buildGraph(phylotree, haplogroup.toString());

		phylotree.annotate(annotatedPolymorphisms);

		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("cluster", cluster);
		page.put("clade", haplogroup);
		page.put("annotations", phylotree.getAnnotations());
		page.put("polymorphisms", annotatedPolymorphisms);
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
