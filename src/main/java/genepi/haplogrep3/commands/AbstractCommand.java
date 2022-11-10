package genepi.haplogrep3.commands;

import java.util.concurrent.Callable;

import genepi.haplogrep3.App;

public abstract class AbstractCommand implements Callable<Integer> {

	public AbstractCommand() {
		App app = App.getDefault();
		app.loadConfiguration(App.CONFIG_FILENAME);
	}

}
