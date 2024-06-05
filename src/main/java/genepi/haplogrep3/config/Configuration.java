package genepi.haplogrep3.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Dataset;

public class Configuration {

	private int port = App.PORT;

	private int maxUploadSizeMb = 200;

	private List<String> phylotrees;

	private String workspace = "jobs";

	private int jobIdLength = 50;

	private int threads = 2;

	private List<Dataset> examples = new Vector<Dataset>();

	private List<String> repositories = new Vector<String>();

	private String baseUrl = "";

	private String url = "";

	private List<NavbarLink> navbar = new Vector<NavbarLink>();

	private String parent = "";
	
	private File configFile;

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

	public List<String> getPhylotrees() {
		return phylotrees;
	}

	public void setPhylotrees(List<String> phylotrees) {
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

	public void setRepositories(List<String> repositories) {
		this.repositories = repositories;
	}

	public List<String> getRepositories() {
		return repositories;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public List<NavbarLink> getNavbar() {
		return navbar;
	}

	public void setNavbar(List<NavbarLink> navbar) {
		this.navbar = navbar;
	}

	public static Configuration loadFromFile(File file, String parent) throws IOException {

		YamlReader reader = new YamlReader(new FileReader(file));
		reader.getConfig().setPropertyElementType(Configuration.class, "phylotrees", String.class);
		reader.getConfig().setPropertyElementType(Configuration.class, "examples", Dataset.class);
		Configuration configuration = reader.read(Configuration.class);
		reader.close();

		configuration.parent = parent;
		configuration.configFile = file;

		for (Dataset dataset : configuration.getExamples()) {
			dataset.updateParent(parent);
		}

		return configuration;

	}

	public void save() throws IOException {
		YamlWriter writer = new YamlWriter(new FileWriter(configFile));
		writer.getConfig().setPropertyElementType(Configuration.class, "phylotrees", String.class);
		writer.getConfig().setPropertyElementType(Configuration.class, "examples", Dataset.class);
		writer.write(this);
		writer.close();

	}

    public File getPluginsLocation() {
		return new File(parent, "trees");
    }
   
    
}
