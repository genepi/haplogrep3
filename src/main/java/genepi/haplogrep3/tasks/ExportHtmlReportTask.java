package genepi.haplogrep3.tasks;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.web.util.BasisTemplateFileRenderer;
import genepi.haplogrep3.web.util.Page;
import genepi.io.FileUtil;

public class ExportHtmlReportTask {

	private Configuration configuration = App.getDefault().getConfiguration();

	private String workspace = configuration.getWorkspace();

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private String filename;

	private Job job;

	public ExportHtmlReportTask(Job job, String filename) {
		this.job = job;
		this.filename = filename;
	}

	public void run() throws Exception {

		Phylotree phylotree = treeRepository.getById(job.getPhylotree());

		String template = "web/jobs/show." + job.getStatus().name().toLowerCase() + ".view.html";

		Page page = new Page();
		page.put("job", job);
		page.put("genes", phylotree.getGenes());

		String clades = FileUtil.path(workspace, job.getId(), "clades.json");
		page.put("clades", FileUtil.readFileAsString(clades));
		page.put("selfContained", true);

		BasisTemplateFileRenderer renderer = new BasisTemplateFileRenderer();
		String content = renderer.render(template, page);

		FileUtil.writeStringBufferToFile(filename, new StringBuffer(content));

		System.out.println("Written html report to file " + filename);

	}

}
