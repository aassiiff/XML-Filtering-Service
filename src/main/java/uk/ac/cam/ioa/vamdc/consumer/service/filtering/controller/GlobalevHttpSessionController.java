package uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller;

import java.util.ArrayList;
import java.util.List;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

@Named
@ApplicationScoped
public class GlobalevHttpSessionController {
	
	/**/
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	
	private final List<HttpSession> _httpSessions = new ArrayList<HttpSession>();

	public List<HttpSession> getHttpSessions() {
		return new ArrayList<HttpSession>(_httpSessions);
	}

	public void addSession(final HttpSession httpSession) {
		_httpSessions.add(httpSession);
		log.info("Session Added");
	}

	public void removeSession(final HttpSession httpSession) {
		_httpSessions.remove(httpSession);
		log.info("Session Removed");
	}
	
	public HttpSession findSession(String sessionID){
		
		for(HttpSession session : _httpSessions){
			log.info(session.getId() + " saved && passed " + sessionID);
			if(session.getId().equalsIgnoreCase(sessionID)){
				log.info(session.getId() + " saved && passed " + sessionID);
				return session;
			} 
			continue;
		}
		return null;
	}

}
