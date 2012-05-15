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

	// private ArrayList<Returnable> selectedReturnablesList;
	//private ArrayList<Returnable> returnablesList;

	/*
	private ArrayList<Returnable> moleculeReturnablesList;

	private ArrayList<Returnable> solidReturnablesList;
	private ArrayList<Returnable> processReturnablesList;
	private ArrayList<Returnable> environmentReturnablesList;
*/
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	

	/*
	@Produces
	@SelectedReturnablesList
	public ArrayList<Returnable> getSelectedReturnablesList() {
		log.info("Producing Array List for Selected Returnables *************************************   **************************");
		selectedReturnablesList = new ArrayList<Returnable>();
		return selectedReturnablesList;
	}
	*/
	/*
	// Returnable List used in the session only
	@Produces
	@ReturnablesList
	public ArrayList<Returnable> getreturnablesList() {
		log.info("Producing Array List for Returnables *************************************   ************************** "
				+ returnablesList.size());
		createReturnablesList();
		return returnablesList;
	}
	 
	
	
	

	@Produces
	@MoleculeReturnableList
	public ArrayList<Returnable> getMoleculeReturnablesList() {
		moleculeReturnablesList = new ArrayList<Returnable>();
		log.info("Producing Array List for Returnables *************************************   ************************** ");
		return moleculeReturnablesList;
	}

	@Produces
	@SolidReturnablesList
	public ArrayList<Returnable> getSolidReturnablesList() {
		solidReturnablesList = new ArrayList<Returnable>();
		log.info("Producing Array List for Returnables *************************************   ************************** ");
		return solidReturnablesList;
	}

	@Produces
	@ProcessReturnablesList
	public ArrayList<Returnable> getProcessReturnablesList() {
		processReturnablesList = new ArrayList<Returnable>();
		log.info("Producing Array List for Returnables *************************************   ************************** ");
		return processReturnablesList;
	}

	@Produces
	@EnvironmentReturnablesList
	public ArrayList<Returnable> getEnvironmentReturnablesList() {
		environmentReturnablesList = new ArrayList<Returnable>();
		log.info("Producing Array List for Returnables *************************************   ************************** ");
		return environmentReturnablesList;
	}
*/
	@Produces
	@UploadedXSAMSContainer
	public ArrayList<UploadedXSAMS> getUploadedXSAMS() {
		//createAtomReturnablesList();
		log.info("Producing Array List for Uploaded XSAMS *************************************   **************************");
		if(uploadedXSAMS == null){
			log.info("Producing Array List for Uploaded XSAMS : " +  uploadedXSAMS);
			uploadedXSAMS = new ArrayList<UploadedXSAMS>();
		}
		
		return uploadedXSAMS;
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
		log.info("Producing XML Database  *************************************   **************************");
		xmlDatabase = new BerkeleyXMLDatabase();
		createDatabaseDirectories();
		createXMLContainer();
		return xmlDatabase;
	}

	private void createDatabaseDirectories() {
		FacesContext fc = FacesContext.getCurrentInstance();

		HttpServletRequest origRequest = (HttpServletRequest) fc
				.getExternalContext().getRequest();
		String clientIPAddress = origRequest.getRemoteAddr();
		clientIPAddress = clientIPAddress.replaceAll(".", "");

		log.info("clientIPAddress: " + clientIPAddress);

		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
				false);
		sessionId = session.getId();

		pathToDatabaseEnvironment = rootXMLDatabaseDirectory + "/"
				+ clientIPAddress + "/" + sessionId;

		log.info("pathToDatabaseEnvironment: " + pathToDatabaseEnvironment);
		File containerDirectory = new File(pathToDatabaseEnvironment);
		if (containerDirectory.exists() == false) {
			containerDirectory.mkdir();
		}
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
