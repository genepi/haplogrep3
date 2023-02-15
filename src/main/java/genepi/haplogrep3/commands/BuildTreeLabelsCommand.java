package genepi.haplogrep3.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import core.Haplogroup;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.haplogrep.io.LabelsWriter;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import picocli.CommandLine.Option;

public class BuildTreeLabelsCommand extends AbstractCommand {
	
	public static String CONFIG_FILE = "haplogrep3.yaml";

	@Option(names = { "--tree" }, description = "tree name", required = true)
	private String tree;
	

	@Option(names = { "--output" }, description = "output labels.txt", required = true)
	private String output;

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

		List<Haplogroup> haploList = 	phylotree.getHaplogroups();
		
		LabelsWriter writeLabel = new LabelsWriter(haploList, output);
		
		return 0;

	}
	
	public Phylotree loadPhylotree(String id) throws FileNotFoundException, IOException {
		PhylotreeRepository repository = new PhylotreeRepository();
		Configuration configuration = Configuration.loadFromFile(new File(CONFIG_FILE), "");
		repository.loadFromConfiguration(configuration);
		return repository.getById(id);
	}

}