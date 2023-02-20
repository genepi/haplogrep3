package genepi.haplogrep3.web.handlers.mutations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationFileReader;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationSettings;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.web.handlers.phylogenies.PhylogeniesIndexHandler;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.BasisTemplateFileRenderer;
import genepi.haplogrep3.web.util.Page;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.staticfiles.Location;

public class MutationsShowHandler extends AbstractHandler {

	public static final String PATH = PhylogeniesIndexHandler.PATH + "/{phylotree}/mutations/{pos}_{ref}_{alt}";

	public static final String PATH_WITH_HAPLOGROUP = PhylogeniesIndexHandler.PATH
			+ "/{phylotree}/haplogroups/{clade}/mutations/{pos}_{ref}_{alt}";

	public static final HandlerType TYPE = HandlerType.GET;

	public static final String TEMPLATE = "web/mutations/show.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	private boolean withClade = false;

	public MutationsShowHandler(boolean withClade) {
		this.withClade = withClade;
	}

	public void handle(Context context) throws Exception {

		String phylotreeId = context.pathParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		String posString = context.pathParam("pos");
		String ref = context.pathParam("ref");
		String alt = context.pathParam("alt");
		String minimalQueryParam = context.queryParam("minimal");

		boolean minimal = (minimalQueryParam != null) && (minimalQueryParam.equalsIgnoreCase("true"));

		int pos = Integer.parseInt(posString);

		Map<String, Object> values = new HashMap<String, Object>();
		for (AnnotationSettings annotation : phylotree.getAnnotations()) {
			if (phylotree.getAnnotations() != null) {
				AnnotationFileReader reader = new AnnotationFileReader(annotation.getFilename(),
						annotation.getProperties(), true, annotation.getRefAllele(), annotation.getAltAllele());
				Map<String, String> result = reader.query(annotation.getChr(), pos, ref, alt);
				if (result != null) {
					values.putAll(result);
				}
				reader.close();
			}
		}

		String details = "<i>No annotations found</i>";
		if (phylotree.getTemplate() != null) {
			File template = new File(phylotree.getTemplate());

			if (!template.exists()) {
				throw new IOException("Template file '" + template.getAbsolutePath() + "' not found.");
			}

			BasisTemplateFileRenderer render = new BasisTemplateFileRenderer(template.getParentFile().getAbsolutePath(),
					Location.EXTERNAL, null);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("annotations", values);
			model.put("position", pos);
			model.put("ref", ref);
			model.put("alt", alt);
			model.put("reference", phylotree.getCategory());
			model.put("phylotree", phylotree.getName() + " (" + phylotree.getVersion() + ")");
			model.put("minimal", minimal);
			details = render.render(template.getName(), model);
		}

		Page page = new Page(context, TEMPLATE);
		page.put("tree", phylotree);
		page.put("mutation", pos + " (" + ref + ">" + alt + ")");
		page.put("details", details);
		page.put("values", values);
		page.put("position", pos);
		page.put("ref", ref);
		page.put("alt", alt);
		
		if (withClade) {
			String haplogroup = context.pathParam("clade");
			page.put("clade", haplogroup);
		} else {
			page.put("clade", "");
		}
		page.put("minimal", minimal);
		page.render();

	}

	@Override
	public String getPath() {
		if (withClade) {
			return configuration.getBaseUrl() + PATH_WITH_HAPLOGROUP;
		} else {
			return configuration.getBaseUrl() + PATH;
		}
	}

	@Override
	public HandlerType getType() {
		return TYPE;
	}

}
