package genepi.haplogrep3.commands;

import java.io.IOException;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import picocli.CommandLine.Command;

@Command
public class ListTreesCommand extends AbstractCommand {

	@Override
	public Integer call() throws IOException {

		PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

		System.out.println();
		System.out.println("Available trees:");
		for (Phylotree phylotree : treeRepository.getAll()) {
			System.out.println("  --tree " + phylotree.getIdWithVersion() + "\t\t\t" + phylotree.getName());
		}

		System.out.println();
		
		return 0;
	}

}