package uk.ac.cam.ioa.vamdc.consumer.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;

import javax.enterprise.event.Event;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import eu.vamdc.xsams.views.RequestException;

import uk.ac.cam.ioa.vamdc.consumer.dummy.data.MyData;
import uk.ac.cam.ioa.vamdc.consumer.dummy.data.TestController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.XMLDatabaseController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.data.FilteringApplicationProducer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.DeleteXSAMSEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.XSAMSUploadEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.UploadedXSAMSContainer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.thread.DownloadManager;

/**
 * An abstract parent for all servlets in the application. This parent just
 * deals with logging and reporting of errors. It provides the following support
 * to its subclasses.
 * <ul>
 * <li>An inheritable commons-logging {@link #log}.
 * <li>All Exception instances from the sub-class caught (including runtime
 * exceptions that inherit from Exception, but not those that inherit from
 * Error).
 * <li>All caught exceptions logged with stack traces.
 * <li>Instances of RequestException reported as 400 errors.
 * <li>All other caught exceptions reported as 500 errors.
 * </ul>
 * The above provisions apply to GET and POST requests only.
 * 
 * @author Guy Rixon
 */

@WebServlet("/service")
public class ServiceServlet extends HttpServlet {

	private static final long serialVersionUID = -1160275330749579717L;

	@Inject
	private FilteringApplicationProducer filteringApplicationProducer;
	
	//@Inject
	//private XMLDatabaseController xmlDatabaseController;
	
	@Inject Event<XSAMSUploadEvent> myFileUploadEvent;
	
	
	private ArrayList<UploadedXSAMS> uploadedFiles =  null;

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	protected Logger log;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
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
		

		//String sessionid = request.getPathInfo();
		String sessionID = session.getId();
		log.info(sessionID + " : " + sessionID);

		if (!request.isRequestedSessionIdFromCookie()) {
			log.info("Session is from cookies");
		} else {
			log.info("Session is from URL");
		}

		
		filteringApplicationProducer.setSessionId(sessionID);
		filteringApplicationProducer.getXmlDatabase();
		
		DownloadManager downloadManager;

		if ("application/x-www-form-urlencoded"
				.equals(request.getContentType())) {
			log.info("Handling application/x-www-form-urlencoded");
			URL[] urls = getUrl(request);

			log.info("Initializing DownloadManager: " + uploadedFiles.isEmpty() + " " + uploadedFiles.toString());
			downloadManager = new DownloadManager(urls, uploadedFiles, filteringApplicationProducer.getXmlDatabase());

			log.info("Invoking DownloadManager");

			Thread thread = new Thread(downloadManager);

			thread.run();
			
			log.info("After Initializing DownloadManager: " + uploadedFiles.isEmpty()  + " " + uploadedFiles.toString());
			session.setAttribute("uploadedFiles", uploadedFiles);

			filteringApplicationProducer.setUploadedXSAMS(uploadedFiles);
			/*
			System.out.println(request.getServerName());
			System.out.println(request.getPathInfo());
			System.out.println(request.getRequestURL());
			System.out.println(request.getRequestURI());
			System.out.println(request.getLocalAddr());
			*/
			log.info("Redirection committed.");
			// + request.getLocalAddr()
			String site =  "redirect?sessionRef=" + sessionID;
			
			System.out.println("Redirected site: " + site);
			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
			response.setHeader("Location", uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.Locations.getRootLocation(request) + site /*response.encodeRedirectURL(site)*/);

		} else if ("multipart/form-data".equals(request.getContentType())) {
			log.info("Handling multipart");
			try {
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iter = upload.getItemIterator(request);
				boolean cached = false;
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					if (name.equals("url") && item.isFormField()) {

						// String key = uploadFromUrl(item);
						// redirect(request, key, response);
					}
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						log.info("Form field " + name + " with value "
								+ Streams.asString(stream) + " detected.");
					} else {
						log.info("File field " + name + " with file name "
								+ item.getName() + " detected.");
					}
					stream.close();
				}
			} catch (FileUploadException e) {
				throw new RequestException(e);
			}
		} else {
			String site = "http://localhost" + ":" + request.getLocalPort() + request
					.getContextPath() + "/redirect?sessionRef=" + sessionID;
			System.out.println("Redirected site: " + site);
			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
			response.setHeader("Location", site /*response.encodeRedirectURL(site)*/);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private URL[] getUrl(HttpServletRequest request) throws RequestException {
		String[] values = request.getParameterValues("url");
		List<URL> postedURL = new ArrayList<URL>();
		URL[] urls = null;

		if (values == null) {
			throw new RequestException(
					"Please set the url parameter or upload a file");
		} else {
			
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null) {
					String value = values[i].trim();
					try {
						URL u = new URL(value);
						postedURL.add(u);
						log.info("Accepted URL " + u + " as a data source");

					} catch (MalformedURLException e) {
						throw new RequestException("'" + value
								+ "' is not a valid URL");
					} finally{
						if (i < values.length){
							continue;
						}
					}
				}
			}
			urls = new URL[postedURL.size()];
			urls = postedURL.toArray(urls);
		}
		return urls;
	}
}
