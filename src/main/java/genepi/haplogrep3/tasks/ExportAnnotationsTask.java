package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import genepi.haplogrep3.haplogrep.io.annotation.AnnotationColumn;
import genepi.haplogrep3.haplogrep.io.annotation.AnnotationSettings;
import genepi.haplogrep3.model.AnnotatedPolymorphism;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Phylotree;
import genepi.io.table.writer.CsvTableWriter;

public class ExportAnnotationsTask {

	private static final String SAMPLE_ID = "SampleID";

	private static final String SNP_POSITION = "Position";

	private static final String SNP_REF = "Ref";

	private static final String SNP_ALT = "Alt";

	private String filename;

	private List<AnnotatedSample> samples;

	private Phylotree phylotree;

	public ExportAnnotationsTask(List<AnnotatedSample> samples, String filename, Phylotree phylotree) {
		this.samples = samples;
		this.filename = FilenameUtils.removeExtension(filename);
		this.filename = this.filename + ".annotations.txt";
		this.phylotree = phylotree;
	}

	public void run() throws IOException {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t', true);
		List<String> columns = new Vector<String>();
		columns.add(SAMPLE_ID);
		columns.add(SNP_POSITION);
		columns.add(SNP_REF);
		columns.add(SNP_ALT);
		for (AnnotationSettings settings : phylotree.getAnnotations()) {
			for (AnnotationColumn property : settings.getProperties()) {
				if (property.getExport() != null) {
					columns.add(property.getExport());
				}
			}
		}

		String[] columnsArray = new String[columns.size()];
		columns.toArray(columnsArray);
		writer.setColumns(columnsArray);
		for (AnnotatedSample sample : samples) {
			phylotree.annotate(sample.getAnnotatedPolymorphisms());

			for (AnnotatedPolymorphism mutation : sample.getAnnotatedPolymorphisms()) {
				writer.setString(SAMPLE_ID, sample.getSample());
				writer.setInteger(SNP_POSITION, mutation.getPosition());
				writer.setString(SNP_REF, mutation.getRef());
				writer.setString(SNP_ALT, mutation.getAlt());
				for (AnnotationSettings settings : phylotree.getAnnotations()) {
					for (AnnotationColumn property : settings.getProperties()) {
						if (property.getExport() != null) {
							String value = mutation.getAnnotations().get(property.getName());
							if (value == null) {
								value = "";
							}
							String cleanValue  = value.replaceAll("\"", "'");
							writer.setString(property.getExport(), cleanValue);
						}
					}
				}
				writer.next();
			}

		}
		writer.close();

		System.out.println("Written annotations to file " + filename);

	}

}
