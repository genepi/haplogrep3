package genepi.haplogrep3.tasks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import core.Polymorphism;
import genepi.annotate.util.MapLocusGFF3;
import genepi.annotate.util.MapLocusItem;
import genepi.annotate.util.SequenceUtil;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.util.PolymorphismHelper;
import htsjdk.samtools.util.IntervalTree.Node;
import phylotree.PhyloTreeNode;

public class PhylotreeExporter {

	private Phylotree phylotree;

	private Map<String, String> codonTable;

	private MapLocusGFF3 maplocus;

	private String refSequence;

	public PhylotreeExporter(Phylotree phylotree) throws IOException {
		this.phylotree = phylotree;
		codonTable = SequenceUtil.loadCodonTableLong(phylotree.getAacTable());
		maplocus = new MapLocusGFF3(phylotree.getGff());
		refSequence = phylotree.getReference().getSequence();
	}

	public void writeToFile(String output) throws IOException {

		FileWriter graphVizWriter = new FileWriter(output);
		graphVizWriter.write(getAsString());
		graphVizWriter.close();

	}

	public String getAsString() throws IOException {

		StringBuffer buffer = new StringBuffer();

		buffer.append("digraph { ");
		buffer.append("graph [layout = dot, rankdir = TB] ");
		buffer.append("node [shape = oval,style = filled,color = lightblue] ");

		phylotree.Phylotree haplogrepPhylotree = phylotree.getPhylotreeInstance();

		List<PhyloTreeNode> nodeList = haplogrepPhylotree.getPhyloTree().getSubHaplogroups();

		writeTreeRecursive(nodeList, buffer);

		buffer.append("}");

		return buffer.toString();
	}

	private void writeTreeRecursive(List<PhyloTreeNode> subHaplogroups, StringBuffer buffer) throws IOException {
		String quote = "\"";
		for (int i = 0; i < subHaplogroups.size(); i++) {
			if (!subHaplogroups.get(i).getParent().getHaplogroup().toString().contains("SARSCOV2")) {
				String snps = "";
				ArrayList<Polymorphism> variants = subHaplogroups.get(i).getExpectedPolys();
				PolymorphismHelper.sortByPosition(variants);
				for (int j = 0; j < variants.size(); j++) {
					snps += PolymorphismHelper.getLabel(variants.get(j));
					String aac = getAAC(variants.get(j));
					if (!aac.isEmpty()) {
						snps += " (" + aac + ")";
					}
					snps += "\n";
				}
				buffer.append(quote + subHaplogroups.get(i).getParent().getHaplogroup().toString() + quote + " -> "
						+ quote + subHaplogroups.get(i).getHaplogroup().toString().replace(" ", "\\n") + quote
						+ "[label=\"" + snps + quote + "] ");
			}
			writeTreeRecursive(subHaplogroups.get(i).getSubHaplogroups(), buffer);
		}

	}

	protected String getAAC(Polymorphism polymorphism) {
		String aac = "";
		Iterator<Node<MapLocusItem>> result = maplocus.findByPosition(polymorphism.getPosition());
		if (result.hasNext()) {
			MapLocusItem item = result.next().getValue();
			try {
				aac = SequenceUtil.getAAC(refSequence, codonTable, item, polymorphism.getPosition(),
						polymorphism.getMutation().name());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!aac.isEmpty()) {
				aac = item.getShorthand() + ":" + aac;
			}
		}
		return aac;
	}

}