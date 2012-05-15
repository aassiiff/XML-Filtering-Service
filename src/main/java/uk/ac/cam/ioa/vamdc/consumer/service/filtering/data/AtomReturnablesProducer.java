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

@SessionScoped @Named
public class AtomReturnablesProducer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 354371752338922874L;

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;
	
	@Inject
	private List<Returnable> returnables;
	
	private ArrayList<Returnable> atomReturnablesList;
	
	public AtomReturnablesProducer(){}
	@Produces
	@AtomReturnablesList
	public ArrayList<Returnable> getAtomReturnablesList() {
		
		if (atomReturnablesList == null) {
			createAtomReturnablesList();
		}
		log.info("Producing Array List for Atom Returnables *************************************  ************************** " + atomReturnablesList.size());
		return atomReturnablesList;
	}
	
	private void createAtomReturnablesList() {
		atomReturnablesList = new ArrayList<Returnable>(returnables.size());
		for (Returnable returnable : returnables) {
			if(returnable.getName().toLowerCase().trim().startsWith("atom")){
				atomReturnablesList.add(new Returnable(returnable));
			}
		}
	}
}
