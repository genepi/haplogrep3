package genepi.haplogrep3.web.util.functions;

import java.util.function.Function;

public class IsRouteActiveFunction implements Function<String, Boolean> {

	private String activeRoute; 
	
	public IsRouteActiveFunction(String activeRoute) {
		this.activeRoute = activeRoute;
	}

	@Override
	public Boolean apply(String route ) {

		return activeRoute.equalsIgnoreCase(route);
	}

}