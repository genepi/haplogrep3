package genepi.haplogrep3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.esotericsoftware.yamlbeans.YamlReader;

import core.Haplogroup;
import core.Polymorphism;
import core.Reference;
import core.SampleFile;
import genepi.haplogrep3.haplogrep.io.LabelsReader;
import genepi.haplogrep3.haplogrep.io.LabelsSettings;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationColumn;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationFileReader;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationSettings;
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

	private String version = "";

	private String name;

	private String category = "Other";

	private String tree;

	private String weights;

	private String fasta;

	private Reference reference;

	private String aacTable;

	private String gff;

	private String alignmentRules;

	private String description = "Please specify a description";

	private String lastUpdate = "unknown";

	private String url = "no_url_provided";

	private String[] source = new String[] { "Please specify the source of the data" };

	private boolean deprecated = false;

	private String[] genes = new String[0];

	private HashSet<String> hotspots = new HashSet<>();

	private LabelsSettings labels;

	private LabelsReader groups;

	private String template;

	private List<AnnotationSettings> annotations = new Vector<AnnotationSettings>();

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getAlignmentRules() {
		return alignmentRules;
	}

	public void setAlignmentRules(String alignmentRules) {
		this.alignmentRules = alignmentRules;
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

	public HashSet<String> getHotspots() {
		return hotspots;
	}

	public void setHotspots(HashSet<String> hotspots) {
		this.hotspots = hotspots;
	}

	public void setLabels(LabelsSettings labels) {
		this.labels = labels;
	}

	public LabelsSettings getLabels() {
		return labels;
	}

	public List<AnnotationSettings> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationSettings> annotations) {
		this.annotations = annotations;
	}

	public LabelsReader getGroups() {
		return groups;
	}

	public phylotree.Phylotree getPhylotreeInstance() {
		return PhylotreeManager.getInstance().getPhylotree(getTree(), getWeights(), getReference(), getHotspots());
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void classify(SampleFile sampleFile, Distance distance, int hits, boolean skipAlignmentRules)
			throws FileNotFoundException {

		RankingMethod rankingMethod = null;

		switch (distance) {
		case HAMMING:
			rankingMethod = new HammingRanking(hits);
			break;
		case JACCARD:
			rankingMethod = new JaccardRanking(hits);
			break;
		case KIMURA:
			rankingMethod = new Kimura2PRanking(hits);
			break;
		case KULCZYNSKI:
			rankingMethod = new KulczynskiRanking(hits);
			break;
		default:
			break;
		}

		phylotree.Phylotree haplogrepPhylotree = getPhylotreeInstance();

		sampleFile.updateClassificationResults(haplogrepPhylotree, rankingMethod);

		sampleFile.runQualityChecks(haplogrepPhylotree);

	}

	public Haplogroup getHaplogroup(String name) {
		phylotree.Phylotree haplogrepPhylotree = getPhylotreeInstance();
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
		phylotree.Phylotree haplogrepPhylotree = getPhylotreeInstance();
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
		// TODO: check required params
		tree = FileUtil.path(parent, tree);
		weights = FileUtil.path(parent, weights);
		aacTable = FileUtil.path(parent, aacTable);
		gff = FileUtil.path(parent, gff);
		fasta = FileUtil.path(parent, fasta);
		if (alignmentRules != null) {
			alignmentRules = FileUtil.path(parent, alignmentRules);
		}
		if (labels != null) {
			labels.setFilename(FileUtil.path(parent, labels.getFilename()));
		}
		if (template != null) {
			template = FileUtil.path(parent, template);
		}
		for (AnnotationSettings annotation : annotations) {
			annotation.setFilename(FileUtil.path(parent, annotation.getFilename()));
		}
	}

	public static Phylotree load(File file) throws IOException {

		YamlReader reader = new YamlReader(new FileReader(file));
		reader.getConfig().setPropertyElementType(Phylotree.class, "annotations", AnnotationSettings.class);
		reader.getConfig().setPropertyElementType(AnnotationSettings.class, "properties", AnnotationColumn.class);

		Phylotree phylotree = reader.read(Phylotree.class);
		phylotree.updateParent(file.getAbsoluteFile().getParent());

		Reference reference = new Reference(phylotree.getFasta());
		phylotree.setReference(reference);

		phylotree.loadLabels();

		return phylotree;

	}

	public void loadLabels() {
		if (labels != null) {
			groups = new LabelsReader(labels.getFilename(), labels.getGroups());
		}
	}

	public List<Haplogroup> getHaplogroups() {
		Vector<Haplogroup> haplogroups = new Vector<Haplogroup>();

		phylotree.Phylotree haplogrepPhylotree = getPhylotreeInstance();
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

	public String getIdWithVersion() {
		if (version == null || version.trim().isEmpty()) {
			return id;
		} else {
			return id + "@" + version;
		}
	}

	public void annotate(List<AnnotatedPolymorphism> polymorphisms) throws IOException {

		for (AnnotationSettings annotation : annotations) {

			AnnotationFileReader reader = new AnnotationFileReader(annotation.getFilename(), annotation.getProperties(),
					true, annotation.getRefAllele(), annotation.getAltAllele());

			for (AnnotatedPolymorphism polymorphism : polymorphisms) {

				Map<String, String> result = reader.query(annotation.getChr(), polymorphism.getPosition(),
						polymorphism.getRef(), polymorphism.getAlt());
				if (result != null) {
					polymorphism.setAnnotations(result);
				}else {
					polymorphism.setAnnotations(new HashMap<>());
				}

			}

			reader.close();
		}

	}

}
