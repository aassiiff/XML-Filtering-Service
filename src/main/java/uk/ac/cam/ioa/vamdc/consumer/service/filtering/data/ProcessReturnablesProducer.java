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
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.ProcessReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SolidReturnablesList;

@SessionScoped
@Named
public class ProcessReturnablesProducer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8653951055428550266L;

	public ProcessReturnablesProducer() {
	}

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	@Inject
	private List<Returnable> returnables;

	private ArrayList<Returnable> processReturnablesList;

	@Produces
	@ProcessReturnablesList
	public ArrayList<Returnable> getProcessReturnablesList() {
		if (processReturnablesList == null) {
			createProcessReturnablesList();
		}
		log.info("Producing Array List for Process Returnables *************************************  ************************** "
				+ processReturnablesList.size());
		return processReturnablesList;
	}

	private void createProcessReturnablesList() {
		processReturnablesList = new ArrayList<Returnable>();
		for (Returnable returnable : returnables) {
			if (returnable.getName().toLowerCase().trim()
					.startsWith("collision")) {
				processReturnablesList.add(new Returnable(returnable));
			}
			if (returnable.getName().toLowerCase().trim().startsWith("cross")) {
				processReturnablesList.add(new Returnable(returnable));
			}
			if (returnable.getName().toLowerCase().trim().startsWith("nonradtran")) {
				processReturnablesList.add(new Returnable(returnable));
			}
			if (returnable.getName().toLowerCase().trim().startsWith("radtran")) {
				processReturnablesList.add(new Returnable(returnable));
			}
		}
	}

}
