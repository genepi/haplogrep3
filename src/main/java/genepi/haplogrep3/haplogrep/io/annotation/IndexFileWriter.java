package genepi.haplogrep3.haplogrep.io.annotation;

import java.io.File;
import java.io.IOException;

import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.samtools.util.BlockCompressedOutputStream;
import htsjdk.tribble.util.LittleEndianOutputStream;

public class IndexFileWriter {

	public static final int INDEX_FORMAT = 270685;

	private File file;

	public IndexFileWriter(File file) {
		this.file = file;
	}

	public void buildIndex(File indexFile, int column, int skip) throws IOException {

		LittleEndianOutputStream output = new LittleEndianOutputStream(new BlockCompressedOutputStream(indexFile));
		output.writeInt(INDEX_FORMAT);

		BlockCompressedInputStream inputstream = new BlockCompressedInputStream(file);
		long offset = inputstream.getFilePointer();
		String line = inputstream.readLine();

		// skip n lines
		for (int i = 0; i < skip; i++) {
			offset = inputstream.getFilePointer();
			line = inputstream.readLine();

		}
		// write position and offset to index file
		while (line != null) {
			String[] tiles = line.split("\t");
			int position = Integer.parseInt(tiles[column - 1]);
			output.writeLong(position);
			output.writeLong(offset);
			offset = inputstream.getFilePointer();
			line = inputstream.readLine();

		}
		inputstream.close();
		output.close();

	}

}
