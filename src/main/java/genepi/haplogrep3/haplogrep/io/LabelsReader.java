package genepi.haplogrep3.haplogrep.io;

import java.util.List;
import java.util.Vector;

import genepi.io.table.reader.CsvTableReader;

public class LabelsReader {

	private List<Group> groups = new Vector<>();

	public LabelsReader(String filename, String columns[]) {

		for (String column : columns) {
			groups.add(new Group(column));
		}

		CsvTableReader reader = new CsvTableReader(filename, '\t');
		while (reader.next()) {
			String haplogroup = reader.getString("haplogroup");
			for (int i = 0; i < columns.length; i++) {
				String label = reader.getString(columns[i]);
				groups.get(i).addLabel(haplogroup, label);
			}
		}

		reader.close();

	}

	public List<Group> getGroups() {
		return groups;
	}

}
