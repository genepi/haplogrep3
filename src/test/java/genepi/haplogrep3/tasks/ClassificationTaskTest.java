package genepi.haplogrep3.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;

public class ClassificationTaskTest {

	public static String CONFIG_FILE = "haplogrep3.yaml";

	public static String PHYLOTREE = "phylotree-rcrs@17.0";

	public Phylotree loadPhylotree(String id) throws FileNotFoundException, IOException {
		PhylotreeRepository repository = new PhylotreeRepository();
		Configuration configuration = Configuration.loadFromFile(new File(CONFIG_FILE), "");
		repository.loadFromConfiguration(configuration);
		return repository.getById(id);
	}

	@Test
	public void testWithHsd() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/hsd/H100.hsd"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("Sample1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

	}

	@Test
	public void testWithPhylotreeRSRS() throws Exception {

		String tree = "phylotree-rsrs@17.0";

		Phylotree phylotree = loadPhylotree(tree);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/fasta/H100.fasta"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("HM625681.1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

		// this H100 sample expects 51 variants according RSRS
		assertEquals(51, firstSample.getAnnotatedPolymorphisms().size());

	}

	@Test
	public void testWithPhylotree16() throws Exception {

		String tree = "phylotree-rcrs@16.0";

		Phylotree phylotree = loadPhylotree(tree);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/fasta/H100.fasta"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("HM625681.1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

		// this H100 sample expects 15 variants according rCRS
		assertEquals(15, firstSample.getAnnotatedPolymorphisms().size());
	}
	
	@Test
	public void testWithPhylotree15FromOnlineRepository() throws Exception {

		String tree = "phylotree-rcrs@15.0";

		Phylotree phylotree = loadPhylotree(tree);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/hsd/H100.hsd"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("Sample1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

		assertEquals(14, firstSample.getAnnotatedPolymorphisms().size());
	}	
	
	
	@Test
	public void testWithPhylotree17_fu() throws Exception {

		String tree = "phylotree-fu-rcrs@1.0";

		Phylotree phylotree = loadPhylotree(tree);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/fasta/L3i2_1.fasta"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("L3i2*2", firstSample.getSample());
		
		//haplogroup only present in phylotree 17 FU (not in previous versions)
		assertEquals("L3i2*2", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

	}
	

	@Test
	public void testWithFasta() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/fasta/H100.fasta"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("HM625681.1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

	}

	@Test
	public void testWithVcf() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/vcf/H100.vcf"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("Sample1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(14, task.getCounters().get("Variants"));
		
		assertEquals(0, firstSample.getNs());
		assertEquals(1, firstSample.getRanges().length);
	}
	
	@Test
	public void testVcfStatistics() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/vcf/H100_complex.vcf"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getCounters().get("Samples"));
		assertEquals(0.71, Double.valueOf((Double)task.getCounters().get("Reference Overlap (%)")), 0.01);
		assertEquals(14, task.getCounters().get("Variants"));
		assertEquals(1, task.getCounters().get("Out Of Range Variants"));
		assertEquals(1, task.getCounters().get("Multiallelic Variants"));
		assertEquals(0, task.getCounters().get("Flagged Variants"));
		assertEquals(1, task.getCounters().get("Duplicate Variants"));
		assertEquals(0, task.getCounters().get("Low Sample Call Rate"));
		assertEquals(2, task.getCounters().get("Variant Call Rate < 90%"));
		
	}

	@Test
	public void testWithVcfAndChipParameter() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/vcf/H100.vcf"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.setChip(true);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("Sample1", firstSample.getSample());
		assertEquals("L2a1p", firstSample.getClade());
		assertEquals(0, firstSample.getNs());
		assertEquals(firstSample.getAnnotatedPolymorphisms().size(), firstSample.getRanges().length);
	}

	@Test
	public void testWithVcfGz() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/vcf/H100.vcf.gz"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertTrue(task.isSuccess());
		assertEquals(1, task.getSamples().size());

		AnnotatedSample firstSample = task.getSamples().get(0);
		assertEquals("Sample1", firstSample.getSample());
		assertEquals("H100", firstSample.getClade());
		assertEquals(0, firstSample.getNs());

	}

	public void testWithUnsupportedFileFormat() throws Exception {

		Phylotree phylotree = loadPhylotree(PHYLOTREE);

		List<File> files = new ArrayList<File>();
		files.add(new File("test-data/H100.png"));

		ClassificationTask task = new ClassificationTask(phylotree, files, Distance.KULCZYNSKI);
		task.run();

		assertFalse(task.isSuccess());

	}

}
