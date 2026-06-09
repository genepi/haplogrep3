package genepi.haplogrep3.plugins;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.Vector;

import com.esotericsoftware.yamlbeans.YamlReader;

import genepi.io.FileUtil;
import net.lingala.zip4j.ZipFile;

public class PluginRepository {

	public static String LATEST_VERSION = null;

	public File pluginsLocation = new File("trees");

	private List<String> urls;

	private List<List<Plugin>> repositories = new Vector<List<Plugin>>();

	private boolean alreadyUpdated = false;

	public PluginRepository(List<String> urls, boolean forceUpdate, File pluginsLocation) throws IOException {
		this.pluginsLocation = pluginsLocation;
		this.urls = urls;
		pluginsLocation.mkdirs();
		updateRepositories(urls, forceUpdate);
	}


	public PluginRelease findById(String id) throws IOException {
		String[] tiles = id.split("@", 2);
		String name = tiles[0];
		String version = LATEST_VERSION;
		if (tiles.length > 1) {
			version = tiles[1];
		} else {
			throw new RuntimeException("Please specify a version. Latest is not yet supported.");
		}
		try {
			return findByNameAndVersion(name, version);
		} catch (RuntimeException e) {
			System.out.println("Could not find tree '" + name + "' with version '" + version + "'. Updating tree repositories and trying again.");
			updateRepositories(urls, true);
			return findByNameAndVersion(name, version);
		}
	}

	public PluginRelease findByNameAndVersion(String name, String version) {
		List<PluginRelease> releases = findReleasesByName(name);
		if (releases == null) {
			throw new RuntimeException("Tree '" + name + "' not found.");
		}
		PluginRelease release = findRelease(releases, version);
		if (release == null) {
			throw new RuntimeException("Tree '" + name + "' found, but version " + version + " not found.");
		}
		return release;
	}

	private List<PluginRelease> findReleasesByName(String name) {
		for (List<Plugin> plugins : repositories) {
			for (Plugin plugin : plugins) {
				if (plugin.getId().equals(name)) {
					return plugin.getReleases();
				}
			}
		}
		return null;
	}

	protected PluginRelease findRelease(List<PluginRelease> releases, String version) {
		for (PluginRelease release : releases) {
			if (release.getVersion().equals(version)) {
				return release;
			}
		}
		return null;
	}

	public InstalledPlugin resolveRelease(PluginRelease release) throws IOException {

		String id = release.getPlugin().getId();

		String filename = "tree.yaml";

		File pluginPath = new File(pluginsLocation, FileUtil.path(id, release.getVersion()));
		InstalledPlugin plugin = new InstalledPlugin();
		plugin.setRelease(release);
		plugin.setPath(new File(pluginPath.getAbsolutePath(), filename));
		if (pluginPath.exists()) {
			return plugin;
		}

		if (isHttpProtocol(release.getUrl())) {
			pluginPath.mkdirs();
			File zipPackage = new File(pluginPath, "package.zip");
			System.out.println("Download tree from " + release.getUrl() + "...");
			download(release.getUrl(), zipPackage);
			extract(zipPackage, pluginPath);
			System.out.println("Tree " + plugin.getRelease().getPlugin().getId() + "@" + plugin.getRelease().getVersion() + " installed.");
		} else {
			plugin.setPath(new File(release.getUrl()));
		}

		return plugin;

	}

	protected void download(String url, File target) throws IOException {

		InputStream in = new URL(url).openStream();
		Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);

	}

	protected void extract(File archive, File target) throws IOException {
		new ZipFile(archive).extractAll(target.getAbsolutePath());

	}

	protected boolean isHttpProtocol(String url) {
		return url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://");
	}

	/**
	 * Update the plugin repositories by downloading the index files from the given urls.
	 * If the file already exists and forceUpdate is false, the file will not be redownloaded.
	 * @param urls
	 * @param forceUpdate
	 * @throws IOException
	 */
	private void updateRepositories(List<String> urls, boolean forceUpdate) throws IOException {
		if (alreadyUpdated) {
			return;
		}
		repositories = new Vector<List<Plugin>>();
		for (String url : urls) {
			File indexFile = null;
			if (isHttpProtocol(url)) {
				indexFile = new File(pluginsLocation, getNameForUrl(url) + ".yaml");
				if(!indexFile.exists() || forceUpdate) {
					download(url, indexFile);
				}
			} else {
				indexFile = new File(url);
			}
			repositories.add(loadFromFile(indexFile));
		}
		if (forceUpdate) {
			alreadyUpdated = true;
		}
	}

	private List<Plugin> loadFromFile(File file) throws IOException {

		if (!file.exists()) {
			throw new IOException("File '" + file.getAbsolutePath() + "' not found.");
		}

		try {

			YamlReader reader = new YamlReader(new FileReader(file));
			reader.getConfig().setPropertyElementType(Plugin.class, "releases", PluginRelease.class);
			List<Plugin> plugins = reader.read(List.class, Plugin.class);
			for (Plugin plugin : plugins) {
				for (PluginRelease release : plugin.getReleases()) {
					release.setPlugin(plugin);
				}
			}
			return plugins;

		} catch (Exception e) {

			System.out.println("Loading repo failed");
			throw e;

		}

	}

	private String getNameForUrl(String url) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");

			md.update(url.getBytes());
			byte[] md5sum = md.digest();

			Formatter fm = new Formatter();
			for (byte b : md5sum) {
				fm.format("%02x", b);
			}
			String result = fm.out().toString();
			fm.close();
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

}
