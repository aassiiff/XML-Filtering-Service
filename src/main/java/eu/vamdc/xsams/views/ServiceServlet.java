package eu.vamdc.xsams.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;

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

import uk.ac.cam.ioa.vamdc.consumer.dummy.data.MyData;
import uk.ac.cam.ioa.vamdc.consumer.dummy.data.TestController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.UploadedXSAMSContainer;

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

	private static final long serialVersionUID = 1L;

	@Inject
	private TestController testController;

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

		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		List<MyData> dataHolderTemp = (List<MyData>) session.getAttribute("dataHolder");
		if(dataHolderTemp != null){
			System.out.println("dataHolderTemp.size(): " + dataHolderTemp.size());
			testController.setLocalDataHolder(dataHolderTemp);
		}
		else {
			System.out.println("testControllerTemp is null");
		}

		String key1 = request.getParameter("key1");
		String key2 = request.getParameter("key2");
		String key3 = request.getParameter("key3");

		testController.setSessionId(session.getId());
		
		testController.doNothing();

		if(key1 != null  && key2 != null && key3 != null){
			System.out.println(key1 + "  " + key2 + "  " + key3);
		}

		if (key1 == null) {
			key1 = "key1 for URL1";
		}

		if (key2 == null) {
			key2 = "key2 for URL2";
		}
		
		if (key3 == null) {
			key3 = "key3 for URL3";
		}
		
		testController.addData(key1, key2, key3);

		//System.out.println(key1 + "  " + key2);

		session.setAttribute("key1", key1);
		session.setAttribute("key2", key2);
		session.setAttribute("key3", key3);

		session.setAttribute("dataHolder", testController.getLocalDataHolder());
		System.out.println("testController.getLocalDataHolder().size(): " + testController.getLocalDataHolder().size());

		System.out.print("ServiceServlet called: ");
		System.out.println(session.getId());
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.write(session.getId());

		String site = "http://" + request.getLocalAddr() + ":"
				+ request.getLocalPort() + request.getContextPath() + "/";
		// site = site + "servlet3";
		System.out.println(": " + response.encodeRedirectURL(site));

		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", response.encodeRedirectURL(site));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
