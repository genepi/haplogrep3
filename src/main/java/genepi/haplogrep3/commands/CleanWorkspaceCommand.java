package genepi.haplogrep3.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import genepi.haplogrep3.App;
import genepi.haplogrep3.tasks.Job;
import genepi.io.FileUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

@Command
public class CleanWorkspaceCommand extends AbstractCommand {

	@Option(names = { "--dry" }, description = "Dry run", required = false, showDefaultValue = Visibility.ALWAYS)
	protected boolean dry = false;

	@Override
	public Integer call() throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		String workspace = App.getDefault().getConfiguration().getWorkspace();

		String[] jobFiles = FileUtil.getFiles(workspace, "*.json");

		int expired = 0;
		int notExpired = 0;
		int totalJobs = 0;

		Gson gson = new Gson();

		for (String jobFile : jobFiles) {
			Job job = gson.fromJson(new FileReader(jobFile), Job.class);
			Date now = new Date();
			if (now.after(job.getExpiresOn())) {
				File jobPath = new File(FileUtil.path(workspace, job.getId()));
				if (jobPath.exists()) {
					System.out.println("Job " + job.getId() + " expired.");
					if (!dry) {
						FileUtil.deleteDirectory(FileUtil.path(workspace, job.getId()));
						expired++;
					}
				}
			} else {
				notExpired++;
			}

			totalJobs++;

		}

		System.out.println("Deleted " + expired + " expired jobs. Unexpired jobs: " + notExpired);

		return 0;

	}

}
