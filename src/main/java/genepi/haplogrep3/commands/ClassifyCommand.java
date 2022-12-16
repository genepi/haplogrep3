package genepi.haplogrep3.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import genepi.haplogrep3.App;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.model.PhylotreeRepository;
import genepi.haplogrep3.tasks.ClassificationTask;
import genepi.haplogrep3.tasks.ExportReportTask;
import genepi.haplogrep3.tasks.ExportSequenceTask;
import genepi.haplogrep3.tasks.ExportReportTask.ExportDataFormat;
import genepi.haplogrep3.tasks.ExportSequenceTask.ExportSequenceFormat;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

@Command
public class ClassifyCommand extends AbstractCommand {

	@Option(names = { "--input", "--in" }, description = "Input fasta file", required = true)
	private String fasta;

	@Option(names = { "--tree" }, description = "Tree Id", required = true)
	private String phylotreeId;

	@Option(names = { "--output", "--out" }, description = "Output file location", required = true)
	private String output;

	@Option(names = { "--distance", "--metric" }, description = "Distance", required = false)
	private Distance distance = Distance.KULCZYNSKI;

	@Option(names = {
			"--chip" }, description = "VCF data from a genotype chip", required = false, showDefaultValue = Visibility.ALWAYS)
	private boolean chip = false;

	@Option(names = {
			"--hetLevel" }, description = "Add heteroplasmies with a level > X from the VCF file to the profile (default: 0.9)", required = false)
	private double hetLevel = 0.9;

	@Option(names = { "--hits" }, description = "Calculate best n hits", required = false)
	private int hits = 1;

	@Option(names = {
			"--extend-report" }, description = "Add flag for a extended final output", required = false, showDefaultValue = Visibility.ALWAYS)
	private boolean extendedReport = false;

	@Option(names = {
			"--write-fasta" }, description = "Write results in fasta format", required = false, showDefaultValue = Visibility.ALWAYS)
	private boolean writeFasta = false;

	@Option(names = {
			"--write-fasta-msa" }, description = "Write multiple sequence alignment (_MSA.fasta) ", required = false, showDefaultValue = Visibility.ALWAYS)
	private boolean writeFastaMSA = false;

	@Override
	public Integer call() {

		PhylotreeRepository treeRepository = App.getDefault().getTreeRepository();

		File fastaFile = new File(fasta);
		if (!fastaFile.exists()) {
			System.out.println("Error: File '" + fastaFile.getAbsolutePath() + "' not found.");
			return 1;
		}

		Phylotree phylotree = treeRepository.getById(phylotreeId);
		if (phylotree == null) {
			System.out.println("Error: Tree " + phylotreeId + " not found.");
			return 1;
		}

		List<File> files = new Vector<File>();
		files.add(fastaFile);

		ClassificationTask classificationTask = new ClassificationTask(phylotree, files, distance);
		classificationTask.setChip(chip);
		classificationTask.setHetLevel(hetLevel);
		classificationTask.setHits(hits);

		try {

			classificationTask.run();

		} catch (Exception e) {
			System.out.println("Error: " + e);
			return 1;
		}

		if (classificationTask.isSuccess()) {

			ExportReportTask exportTask = new ExportReportTask(classificationTask.getSamples(), output,
					extendedReport ? ExportDataFormat.EXTENDED : ExportDataFormat.SIMPLE, phylotree.getReference());
			try {
				exportTask.run();
			} catch (IOException e) {
				System.out.println("Error: " + e);
				return 1;
			}

			if (writeFasta) {
				ExportSequenceTask exportSequenceTask = new ExportSequenceTask(classificationTask.getSamples(), output,
						ExportSequenceFormat.FASTA, phylotree.getReference());
				try {
					exportSequenceTask.run();
				} catch (IOException e) {
					System.out.println("Error: " + e);
					return 1;
				}
			}

			if (writeFastaMSA) {
				ExportSequenceTask exportSequenceTask = new ExportSequenceTask(classificationTask.getSamples(), output,
						ExportSequenceFormat.FASTA_MSA, phylotree.getReference());
				try {
					exportSequenceTask.run();
				} catch (IOException e) {
					System.out.println("Error: " + e);
					return 1;
				}
			}

			return 0;

		} else {

			System.out.println("Error: " + classificationTask.getError());
			return 1;

		}

	}

}
