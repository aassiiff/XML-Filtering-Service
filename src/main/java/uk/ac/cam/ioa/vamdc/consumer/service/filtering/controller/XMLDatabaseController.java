package uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ejb.Startup;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.XMLDatabase;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase.BerkeleyXMLDatabase;

@Startup
@ManagedBean
@SessionScoped
public class XMLDatabaseController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7241797009002822683L;


	@Inject
    @Category("vamdc-xml-db-consumer-service")
    private Logger log;
	
	@Inject
	@XMLDatabase
	private BerkeleyXMLDatabase xmlDatabase;
	
	public XMLDatabaseController(){
		//log.info("XMLDatabaseController()");
	}
	/*
	public XMLDatabaseController(BerkeleyXMLDatabase xmlDatabase){
		this.xmlDatabase = xmlDatabase;
	}
	*/


	public void loadXMLFile(String pathToFileValue, String fileValue) {
		//
		log.info("loadXMLFile: " + pathToFileValue + "  " +  fileValue);
		
		xmlDatabase.loadXMLFileInContainer(pathToFileValue, fileValue);
		/**/
		try {
			//xmlDatabase.doQuery("//*/Sources/Source/Category");
			
			//xmlDatabase.doQuery("//*/Species/Atoms/Atom/ChemicalElement/ElementSymbol");
		} catch (Throwable e) {
			e.printStackTrace();
		}	
	}
	
	public void deleateXMLFile(String fileName){
		xmlDatabase.deleteXMLFile(fileName);
	}

	public BerkeleyXMLDatabase getXmlDatabase() {
		return xmlDatabase;
	}

	public void setXmlDatabase(BerkeleyXMLDatabase xmlDatabase) {
		this.xmlDatabase = xmlDatabase;
	}
	
	

}
