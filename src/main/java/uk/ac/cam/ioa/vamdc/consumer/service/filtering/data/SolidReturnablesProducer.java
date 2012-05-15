package uk.ac.cam.ioa.vamdc.consumer.service.filtering.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.Returnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.AtomReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SolidReturnablesList;


@SessionScoped @Named
public class SolidReturnablesProducer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3490934851541484866L;
	
	public SolidReturnablesProducer(){}
	
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;
	
	@Inject
	private List<Returnable> returnables;
	
	private ArrayList<Returnable> solidReturnablesList;
	
	@Produces
	@SolidReturnablesList
	public ArrayList<Returnable> getSolidReturnablesList() {
		
		if (solidReturnablesList == null) {
			createSolidReturnablesList();
		}
		log.info("Producing Array List for Solid Returnables *************************************  ************************** " + solidReturnablesList.size());
		return solidReturnablesList;
	}
	
	private void createSolidReturnablesList() {
		solidReturnablesList = new ArrayList<Returnable>();
		for (Returnable returnable : returnables) {
			if(returnable.getName().toLowerCase().trim().startsWith("particle") || returnable.getName().toLowerCase().trim().startsWith("solid")){
				solidReturnablesList.add(new Returnable(returnable));
			}
		}
	}

}
