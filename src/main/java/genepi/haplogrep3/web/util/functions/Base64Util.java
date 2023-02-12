package genepi.haplogrep3.web.util.functions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import io.javalin.core.util.FileUtil;

public class Base64Util {

	public static byte[] readBytesFromFile(String filename) throws IOException {
		FileInputStream in = new FileInputStream(filename);
		byte[] bytes = readBytes(in);
		in.close();
		return bytes;
	}

	public static byte[] readBytesFromClasspath(String path) throws IOException {
		String resolvedPath = resolvePath(path);
		InputStream in = FileUtil.class.getResourceAsStream(resolvedPath);
		byte[] bytes = readBytes(in);
		in.close();
		return bytes;
	}

	public static String readString(InputStream in) throws IOException {
		byte[] buffer = new byte[1024 * 10];
		int read = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		return new String(out.toByteArray(), "UTF-8");
	}

	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] targetArray = new byte[in.available()];
		in.read(targetArray);
		return targetArray;
	}

	public static String encodeBase64(String mimeType, String content) {
		return encodeBase64(mimeType, content.getBytes());
	}

	public static String encodeBase64(String mimeType, byte[] bytes) {
		String encodedContent = java.util.Base64.getEncoder().encodeToString(bytes);
		String data = "data:" + mimeType + ";base64," + encodedContent;
		return data;
	}

	public static String resolvePath(String path) {
		String filename = new File(path).getName();
		URI uri = URI.create(path);
		String resolvedPath = uri.resolve("").toString() + filename;
		return resolvedPath;
	}

}
