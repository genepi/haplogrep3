package genepi.haplogrep3.commands;

import genepi.haplogrep3.App;
import genepi.haplogrep3.web.WebApp;
import picocli.CommandLine.Command;

@Command
public class ServerCommand extends AbstractCommand {

	@Override
	public Integer call() {

		App app = App.getDefault();
		int port = app.getConfiguration().getPort();

		WebApp server = new WebApp(port);
		server.start();

		return 0;

	}

}
