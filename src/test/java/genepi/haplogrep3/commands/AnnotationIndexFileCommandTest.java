package genepi.haplogrep3.commands;

import org.junit.Test;

public class AnnotationIndexFileCommandTest {

	
	@Test
	public void testWithHsd() throws Exception {
		AnnotationIndexCommand command = new AnnotationIndexCommand();
		command.setFile("trees/phylotree-fu-rcrs/1.0/annotations/rCRS_annotation_2023-02-15_2.txt.gz");
		command.setSkip(1);
		command.setStart(2);
		command.call();
	}
	
}
