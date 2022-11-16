package genepi.haplogrep3.web.handlers.jobs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.RandomStringUtils;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.Dataset;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.Job;
import genepi.haplogrep3.tasks.JobQueue;
import genepi.haplogrep3.web.util.AbstractHandler;
import genepi.haplogrep3.web.util.FileStorage;
import genepi.haplogrep3.web.util.RouteUtil;
import genepi.io.FileUtil;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.UploadedFile;

public class JobsCreateHandler extends AbstractHandler {

	public static final String PATH = "/jobs";

	public static final HandlerType TYPE = HandlerType.POST;

	public static final String TEMPLATE = "web/results.json.view.html";

	private PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

	private Configuration configuration = App.getDefault().getConfiguration();

	private JobQueue jobQueue = App.getDefault().getJobQueue();

	private String workspace = configuration.getWorkspace();

	public void handle(Context context) throws Exception {

		if (!context.isMultipartFormData()) {
			throw new Exception("Uploaded data is not multipart form data.");
		}

		String phylotreeId = context.formParam("phylotree");
		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			throw new Exception("Phylotree " + phylotreeId + " not found.");
		}

		String dataset = context.formParam("dataset");

		Job job = null;
		if (dataset == null) {
			List<UploadedFile> uploadedFiles = context.uploadedFiles("files");
			job = createJobFromUploadedFiles(phylotree, uploadedFiles);
		} else {
			job = createJobFromDataset(phylotree, dataset);
		}

		jobQueue.submit(job);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("job", job.getId());
		String path = RouteUtil.path(JobsShowHandler.PATH, params);

		context.redirect(path);

	}

	public Job createJobFromUploadedFiles(Phylotree phylotree, List<UploadedFile> uploadedFiles) throws Exception {

		// check min 1 file uploaded and no empty files
		boolean emptyFiles = true;
		for (UploadedFile uploadedFile : uploadedFiles) {
			if (uploadedFile.getSize() > 0) {
				emptyFiles = false;
			}
		}

		if (emptyFiles) {
			throw new Exception("No files uploaded.");
		}

		// check max upload size
		for (UploadedFile uploadedFile : uploadedFiles) {
			if (uploadedFile.getSize() > configuration.getMaxUploadSizeMb() * 1024 * 1024) {
				throw new Exception("Maximal upload limit of " + configuration.getMaxUploadSizeMb() + " MB excited.");
			}
		}

		String jobId = RandomStringUtils.randomAlphanumeric(configuration.getJobIdLength());

		String jobDirectory = FileUtil.path(workspace, jobId);
		FileUtil.createDirectory(jobDirectory);

		// store uploaded files
		String dataDirectory = FileUtil.path(jobDirectory, "data");
		FileUtil.createDirectory(dataDirectory);
		List<File> files = FileStorage.store(uploadedFiles, dataDirectory);

		return Job.create(jobId, workspace, phylotree, files, Distance.KULCZYNSKI);

	}

	public Job createJobFromDataset(Phylotree phylotree, String id) throws Exception {

		Dataset dataset = configuration.getExampleById(id);

		if (dataset == null) {
			throw new Exception("Example '" + id + "' not found.");
		}

		File file = new File(dataset.getFile());
		if (!file.exists()) {
			throw new Exception("File '" + file.getAbsolutePath() + "' not found.");
		}

		String jobId = RandomStringUtils.randomAlphanumeric(configuration.getJobIdLength());

		String jobDirectory = FileUtil.path(workspace, jobId);
		FileUtil.createDirectory(jobDirectory);

		String dataDirectory = FileUtil.path(jobDirectory, "data");
		FileUtil.createDirectory(dataDirectory);

		List<File> files = new Vector<File>();
		files.add(file);

		return Job.create(jobId, workspace, phylotree, files, Distance.KULCZYNSKI);

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
