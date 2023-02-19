package genepi.haplogrep3.web;

import genepi.haplogrep3.web.handlers.ContactPageHandler;
import genepi.haplogrep3.web.handlers.ErrorHandler;
import genepi.haplogrep3.web.handlers.IndexPageHandler;
import genepi.haplogrep3.web.handlers.clades.CladesShowHandler;
import genepi.haplogrep3.web.handlers.groups.GroupsShowHandler;
import genepi.haplogrep3.web.handlers.jobs.JobsCreateHandler;
import genepi.haplogrep3.web.handlers.jobs.JobsDownloadHandler;
import genepi.haplogrep3.web.handlers.jobs.JobsShowHandler;
import genepi.haplogrep3.web.handlers.mutations.MutationsShowHandler;
import genepi.haplogrep3.web.handlers.phylogenies.PhylogeniesIndexHandler;
import genepi.haplogrep3.web.handlers.phylogenies.PhylogeniesShowHandler;
import genepi.haplogrep3.web.util.AbstractErrorHandler;
import genepi.haplogrep3.web.util.AbstractWebApp;

public class WebApp extends AbstractWebApp {

	public WebApp(int port) {
		super(port);
	}

	protected void routes() {
		route("index", new IndexPageHandler());
		route("contact", new ContactPageHandler());
		route("jobs_create", new JobsCreateHandler());
		route("jobs_show", new JobsShowHandler());
		route("jobs_download", new JobsDownloadHandler());
		route("phylogenies_index", new PhylogeniesIndexHandler());
		route("phylogenies_show", new PhylogeniesShowHandler());
		route("clades_show", new CladesShowHandler());
		route("groups_show", new GroupsShowHandler());
		route("mutations_show", new MutationsShowHandler(false));
		route("mutations_with_clade_show", new MutationsShowHandler(true));
		staticFileTemplate("/app.js");
	}

	@Override
	protected AbstractErrorHandler errorHandler() {
		return new ErrorHandler();
	}

}
