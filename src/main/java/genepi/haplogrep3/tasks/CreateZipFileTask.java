package genepi.haplogrep3.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateZipFileTask {

	private String filename;

	private String[] files;

	public CreateZipFileTask(String[] files, String filename) {
		this.filename = filename;
		this.files = files;
	}

	public void run() throws IOException {
		createZipArchive(files, filename);
	}

	protected void createZipArchive(String[] files, String filename) throws IOException {

		final FileOutputStream fos = new FileOutputStream(filename);
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		for (String srcFile : files) {
			File fileToZip = new File(srcFile);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}

		zipOut.close();
		fos.close();
	}

}
