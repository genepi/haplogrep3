package genepi.haplogrep3.commands;

import java.io.IOException;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.PhylotreeRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command
public class InstallTreeCommand extends AbstractCommand {

	@Parameters(paramLabel = "TREE", description = "one or more trees to install")

	private String[] trees;

	@Override
	public Integer call() throws IOException {

		for (String tree : trees) {
			PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();
			treeRepository.install(tree, App.getDefault().getConfiguration());
			System.out.println("Tree " + tree + " installed.");
		}

		return 0;
	}

}