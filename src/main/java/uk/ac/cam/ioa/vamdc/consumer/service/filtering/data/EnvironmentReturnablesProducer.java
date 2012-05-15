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
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.EnvironmentReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SolidReturnablesList;

@SessionScoped @Named
public class EnvironmentReturnablesProducer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4569323483494005854L;
	
	public EnvironmentReturnablesProducer(){}
	
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;
	
	@Inject
	private List<Returnable> returnables;
	
	private ArrayList<Returnable> environmentReturnablesList;

	@Produces
	@EnvironmentReturnablesList
	public ArrayList<Returnable> getEnvironmentReturnablesList() {
		if(environmentReturnablesList == null){
		createEnvironmentReturnablesList();
		}
		log.info("Producing Array List for Environment Returnables *************************************  ************************** " + environmentReturnablesList.size());
		return environmentReturnablesList;
	}
	
	private void createEnvironmentReturnablesList() {
		environmentReturnablesList = new ArrayList<Returnable>();
		for (Returnable returnable : returnables) {
			if(returnable.getName().toLowerCase().trim().startsWith("environment") || returnable.getName().toLowerCase().trim().startsWith("function")
					|| returnable.getName().toLowerCase().trim().startsWith("method") || returnable.getName().toLowerCase().trim().startsWith("source")){
				environmentReturnablesList.add(new Returnable(returnable));
			}
		}
	}
	

}
