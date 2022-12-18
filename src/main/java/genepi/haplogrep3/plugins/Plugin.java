package genepi.haplogrep3.plugins;

import java.util.List;
import java.util.Vector;

public class Plugin {

	private String id;

	private String description;

	private String url;

	private String license;

	private String latest;

	private List<PluginRelease> releases = new Vector<PluginRelease>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public void setReleases(List<PluginRelease> releases) {
		this.releases = releases;
	}

	public List<PluginRelease> getReleases() {
		return releases;
	}

}
