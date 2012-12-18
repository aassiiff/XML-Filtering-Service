package uk.ac.cam.ioa.vamdc.consumer.service.filtering.observer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.XMLDatabaseController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.XSAMSUploadEvent;

public class FileUploadObserver {
	
	
	@Inject
	private XMLDatabaseController xmlDatabaseController;
	
	public void onFileUpload(@Observes XSAMSUploadEvent event){		
		System.out.println("Observing File Upload: " + event.getFileName() + "  " + event.getFileSize());	
		
		xmlDatabaseController.loadXMLFile("/opt/jboss/VAMDCData/tempXSAMS/", event.getFileName());
	}

}
