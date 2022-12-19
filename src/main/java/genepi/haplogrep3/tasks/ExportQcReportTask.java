package genepi.haplogrep3.tasks;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import genepi.haplogrep3.model.AnnotatedSample;
import genepi.io.table.writer.CsvTableWriter;

public class ExportQcReportTask {

	private static final String INFO_TYPE = "info";

	private static final String WARNING_TYPE = "warning";

	private static final String ERROR_TYPE = "error";

	private static final String MESSAGE = "Message";

	private static final String TYPE = "Type";

	private static final String SAMPLE_ID = "SampleID";

	private String filename;

	private List<AnnotatedSample> samples;

	public ExportQcReportTask(List<AnnotatedSample> samples, String filename) {
		this.samples = samples;
		this.filename = FilenameUtils.removeExtension(filename);
		this.filename = this.filename + ".qc.txt";
	}

	public void run() throws IOException {

		CsvTableWriter writer = new CsvTableWriter(filename, '\t', true);
		writer.setColumns(new String[] { SAMPLE_ID, TYPE, MESSAGE });
		for (AnnotatedSample sample : samples) {
			for (String error : sample.getErrors()) {
				writer.setString(SAMPLE_ID, sample.getSample());
				writer.setString(TYPE, ERROR_TYPE);
				writer.setString(MESSAGE, error);
				writer.next();
			}
			for (String warning : sample.getWarnings()) {
				writer.setString(SAMPLE_ID, sample.getSample());
				writer.setString(TYPE, WARNING_TYPE);
				writer.setString(MESSAGE, warning);
				writer.next();
			}
			for (String info : sample.getInfos()) {
				writer.setString(SAMPLE_ID, sample.getSample());
				writer.setString(TYPE, INFO_TYPE);
				writer.setString(MESSAGE, info);
				writer.next();
			}
		}
		writer.close();

		System.out.println("Written qc report to file " + filename);

	}

}
