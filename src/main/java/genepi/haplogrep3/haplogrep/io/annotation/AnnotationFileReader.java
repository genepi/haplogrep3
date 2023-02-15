package genepi.haplogrep3.haplogrep.io.annotation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import genepi.io.table.reader.CsvTableReader;
import htsjdk.tribble.readers.TabixReader.Iterator;

public class AnnotationFileReader {

	private IndexFileReader reader;

	private Map<String, Integer> columnsIndex = new HashMap<String, Integer>();

	private char separator = '\t';

	private int indexRef;

	private int indexAlt;

	private String[] buffer;

	public AnnotationFileReader(String input, List<AnnotationColumn> columns, boolean comments, String ref, String alt)
			throws IOException {

		CsvTableReader tableReader = new CsvTableReader(input, separator, !comments);

		if (!tableReader.hasColumn(ref)) {
			throw new IOException("Column '" + ref + "' not found in file '" + input + "'");
		}
		indexRef = tableReader.getColumnIndex(ref);

		if (!tableReader.hasColumn(alt)) {
			throw new IOException("Column '" + alt + "' not found in file '" + input + "'");
		}
		indexAlt = tableReader.getColumnIndex(alt);

		for (AnnotationColumn column : columns) {
			if (!tableReader.hasColumn(column.getColumn())) {
				throw new IOException("Column '" + column.getColumn() + "' not found in file '" + input + "'");
			}
			int index = tableReader.getColumnIndex(column.getColumn());
			columnsIndex.put(column.getColumn(), index);
		}

		buffer = new String[tableReader.getColumns().length];

		tableReader.close();

		reader = new IndexFileReader(input);

	}

	public Map<String, String> query(String chr, int position, String ref, String alt) throws IOException {

		Iterator result = reader.query(position);
		String line = result.next();

		while (line != null) {

			buffer = line.split("\t");

			String annoRef = buffer[indexRef];
			String annoAlt = buffer[indexAlt];

			boolean match = (annoRef.equalsIgnoreCase(ref) && annoAlt.equalsIgnoreCase(alt));

			if (match) {
				Map<String, String> values = new HashMap<>();
				for (String column : columnsIndex.keySet()) {
					int index = columnsIndex.get(column);
					values.put(column, buffer[index]);
				}
				return values;
			}

			line = result.next();

		}

		return null;
	}
	
	public void close() {
		reader.close();
	}

}
