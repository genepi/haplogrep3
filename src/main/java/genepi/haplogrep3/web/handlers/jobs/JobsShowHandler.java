package genepi.haplogrep3.web.handlers.jobs;

import java.io.File;
import java.io.FileReader;
import java.util.Date;

import com.google.gson.Gson;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.Job;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.Page;
import genepi.io.FileUtil;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class JobsShowHandler extends AbstractHandler {

	public static final String PATH = "/jobs/{job}";

	public static final HandlerType TYPE = HandlerType.GET;

	private Configuration configuration = App.getDefault().getConfiguration();

	private String workspace = configuration.getWorkspace();

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();
	
	public void handle(Context context) throws Exception {

		String jobId = context.pathParam("job");

		String filename = FileUtil.path(workspace, jobId + ".json");
		File jobFile = new File(filename);

		if (jobFile.exists()) {

			Gson gson = new Gson();

			Job job = gson.fromJson(new FileReader(jobFile), Job.class);
			Date now = new Date();
			if (now.after(job.getExpiresOn())) {
				throw new Exception("Job expired.");
			}
			
			Phylotree phylotree = treeRepository.getById(job.getPhylotree());
			
			String template = "web/jobs/show." + job.getStatus().name().toLowerCase() + ".view.html";

			Page page = new Page(context, template);
			page.put("job", job);
			page.put("genes", phylotree.getGenes());
			page.render();

		} else {
			throw new Exception("Job not found.");
		}

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
