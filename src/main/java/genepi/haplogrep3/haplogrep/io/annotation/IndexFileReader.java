package genepi.haplogrep3.haplogrep.io.annotation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import genepi.io.text.LineReader;
import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.tribble.readers.TabixReader;
import htsjdk.tribble.readers.TabixReader.Iterator;

public class IndexFileReader {

	class Entry {
		public long offset;
		public int lines;
	}

	private Map<Long, Entry> index = new HashMap<Long, Entry>();

	private BlockCompressedInputStream stream;

	public IndexFileReader(String input) throws NumberFormatException, IOException {
		readIndex(input + ".index");
		stream = new BlockCompressedInputStream(new File(input));
	}

	protected void readIndex(String file) throws NumberFormatException, IOException {
		BlockCompressedInputStream stream = new BlockCompressedInputStream(new File(file));
		int format = readInt(stream);
		if (format != IndexFileWriter.INDEX_FORMAT) {
			throw new IOException("File '" + file + "' is not a valid index file.");
		}

		long position = readLong(stream);
		while (position != -1) {
			long offset = readLong(stream);
			if (!index.containsKey(position)) {
				Entry entry = new Entry();
				entry.offset = offset;
				entry.lines = 1;
				index.put(position, entry);
			} else {
				index.get(position).lines++;
			}
			position = readLong(stream);
		}
		stream.close();
	}

	public Iterator query(long position) throws IOException {

		Entry entry = index.get(position);
		if (entry != null) {
			stream.seek(entry.offset);

			return new Iterator() {

				int count = 0;

				@Override
				public String next() throws IOException {
					if (count < entry.lines) {
						count++;
						return stream.readLine();
					} else {
						return null;
					}
				}
			};
		} else {

			return new Iterator() {

				@Override
				public String next() throws IOException {
					return null;
				}
			};
		}
	}

	public void close() {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public long readLong(final InputStream is) throws IOException {
		final byte[] buf = new byte[8];
		int read = is.read(buf);
		if (read == 8) {
			return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getLong();
		} else {
			return -1;
		}
	}

	public int readInt(final InputStream is) throws IOException {
		byte[] buf = new byte[4];
		is.read(buf);
		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

}
