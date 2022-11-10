package genepi.haplogrep3.haplogrep.io;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class PhylotreeWriter {

	public static void writeToXml(String filename, PhylotreeDto phylotree) throws IOException {

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		xmlMapper.writeValue(new File(filename), phylotree);

	}

}
