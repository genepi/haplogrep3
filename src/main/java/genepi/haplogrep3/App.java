package genepi.haplogrep3;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import genepi.haplogrep3.commands.AlignCommand;
import genepi.haplogrep3.commands.BuildTreeCommand;
import genepi.haplogrep3.commands.ClassifyCommand;
import genepi.haplogrep3.commands.ClusterHaplogroupsCommand;
import genepi.haplogrep3.commands.DistanceCommand;
import genepi.haplogrep3.commands.ExportTreeCommand;
import genepi.haplogrep3.commands.InstallTreeCommand;
import genepi.haplogrep3.commands.ListTreesCommand;
import genepi.haplogrep3.commands.ServerCommand;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.JobQueue;
import genepi.io.FileUtil;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "haplogrep")
public class App implements Runnable {

	public static final String NAME = "Haplogrep 3";

	public static final String VERSION = "3.0.3";

	public static final String COPYRIGHT = "(c) 2022 Sebastian Schönherr, Hansi Weissensteiner, Lukas Forer";

	public static final int PORT = 7000;

	public static final String CONFIG_FILENAME = "haplogrep3.yaml";

	private static App instance;

	private Configuration configuration = new Configuration();

	private String configFilename = null;

	private PhylotreeRepository treeRepository;

	private JobQueue jobQueue;

	private static CommandLine commandLine;

	public static synchronized App getDefault() {
		if (instance == null) {
			instance = new App();
		}
		return instance;
	}

	public void loadConfiguration(String configFilename) {

		if (this.configFilename != null && this.configFilename.equals(configFilename)) {
			return;
		}

		try {

			String parent = ".";

			File configFile = new File(configFilename);
			if (!configFile.exists()) {

				File jarFile = new File(
						App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				configFile = new File(FileUtil.path(jarFile.getParent(), CONFIG_FILENAME));

				parent = jarFile.getParent();

				if (!configFile.exists()) {

					System.out.println("Configuration file '" + configFilename + "' not found.");
					System.exit(1);

				}
			}

			configuration = Configuration.loadFromFile(configFile, parent);
			treeRepository = new PhylotreeRepository();
			treeRepository.loadFromConfiguration(configuration);

			jobQueue = new JobQueue(configuration.getThreads());

			this.configFilename = configFilename;

		} catch (IOException | URISyntaxException e) {
			System.out.println("Loading configuration from file '" + configFilename + "' failed.");
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public PhylotreeRepository getTreeRepository() {
		return treeRepository;
	}

	public JobQueue getJobQueue() {
		return jobQueue;
	}

	public static void main(String[] args) throws URISyntaxException {

		System.out.println();
		System.out.println(NAME + " " + VERSION);
		if (COPYRIGHT != null && !COPYRIGHT.isEmpty()) {
			System.out.println(COPYRIGHT);
		}

		commandLine = new CommandLine(new App());
		commandLine.addSubcommand("server", new ServerCommand());
		commandLine.addSubcommand("classify", new ClassifyCommand());
		commandLine.addSubcommand("align", new AlignCommand());
		commandLine.addSubcommand("distance", new DistanceCommand());
		commandLine.addSubcommand("export-tree", new ExportTreeCommand());
		commandLine.addSubcommand("build-tree", new BuildTreeCommand());
		commandLine.addSubcommand("trees", new ListTreesCommand());
		commandLine.addSubcommand("install-tree", new InstallTreeCommand());
		commandLine.addSubcommand("cluster-haplogroups", new ClusterHaplogroupsCommand());

		commandLine.setExecutionStrategy(new CommandLine.RunLast());
		int result = commandLine.execute(args);
		System.exit(result);

	}

	public static boolean isDevelopmentSystem() {
		return new File("src/main/resources").exists();
	}

	@Override
	public void run() {
		commandLine.usage(System.out);
	}

}
