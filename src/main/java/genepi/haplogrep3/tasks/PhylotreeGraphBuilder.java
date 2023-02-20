package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import core.Polymorphism;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.util.PolymorphismHelper;
import genepi.haplogrep3.web.util.graph.Graph;
import phylotree.PhyloTreeNode;

public class PhylotreeGraphBuilder {

	public static Graph buildGraph(Phylotree phylotree, String haplogroup) throws IOException {
		Set<String> haplogroups = new HashSet<>();
		haplogroups.add(haplogroup);
		return build(phylotree, haplogroups);
	}

	public static Graph build(Phylotree phylotree, Set<String> haplogroups) throws IOException {

		Graph graph = new Graph();
		for (String haplogroup : haplogroups) {
			graph.addNode(cleanUpNode(haplogroup)).setColor("lightblue");
		}

		for (String haplogroup : haplogroups) {
			PhyloTreeNode node = phylotree.getHaplogroupTreeNode(haplogroup);
			writeTree(node, graph);
		}
		return graph;
	}

	private static void writeTree(PhyloTreeNode child, Graph graph) {

		if (child == null) {
			return;
		}

		PhyloTreeNode root = child.getParent();
		if (root == null) {
			return;
		}

		ArrayList<Polymorphism> variants = child.getExpectedPolys();
		PolymorphismHelper.sortByPosition(variants);
		String snps = "";
		for (int j = 0; j < variants.size(); j++) {
			snps += PolymorphismHelper.getLabel(variants.get(j));
			snps += "\\n";
		}

		graph.addEdge(cleanUpNode(root.getHaplogroup().toString()), cleanUpNode(child.getHaplogroup().toString()),
				snps);

		writeTree(root, graph);

	}

	protected static String cleanUpNode(String node) {
		return node.replace(" ", "\\n").replace("'", "\\'");
	}

}