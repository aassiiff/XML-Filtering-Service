package uk.ac.cam.ioa.vamdc.consumer.service.filtering.data;

import java.io.File;
import java.util.ArrayList;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;
import org.milyn.Smooks;
import org.xml.sax.SAXException;

import com.sleepycat.dbxml.XmlContainer;

//import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.Returnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.XMLDatabaseController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SubmittedQuery;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SubmittedQueriesContainer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.UploadedXSAMSContainer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.XMLDatabase;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase.BerkeleyXMLDatabase;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class FilteringApplicationProducer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 383912726669770999L;
	private ArrayList<UploadedXSAMS> uploadedXSAMS;
	private ArrayList<SubmittedQuery> submittedQuereis;
	private BerkeleyXMLDatabase xmlDatabase;

	private String rootXMLDatabaseDirectory = "/opt/jboss/VAMDCData/tempXSAMS";
	private String pathToDatabaseEnvironment = null;
	private String sessionId = null;
	private XmlContainer openedContainer = null;

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	// @Inject
	// private XMLDatabaseController xmlDatabaseController;

	@Produces
	@UploadedXSAMSContainer
	public ArrayList<UploadedXSAMS> getUploadedXSAMS() {
		// createAtomReturnablesList();
		log.info("Producing Array List for Uploaded XSAMS *************************************   **************************");
		if (uploadedXSAMS == null) {
			uploadedXSAMS = new ArrayList<UploadedXSAMS>();
			log.info("Producing Array List for Uploaded XSAMS : "
					+ uploadedXSAMS.toString());
		}

		return uploadedXSAMS;
	}

	public void setUploadedXSAMS(ArrayList<UploadedXSAMS> uploadedXSAMS) {
		this.uploadedXSAMS = null;
		this.uploadedXSAMS = uploadedXSAMS;
	}

	@Produces
	@SubmittedQueriesContainer
	public ArrayList<SubmittedQuery> getSubmittedQuereis() {
		log.info("Producing Array List for Submitted Quereis *************************************   **************************");
		submittedQuereis = new ArrayList<SubmittedQuery>();
		return submittedQuereis;
	}

	@Produces
	@XMLDatabase
	public BerkeleyXMLDatabase getXmlDatabase() {
		if (xmlDatabase == null) {
			log.info("Producing XML Database  *************************************   **************************");
			xmlDatabase = new BerkeleyXMLDatabase();
			createDatabaseDirectories();
			createXMLContainer();
		}
		return xmlDatabase;
	}

	private void createDatabaseDirectories() {
		FacesContext fc = FacesContext.getCurrentInstance();

		if (sessionId == null) {
			/*
			 * HttpServletRequest origRequest = (HttpServletRequest) fc
			 * .getExternalContext().getRequest(); String clientIPAddress =
			 * origRequest.getRemoteAddr(); clientIPAddress =
			 * clientIPAddress.replaceAll(".", "");
			 * 
			 * log.info("clientIPAddress: " + clientIPAddress);
			 */

			HttpSession session = (HttpSession) fc.getExternalContext()
					.getSession(false);
			sessionId = session.getId();
		}

		pathToDatabaseEnvironment = rootXMLDatabaseDirectory + "/" + sessionId;

		log.info("pathToDatabaseEnvironment: " + pathToDatabaseEnvironment);
		File containerDirectory = new File(pathToDatabaseEnvironment);
		if (containerDirectory.exists() == false) {
			containerDirectory.mkdir();
		}
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	private void createXMLContainer() {
		if (openedContainer == null) {
			createDatabaseDirectories();

			if (pathToDatabaseEnvironment != null) {
				if (sessionId != null) {
					openedContainer = xmlDatabase.createXMLDatabase(
							pathToDatabaseEnvironment, sessionId);
				}
			}
		}
	}
}
