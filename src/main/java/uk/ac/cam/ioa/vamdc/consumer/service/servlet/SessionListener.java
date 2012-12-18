package uk.ac.cam.ioa.vamdc.consumer.service.servlet;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.GlobalevHttpSessionController;

/**
 * Application Lifecycle Listener implementation class SessionListen
 * 
 */
@WebListener
public class SessionListener implements HttpSessionActivationListener,
		HttpSessionListener {

	@Inject
	private GlobalevHttpSessionController globalevHttpSessionController;

	private int sessionCount;

	/**
	 * Default constructor.
	 */
	public SessionListener() {
		this.sessionCount = 0;
	}

	/**
	 * @see HttpSessionActivationListener#sessionDidActivate(HttpSessionEvent)
	 */
	public void sessionDidActivate(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionActivationListener#sessionWillPassivate(HttpSessionEvent)
	 */
	public void sessionWillPassivate(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		globalevHttpSessionController.addSession(session);
		session.setMaxInactiveInterval(60 * 90);
		synchronized (this) {
			sessionCount++;
		}
		String id = session.getId();
		Date now = new Date();
		String message = new StringBuffer("New Session created on ")
				.append(now.toString()).append("\nID: ").append(id)
				.append("\n").append("There are now ")
				.append("" + sessionCount)
				.append(" live sessions in the application.").toString();

		System.out.println(message);
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		globalevHttpSessionController.removeSession(session);
		String id = session.getId();
		synchronized (this) {
			--sessionCount;
		}
		String message = new StringBuffer("Session destroyed"
				+ "\nValue of destroyed session ID is").append("" + id)
				.append("\n").append("There are now ")
				.append("" + sessionCount)
				.append(" live sessions in the application.").toString();
		System.out.println(message);
	}
}
