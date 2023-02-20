package genepi.haplogrep3.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.haplogrep.io.HaplogroupClustering;
import genepi.haplogrep3.model.Cluster;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import picocli.CommandLine.Option;

public class ClusterHaplogroupsCommand extends AbstractCommand {

	public static String CONFIG_FILE = "haplogrep3.yaml";

	@Option(names = { "--tree" }, description = "tree name", required = true)
	String tree;

	@Option(names = { "--output" }, description = "output haplogrpups (txt)", required = true)
	String output;

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	@Override
	public Integer call() throws Exception {

		Phylotree phylotree = loadPhylotree(tree);

		List<Cluster> clusters = loadClusters();

		new HaplogroupClustering(phylotree, clusters, output);

		return 0;

	}

	public Phylotree loadPhylotree(String id) throws FileNotFoundException, IOException {
		PhylotreeRepository repository = new PhylotreeRepository();
		Configuration configuration = Configuration.loadFromFile(new File(CONFIG_FILE), "");
		repository.loadFromConfiguration(configuration);
		return repository.getById(id);
	}

	public List<Cluster> loadClusters() {
		List<Cluster> clusters = new ArrayList<Cluster>();
		Cluster cluster = new Cluster("B", new String[] { "B6", "B5", "B4" }, "red");
		clusters.add(cluster);

		cluster = new Cluster("L0", new String[] { "L0" }, "blue");
		clusters.add(cluster);
		// TODO: check with hansi, H2a2a1 for missing haplogroups
		cluster = new Cluster("H", new String[] { "H", "H2a2a1" }, "blue");
		clusters.add(cluster);

		return clusters;
	}

}