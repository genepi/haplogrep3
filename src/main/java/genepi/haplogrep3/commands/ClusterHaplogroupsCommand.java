package genepi.haplogrep3.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import genepi.haplogrep3.App;
import genepi.haplogrep3.haplogrep.io.HaplogroupClustering;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import picocli.CommandLine.Option;

public class ClusterHaplogroupsCommand extends AbstractCommand {

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

		new HaplogroupClustering(phylotree, phylotree.getClusters(), output);

		return 0;

	} 

	public Phylotree loadPhylotree(String id) throws FileNotFoundException, IOException {
		PhylotreeRepository repository = App.getDefault().getTreeRepository();
		return repository.getById(id);
	}

}