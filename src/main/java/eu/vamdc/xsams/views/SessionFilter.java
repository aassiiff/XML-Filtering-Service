package eu.vamdc.xsams.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.annotation.WebFilter;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import uk.ac.cam.ioa.vamdc.consumer.dummy.data.MyData;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.GlobalevHttpSessionController;

/**
 * Servlet Filter implementation class SessionFilter
 */
@WebFilter(filterName= "/SessionFilter", urlPatterns={"/service"})
public class SessionFilter implements Filter {

	/**/
	@Inject
	private GlobalevHttpSessionController globalevHttpSessionController;

	/**
	 * Default constructor.
	 */
	public SessionFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("Filter Called");

		HttpSession sessionSaved = getSession((HttpServletRequest) request);
		HttpSession sessionNew;
		
		/*
		 * If request has jsessionID; then retrieve it from saved sessions.
		 * If session matching jsessionID is found then process it
		 * Else If session matching jsessionID is not found then ignore jsessionID
		 * and create new Session
		 */
		if (sessionSaved != null) {
			sessionNew = ((HttpServletRequest) request)
					.getSession(false);
			/*
			 * there is no session attached to the request even based on cookies
			 * so create a new Session
			 */
			if (sessionNew == null) {
				sessionNew = ((HttpServletRequest) request).getSession(true);
			}
			
			/*
			 * Test if saved session and new session have same ID 
			 * If they have same IDś then nothing to do
			 * Else copy session attributes from the saved session to new Session.
			 */
			if (sessionNew.getId().equalsIgnoreCase(sessionSaved.getId())) {
				// Nothing to be Done
				System.out.println("Both Sessions are same ");
			} else {
				//This doesn´t work
				//sessionNew = sessionSaved;
				@SuppressWarnings("unchecked")
				List<MyData> dataHolder = (List<MyData>) sessionSaved
						.getAttribute("dataHolder");
				if (dataHolder != null) {
					sessionNew.setAttribute("dataHolder", dataHolder);
					System.out
					.println("Session Found and Both Sessions are not same and TestController is NOT null in old session: " + dataHolder.size());
				} else {
					System.out
							.println("Session Found and Both Sessions are not same and TestController is null in old session");
				}
			}
		} else {
			sessionNew = ((HttpServletRequest) request).getSession(true);
		}
		System.out.println("URL: " + getUrl((HttpServletRequest) request));
		String pathInfo = ((HttpServletRequest) request).getPathInfo();
		// getServletPath
		System.out.println("pathInfo: " + pathInfo);
		String servletInfo = ((HttpServletRequest) request).getServletPath();
		System.out.println("pathInfo: " + servletInfo);

		Enumeration<String> attrs = request.getAttributeNames();
		while (attrs.hasMoreElements()) {
			System.out.println(attrs.nextElement());
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	private String getUrl(HttpServletRequest req) {
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

	private HttpSession getSession(HttpServletRequest request) {
		HttpSession session = null;
		String sessionID = checkAndGetSession(request);
		if (sessionID != null) {
			session = globalevHttpSessionController.findSession(sessionID);
			if (session != null)
				System.out.println("Session ID of saved Session: "
						+ session.getId());
		}
		return session;
	}

	private String checkAndGetSession(HttpServletRequest request) {

		String sessionID = null;

		String reqUrl = request.getRequestURL().toString();
		reqUrl = reqUrl.toLowerCase();

		int index = reqUrl.indexOf(";jsessionid");
		System.out.println(index);

		if (index > 1) {
			sessionID = reqUrl.substring(index + 12, reqUrl.length());
			System.out.println(sessionID);
		}

		return sessionID;
	}

}