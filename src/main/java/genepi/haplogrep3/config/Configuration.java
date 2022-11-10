package genepi.haplogrep3.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Vector;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Dataset;
import genepi.haplogrep3.model.Phylotree;

public class Configuration {

	private int port = App.PORT;

	private int maxUploadSizeMb = 200;

	private List<Phylotree> phylotrees;

	private String workspace = "jobs";

	private int jobIdLength = 50;

	private int threads = 2;

	private List<Dataset> examples = new Vector<Dataset>();

	public Configuration() {

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxUploadSizeMb() {
		return maxUploadSizeMb;
	}

	public void setMaxUploadSizeMb(int maxUploadSizeMb) {
		this.maxUploadSizeMb = maxUploadSizeMb;
	}

	public List<Phylotree> getPhylotrees() {
		return phylotrees;
	}

	public void setPhylotrees(List<Phylotree> phylotrees) {
		this.phylotrees = phylotrees;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getWorkspace() {
		return workspace;
	}

	public int getJobIdLength() {
		return jobIdLength;
	}

	public void setJobIdLength(int jobIdLength) {
		this.jobIdLength = jobIdLength;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public List<Dataset> getExamples() {
		return examples;
	}

	public Dataset getExampleById(String id) {
		for (Dataset dataset : examples) {
			if (dataset.getId().equals(id)) {
				return dataset;
			}
		}
		return null;
	}

	public void setExamples(List<Dataset> examples) {
		this.examples = examples;
	}

	public static Configuration loadFromFile(File file, String parent) throws YamlException, FileNotFoundException {

		YamlReader reader = new YamlReader(new FileReader(file));
		reader.getConfig().setPropertyElementType(Configuration.class, "phylotrees", Phylotree.class);
		reader.getConfig().setPropertyElementType(Configuration.class, "examples", Dataset.class);
		Configuration configuration = reader.read(Configuration.class);

		// Update relative path
		for (Phylotree phylotree : configuration.getPhylotrees()) {
			phylotree.updateParent(parent);
		}
		for (Dataset dataset : configuration.getExamples()) {
			dataset.updateParent(parent);
		}

		return configuration;

	}

}
