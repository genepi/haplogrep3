package genepi.haplogrep3.tasks;

import java.util.List;

import genepi.haplogrep3.model.AnnotatedSample;
import genepi.io.table.writer.CsvTableWriter;
import genepi.io.table.writer.ExcelTableWriter;
import genepi.io.table.writer.ITableWriter;

public class ExportDataTask {

	private String filename;

	private List<AnnotatedSample> samples;

	public static enum ExportDataFormat {
		EXCEL, CSV
	}

	private ExportDataFormat format = ExportDataFormat.CSV;

	public ExportDataTask(List<AnnotatedSample> samples, String filename, ExportDataFormat format) {
		this.samples = samples;
		this.filename = filename;
		this.format = format;
	}

	public void run() {

		ITableWriter writer = null;

		switch (format) {
		case CSV:
			writer = new CsvTableWriter(filename);
			break;
		case EXCEL:
			writer = new ExcelTableWriter(filename);
			break;
		default:
			writer = new CsvTableWriter(filename);
			break;
		}

		writer.setColumns(new String[] { "sample", "clade", "quality" });
		for (AnnotatedSample sample : samples) {
			writer.setString("sample", sample.getSample());
			writer.setString("clade", sample.getClade());
			writer.setDouble("quality", sample.getQuality());
			writer.next();
		}
		writer.close();

		System.out.println("Written clades to file " + filename);

	}

}
