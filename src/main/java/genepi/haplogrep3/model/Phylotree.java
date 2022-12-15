package genepi.haplogrep3.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.esotericsoftware.yamlbeans.YamlReader;

import core.Haplogroup;
import core.Polymorphism;
import core.Reference;
import core.SampleFile;
import genepi.haplogrep3.util.PolymorphismHelper;
import genepi.io.FileUtil;
import phylotree.PhyloTreeNode;
import phylotree.PhylotreeManager;
import search.ranking.HammingRanking;
import search.ranking.JaccardRanking;
import search.ranking.Kimura2PRanking;
import search.ranking.KulczynskiRanking;
import search.ranking.RankingMethod;

public class Phylotree {

	private String id;

	private String name;

	private String tree;

	private String weights;

	private String fasta;

	private Reference reference;

	private String maplocus;

	private String aacTable;

	private String gff;

	private String description = "Please specify a description";

	private String lastUpdate = "unknown";

	private String url = "no_url_provided";

	private String[] source = new String[] { "Please specify the source of the data" };

	private boolean deprecated = false;

	private String[] genes = new String[0];

	public Phylotree() {

	}

	public Phylotree(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public String getWeights() {
		return weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public String getFasta() {
		return fasta;
	}

	public void setFasta(String fasta) {
		this.fasta = fasta;
	}

	public String getMaplocus() {
		return maplocus;
	}

	public void setMaplocus(String maplocus) {
		this.maplocus = maplocus;
	}

	public String getAacTable() {
		return aacTable;
	}

	public void setAacTable(String aacTable) {
		this.aacTable = aacTable;
	}

	public String getGff() {
		return gff;
	}

	public void setGff(String gff) {
		this.gff = gff;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public String[] getSource() {
		return source;
	}

	public void setSource(String[] source) {
		this.source = source;
	}

	public String[] getGenes() {
		return genes;
	}

	public void setGenes(String[] genes) {
		this.genes = genes;
	}

	public void classify(SampleFile sampleFile, Distance distance, int hits) {

		RankingMethod rankingMethod = null;

		switch (distance) {
		case HAMMING:
			rankingMethod = new HammingRanking(hits);
			break;
		case JACCARD:
			rankingMethod = new JaccardRanking(hits);
			break;
		case KIMURA:
			rankingMethod = new Kimura2PRanking(1);
			break;
		case KULCZYNSKI:
			rankingMethod = new KulczynskiRanking(hits);
			break;
		default:
			break;
		}

		phylotree.Phylotree haplogrepPhylotree = PhylotreeManager.getInstance().getPhylotree(getTree(), getWeights(),
				getReference());
		sampleFile.updateClassificationResults(haplogrepPhylotree, rankingMethod);

	}

	public Haplogroup getHaplogroup(String name) {
		phylotree.Phylotree haplogrepPhylotree = PhylotreeManager.getInstance().getPhylotree(getTree(), getWeights(),
				getReference());
		List<PhyloTreeNode> nodeList = haplogrepPhylotree.getPhyloTree().getSubHaplogroups();
		return findHaplogroup(name, nodeList);
	}

	protected Haplogroup findHaplogroup(String name, List<PhyloTreeNode> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			Haplogroup haplogroup = nodeList.get(i).getHaplogroup();
			if (haplogroup.toString().equalsIgnoreCase(name)) {
				return haplogroup;
			}
			Haplogroup subHaplogroup = findHaplogroup(name, nodeList.get(i).getSubHaplogroups());
			if (subHaplogroup != null) {
				return subHaplogroup;
			}
		}
		return null;
	}

	public PhyloTreeNode getHaplogroupTreeNode(String name) {
		phylotree.Phylotree haplogrepPhylotree = PhylotreeManager.getInstance().getPhylotree(getTree(), getWeights(),
				getReference());
		List<PhyloTreeNode> nodeList = haplogrepPhylotree.getPhyloTree().getSubHaplogroups();
		return findHaplogroupTreeNode(name, nodeList);
	}

	protected PhyloTreeNode findHaplogroupTreeNode(String name, List<PhyloTreeNode> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			Haplogroup haplogroup = nodeList.get(i).getHaplogroup();
			if (haplogroup.toString().equalsIgnoreCase(name)) {
				return nodeList.get(i);
			}
			PhyloTreeNode subHaplogroup = findHaplogroupTreeNode(name, nodeList.get(i).getSubHaplogroups());
			if (subHaplogroup != null) {
				return subHaplogroup;
			}
		}
		return null;
	}

	protected void updateParent(String parent) {
		tree = FileUtil.path(parent, tree);
		weights = FileUtil.path(parent, weights);
		maplocus = FileUtil.path(parent, maplocus);
		aacTable = FileUtil.path(parent, aacTable);
		gff = FileUtil.path(parent, gff);
		fasta = FileUtil.path(parent, fasta);
	}

	public static Phylotree load(File file) throws IOException {

		YamlReader reader = new YamlReader(new FileReader(file));
		Phylotree phylotree = reader.read(Phylotree.class);
		phylotree.updateParent(file.getAbsoluteFile().getParent());

		Reference reference = new Reference(phylotree.getFasta());
		phylotree.setReference(reference);
		
		//TODO: set it manually as workaround on Polymorphism L480
		reference.setName("RCRS");

		return phylotree;

	}

	public List<Haplogroup> getHaplogroups() {
		Vector<Haplogroup> haplogroups = new Vector<Haplogroup>();

		phylotree.Phylotree haplogrepPhylotree = PhylotreeManager.getInstance().getPhylotree(getTree(), getWeights(),
				getReference());
		List<PhyloTreeNode> nodeList = haplogrepPhylotree.getPhyloTree().getSubHaplogroups();
		addAllHaplogroups(haplogroups, nodeList);
		return haplogroups;

	}

	protected void addAllHaplogroups(Vector<Haplogroup> haplogroups, List<PhyloTreeNode> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			Haplogroup haplogroup = nodeList.get(i).getHaplogroup();
			haplogroups.add(haplogroup);
			addAllHaplogroups(haplogroups, nodeList.get(i).getSubHaplogroups());
		}
	}

	public List<Polymorphism> getPolymorphisms(Haplogroup haplogroup) {
		Vector<Polymorphism> polymorphisms = new Vector<Polymorphism>();
		PhyloTreeNode node = getHaplogroupTreeNode(haplogroup.toString());
		addAllPolymorphisms(polymorphisms, node);
		PolymorphismHelper.sortByPosition(polymorphisms);
		return polymorphisms;

	}

	protected void addAllPolymorphisms(Vector<Polymorphism> polymorphisms, PhyloTreeNode node) {
		for (Polymorphism polymorphism : node.getExpectedPolys()) {
			polymorphisms.add(polymorphism);
		}
		if (node.getParent() != null) {
			addAllPolymorphisms(polymorphisms, node.getParent());
		}
	}

}
