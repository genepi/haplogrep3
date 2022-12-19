package genepi.haplogrep3.web.handlers.jobs;

import java.io.File;
import java.io.FileInputStream;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.io.FileUtil;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class JobsDownloadHandler extends AbstractHandler {

	public static final String PATH = "/jobs/{job}/{file}";

	public static final HandlerType TYPE = HandlerType.GET;

	private Configuration configuration = App.getDefault().getConfiguration();

	private String workspace = configuration.getWorkspace();

	public void handle(Context context) throws Exception {

		String job = context.pathParam("job");

		String fileId = context.pathParam("file");

		String filename = FileUtil.path(workspace, job, fileId);
		File file = new File(filename);

		if (file.exists()) {
			context.result(new FileInputStream(file));
		} else {
			throw new Exception("Job " + job + " not found.");
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
