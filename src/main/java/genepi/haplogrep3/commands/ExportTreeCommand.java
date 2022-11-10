package genepi.haplogrep3.commands;

import java.io.IOException;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.PhylotreeExporter;
import picocli.CommandLine.Option;

public class ExportTreeCommand extends AbstractCommand {

	@Option(names = { "--tree" }, description = "Tree Id", required = true)
	private String phylotreeId;

	@Option(names = { "--output" }, description = "output dot file", required = true)
	private String output;

	public String getPhylotreeId() {
		return phylotreeId;
	}

	public void setPhylotreeId(String phylotreeId) {
		this.phylotreeId = phylotreeId;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public Integer call() throws IOException {

		PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			System.out.println("Error: Tree " + phylotreeId + " not found.");
			return 1;
		}

		PhylotreeExporter exporter = new PhylotreeExporter(phylotree);
		exporter.writeToFile(output);

		System.out.println("Done.");
		System.out.println("File written to " + output);

		return 0;
	}

}