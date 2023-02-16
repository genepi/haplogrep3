package genepi.haplogrep3.commands;

import org.junit.Test;

public class AnnotationIndexFileCommandTest {

	
	@Test
	public void testWithHsd() throws Exception {
		AnnotationIndexCommand command = new AnnotationIndexCommand();
		command.setFile("/Users/lukfor/Development/git/phylotree-fu-rcrs/src/annotations/gnomad.genomes.v3.1.sites.chrM.reduced_annotations.tsv.gz");
		command.setSkip(1);
		command.setStart(2);
		command.call();
	}
	
}
