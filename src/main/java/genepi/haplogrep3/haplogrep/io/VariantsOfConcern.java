package genepi.haplogrep3.haplogrep.io;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import genepi.annotate.util.MapLocusGFF3;
import genepi.annotate.util.MapLocusItem;
import genepi.annotate.util.SequenceUtil;
import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Phylotree;
import genepi.io.text.LineReader;
import htsjdk.samtools.util.IntervalTree.Node;

public class VariantsOfConcern {

	private Set<String> variants = new HashSet<String>();

	private Map<String, String> codonTable;

	private MapLocusGFF3 maplocus;

	private String refSequence;
	
	public VariantsOfConcern(String filename) throws IOException {
		LineReader reader = new LineReader(filename);
		while (reader.next()) {
			String variant = reader.get();
			variants.add(variant);
		}
		reader.close();
		
		Phylotree phylotree = App.getDefault().getTreeRepository().getById("sarscov2-v6");
		
		codonTable = SequenceUtil.loadCodonTableLong(phylotree.getAacTable());
		maplocus = new MapLocusGFF3(phylotree.getGff());
		refSequence = phylotree.getReference().getSequence();		

	}

	public boolean includes(int position, String mutation) throws Exception {

		Iterator<Node<MapLocusItem>> result = maplocus.findByPosition(position);
		if (result.hasNext()) {
			MapLocusItem item = result.next().getValue();
			String aac = SequenceUtil.getAAC(refSequence, codonTable, item, position,
					mutation);
			aac = aac.replaceAll("\\?", "-");
			String variant = item.getShorthand() + ":" + aac;
			System.out.println(variant);
			return variants.contains(variant);
		}
		
		return false;

	}

}
