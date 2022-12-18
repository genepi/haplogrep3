package genepi.haplogrep3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import genepi.haplogrep3.App;
import genepi.haplogrep3.config.Configuration;
import genepi.haplogrep3.plugins.InstalledPlugin;
import genepi.haplogrep3.plugins.PluginRelease;
import genepi.haplogrep3.plugins.PluginRepository;

public class PhylotreeRepository {

	private List<Phylotree> trees;

	private boolean forceUpdate;

	public static boolean FORCE_UPDATE = false;

	public PhylotreeRepository() {
		trees = new Vector<Phylotree>();
	}

	public synchronized void loadFromConfiguration(Configuration configuration)
			throws FileNotFoundException, IOException {

		trees = new Vector<Phylotree>();

		PluginRepository repository = new PluginRepository(configuration.getRepositories(), forceUpdate);

		for (String id : configuration.getPhylotrees()) {

			Phylotree phylotree = null;

			if (new File(id).exists()) {

				phylotree = Phylotree.load(new File(id));

			} else {

				PluginRelease pluginRelease = repository.findById(id);
				InstalledPlugin plugin = repository.resolveRelease(pluginRelease);
				phylotree = Phylotree.load(plugin.getPath());

			}

			trees.add(phylotree);

		}

	}
	
	public void install(String id, Configuration configuration) throws IOException {
		
		PluginRepository repository = new PluginRepository(configuration.getRepositories(), forceUpdate);
		
		Phylotree phylotree = null;
		
		if (new File(id).exists()) {

			phylotree = Phylotree.load(new File(id));

		} else {

			PluginRelease pluginRelease = repository.findById(id);
			InstalledPlugin plugin = repository.resolveRelease(pluginRelease);
			phylotree = Phylotree.load(plugin.getPath());

		}
		
		if (phylotree != null) {
			configuration.getPhylotrees().add(id);
			configuration.save(new File(App.CONFIG_FILENAME));
		}
	}

	public List<Phylotree> getAll() {

		return Collections.unmodifiableList(trees);

	}

	public synchronized Phylotree getById(String id) {

		// TODO: use data-structure with O(1), hashmap or so.

		for (Phylotree tree : trees) {
			if (tree.getId().equals(id)) {
				return tree;
			}
		}
		return null;

	}

}
