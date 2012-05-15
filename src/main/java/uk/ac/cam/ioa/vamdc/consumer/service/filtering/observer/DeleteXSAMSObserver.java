package uk.ac.cam.ioa.vamdc.consumer.service.filtering.observer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller.XMLDatabaseController;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.DeleteXSAMSEvent;

public class DeleteXSAMSObserver {
	

	@Inject
	private XMLDatabaseController xmlDatabaseController;
	
	public void onDeleteXSAMS(@Observes DeleteXSAMSEvent event){		
		System.out.println("Observing XSAMS Delete: " + event.getFileName() + "  " + event.getFileName());	
		
		xmlDatabaseController.deleateXMLFile(event.getFileName());
	}

}
