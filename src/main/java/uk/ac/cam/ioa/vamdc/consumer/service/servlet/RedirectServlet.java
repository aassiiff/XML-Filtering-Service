package uk.ac.cam.ioa.vamdc.consumer.service.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.data.FilteringApplicationProducer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;

/**
 * Servlet implementation class RedirectServlet
 */
@WebServlet("/redirect")
public class RedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FilteringApplicationProducer filteringApplicationProducer;
	
	private ArrayList<UploadedXSAMS> uploadedFiles =  null;
	
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	protected Logger log;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RedirectServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//processRequest(request,response);
		log.info("RedirectServlet");
		
		uploadedFiles =  new ArrayList<UploadedXSAMS>();
		HttpSession session = request.getSession();
		ArrayList<UploadedXSAMS> tempUploadedFiles = (ArrayList<UploadedXSAMS>) session.getAttribute("uploadedFiles");
		
		if(tempUploadedFiles != null){
			log.info("tempUploadedFiles is not null: " + tempUploadedFiles.size());
			uploadedFiles = tempUploadedFiles;
			log.info("uploadedFiles is now tempUploadedFiles: " + uploadedFiles.size());
		} else {
			log.info("tempUploadedFiles is null: " + uploadedFiles.size());
		}
		
		String sessionID = session.getId();
		String oldSessionID = (String) request.getAttribute("oldSessionID");
		log.info(sessionID + " : " + sessionID);
		if(oldSessionID != null){
			log.info("old Session ID : " + oldSessionID);
			filteringApplicationProducer.setSessionId(oldSessionID);
		}
		

		if (!request.isRequestedSessionIdFromCookie()) {
			log.info("Session is from cookies");
		} else {
			log.info("Session is from URL");
		}
		
		filteringApplicationProducer.setUploadedXSAMS(uploadedFiles);
		
		String site = uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.Locations.getRootLocation(request);
		String urlOnly = site + "index.jsf";
		String urlWithSessionID = response.encodeRedirectURL(site + "index.jsf");
		response.encodeRedirectURL(site);
		response.sendRedirect( urlOnly );
		  
		log.info("Redirection committed.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	  * Handle all HTTP <tt>GET</tt> and <tt>POST</tt> requests.
	  * process only first "page" parameter will be processes
	  */
	  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String site = request.getContextPath() + "/";
		  String[] values = request.getParameterValues("page");
		  String value = values[0].trim();
		  if(value != null && value.trim().length() > 0){
			  if(value.equalsIgnoreCase("home")){
				  String urlWithSessionID = response.encodeRedirectURL(site + "index.jsf");
				  response.encodeRedirectURL(site);
				  response.sendRedirect( urlWithSessionID );
			  } else if(value.equalsIgnoreCase("queryBuilder")){
				  String urlWithSessionID = response.encodeRedirectURL(site + "queryBuilderForm1.jsf");
				  response.encodeRedirectURL(site);
				  response.sendRedirect( urlWithSessionID );
			  } else if(value.equalsIgnoreCase("submittedQueries")){
				  String urlWithSessionID = response.encodeRedirectURL(site + "submittedQueries.jsf");
				  response.encodeRedirectURL(site);
				  response.sendRedirect( urlWithSessionID );
			  } else if(value.equalsIgnoreCase("queryFilter")){
				  String urlWithSessionID = response.encodeRedirectURL(site + "queryFilter.jsf");
				  response.encodeRedirectURL(site);
				  response.sendRedirect( urlWithSessionID );
			  } else {
				  String urlWithSessionID = response.encodeRedirectURL(site + "index.jsf");
				  response.encodeRedirectURL(site);
				  response.sendRedirect( urlWithSessionID );
			  }
		  }
		  
	  }
	  
	  /* PRIVATE 
	  
	  private void redirect(HttpServletResponse aResponse) throws IOException {
	    String urlWithSessionID = aResponse.encodeRedirectURL("");
	    aResponse.sendRedirect( urlWithSessionID );
	  }

	  private void forward(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
	    RequestDispatcher dispatcher = aRequest.getRequestDispatcher("");
	    dispatcher.forward(aRequest, aResponse);
	  }
	*/
	  
	  /*
	   *  <li onclick="removeClass(this)"><a href="redirect?page=home">Home</a></li>
			        <li onclick="removeClass(this)"><a href="redirect?page=queryBuilder">Query Builder</a></li>
			        <li onclick="removeClass(this)"><a href="redirect?page=submittedQueries">Submitted Queries</a></li>        
			        <li class="active"  onclick="removeClass(this)"><a href="redirect?page=queryFilter">XQuery Form</a></li>
	   */
}
