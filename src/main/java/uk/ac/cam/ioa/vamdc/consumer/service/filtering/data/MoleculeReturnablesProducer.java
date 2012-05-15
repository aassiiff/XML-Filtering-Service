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
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.MoleculeReturnableList;

@SessionScoped @Named
public class MoleculeReturnablesProducer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5682652715623535886L;
	
	public MoleculeReturnablesProducer(){}
	
	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;
	
	@Inject
	private List<Returnable> returnables;
	
	private ArrayList<Returnable> moleculeReturnablesList;
	
	@Produces
	@MoleculeReturnableList
	public ArrayList<Returnable> getMoleculeReturnablesList() {
		
		if (moleculeReturnablesList == null) {
			createMoleculeReturnablesList();
		}
		log.info("Producing Array List for Molecule Returnables *************************************  ************************** " + moleculeReturnablesList.size());
		return moleculeReturnablesList;
	}
	
	private void createMoleculeReturnablesList() {
		log.info("createMoleculeReturnablesList");
		moleculeReturnablesList = new ArrayList<Returnable>();
		for (Returnable returnable : returnables) {
			if(returnable.getName().toLowerCase().trim().startsWith("molecule")){
				moleculeReturnablesList.add(new Returnable(returnable));
			}
		}
	}

}
