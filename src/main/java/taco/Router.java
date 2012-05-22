package taco;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import taco.def.DefaultObjectFactory;


/**
 * Base class for the taco routing configuration.
 * 
 * @author henper
 * 
 */
public abstract class Router {

	private List<RoutingFlow> flows = new ArrayList<RoutingFlow>();
	private ObjectFactory objectFactory = new DefaultObjectFactory();

	/**
	 * Inject an object factory
	 * 
	 * @param objectFactory
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	/**
	 * Finds a flow for a specific request URI.
	 * 
	 * @param requestURI
	 * @param requestParams
	 * @return
	 */
	public final PreparedFlow execute(HttpServletRequest request) {
		for (RoutingFlow flow : flows) {
			RoutingContinuation cont = flow.execute(request);

			if (cont != null) {
				PreparedFlow pflow = new PreparedFlow();
				pflow.setFlow(flow);
				pflow.setContinuation(cont);
				return pflow;
			}
		}
		return null;
	}

	public boolean hasMatchingRoute(String uri) {
		for (RoutingFlow flow : flows) {
			if (flow.matches(uri)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This is where all url mappings are configured.
	 */
	public abstract void init();

	/**
	 * Start configuring routing for a specific url pattern.
	 * 
	 * @param route
	 * @return a routing flow, optimized for method chaining
	 */
	protected RoutingFlow route(String route) {
		RoutingFlow flow = new RoutingFlow(objectFactory);
		flow.route(route);
		flows.add(flow);
		return flow;
	}


}
