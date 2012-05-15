package uk.ac.cam.ioa.vamdc.consumer.service.filtering.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import javax.inject.Inject;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.Returnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SelectedReturnable;
import java.io.Serializable;

@RequestScoped
public class SelectedReturnablesProducer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9110641619513304817L;

	/* */
	private List<SelectedReturnable> selectedReturnablesList = new ArrayList<SelectedReturnable>();

	private boolean atomsRestrictable = false;
	private boolean processRestrictables = false;
	
	@Inject
	MoleculeReturnablesProducer moleculeReturnablesProducer;

	@Inject
	AtomReturnablesProducer atomReturnablesProducer;

	@Inject
	SolidReturnablesProducer solidReturnablesProducer;

	@Inject
	ProcessReturnablesProducer processReturnablesProducer;

	@Inject
	EnvironmentReturnablesProducer environmentReturnablesProducer;

	private ArrayList<Returnable> atomReturnablesList;
	private ArrayList<Returnable> moleculeReturnablesList;
	private ArrayList<Returnable> solidReturnablesList;
	private ArrayList<Returnable> processReturnablesList;
	private ArrayList<Returnable> environmentReturnablesList;

	@Produces
	@Named
	public List<SelectedReturnable> getSelectedReturnablesList() {
		System.out.println(" @@@@@@@@@@@   getSelectedReturnablesList");
		moleculeReturnablesList = moleculeReturnablesProducer
				.getMoleculeReturnablesList();
		atomReturnablesList = atomReturnablesProducer.getAtomReturnablesList();
		solidReturnablesList = solidReturnablesProducer
				.getSolidReturnablesList();
		processReturnablesList = processReturnablesProducer
				.getProcessReturnablesList();
		environmentReturnablesList = environmentReturnablesProducer
				.getEnvironmentReturnablesList();
		updateSelectedReturnablesList();
		return selectedReturnablesList;
	}

	private void updateSelectedReturnablesList() {
		int counter = 1;
		Iterator<Returnable> iterator = atomReturnablesList.iterator();
		while (iterator.hasNext()) {
			Returnable tempReturnable = iterator.next();
			// System.out.println(tempReturnable.isSelected());
			if (tempReturnable.isSelected()) {
				SelectedReturnable tempSR = new SelectedReturnable();
				tempSR.setName(tempReturnable.getName());
				tempSR.setShortDescription(tempReturnable.getShortDescription());
				tempSR.setUnit(tempReturnable.getUnit());
				tempSR.setLongDescription(tempReturnable.getLongDescription());
				tempSR.setColumnName(tempReturnable.getName());
				tempSR.setxQueryMapping(tempReturnable.getxQueryMapping());
				tempSR.setColumnOrder(counter++);
				addInSelectedReturnable(tempSR);
			}
		}

		iterator = moleculeReturnablesList.iterator();
		while (iterator.hasNext()) {
			Returnable tempReturnable = iterator.next();
			//System.out.println(tempReturnable.isSelected());
			if (tempReturnable.isSelected()) {
				SelectedReturnable tempSR = new SelectedReturnable();
				tempSR.setName(tempReturnable.getName());
				tempSR.setShortDescription(tempReturnable.getShortDescription());
				tempSR.setUnit(tempReturnable.getUnit());
				tempSR.setLongDescription(tempReturnable.getLongDescription());
				tempSR.setColumnName(tempReturnable.getName());
				tempSR.setxQueryMapping(tempReturnable.getxQueryMapping());
				tempSR.setColumnOrder(counter++);
				addInSelectedReturnable(tempSR);
			}
		}
		
		iterator = solidReturnablesList.iterator();
		while (iterator.hasNext()) {
			Returnable tempReturnable = iterator.next();
			//System.out.println(tempReturnable.isSelected());
			if (tempReturnable.isSelected()) {
				SelectedReturnable tempSR = new SelectedReturnable();
				tempSR.setName(tempReturnable.getName());
				tempSR.setShortDescription(tempReturnable.getShortDescription());
				tempSR.setUnit(tempReturnable.getUnit());
				tempSR.setLongDescription(tempReturnable.getLongDescription());
				tempSR.setColumnName(tempReturnable.getName());
				tempSR.setxQueryMapping(tempReturnable.getxQueryMapping());
				tempSR.setColumnOrder(counter++);
				addInSelectedReturnable(tempSR);
			}
		}
		
		iterator = processReturnablesList.iterator();
		while (iterator.hasNext()) {
			Returnable tempReturnable = iterator.next();
			//System.out.println(tempReturnable.isSelected());
			if (tempReturnable.isSelected()) {
				SelectedReturnable tempSR = new SelectedReturnable();
				tempSR.setName(tempReturnable.getName());
				tempSR.setShortDescription(tempReturnable.getShortDescription());
				tempSR.setUnit(tempReturnable.getUnit());
				tempSR.setLongDescription(tempReturnable.getLongDescription());
				tempSR.setColumnName(tempReturnable.getName());
				tempSR.setxQueryMapping(tempReturnable.getxQueryMapping());
				tempSR.setColumnOrder(counter++);
				addInSelectedReturnable(tempSR);
			}
		}
		
		iterator = environmentReturnablesList.iterator();
		while (iterator.hasNext()) {
			Returnable tempReturnable = iterator.next();
			//System.out.println(tempReturnable.isSelected());
			if (tempReturnable.isSelected()) {
				SelectedReturnable tempSR = new SelectedReturnable();
				tempSR.setName(tempReturnable.getName());
				tempSR.setShortDescription(tempReturnable.getShortDescription());
				tempSR.setUnit(tempReturnable.getUnit());
				tempSR.setLongDescription(tempReturnable.getLongDescription());
				tempSR.setColumnName(tempReturnable.getName());
				tempSR.setxQueryMapping(tempReturnable.getxQueryMapping());
				tempSR.setColumnOrder(counter++);
				addInSelectedReturnable(tempSR);
			}
		}
	}

	private void addInSelectedReturnable(SelectedReturnable tempSR) {
		boolean exists = false;
		Iterator<SelectedReturnable> iterator = selectedReturnablesList
				.iterator();
		while (iterator.hasNext()) {
			SelectedReturnable tempReturnable = iterator.next();
			if (tempReturnable.getName().trim()
					.equalsIgnoreCase(tempSR.getName().trim())) {
				exists = true;
				return;
			}
		}
		if (!exists) {
			selectedReturnablesList.add(tempSR);
		}
	}

	public boolean isAtomsRestrictable() {
		return atomsRestrictable;
	}

	public void setAtomsRestrictable(boolean atomsRestrictable) {
		this.atomsRestrictable = atomsRestrictable;
	}

	public boolean isProcessRestrictables() {
		return processRestrictables;
	}

	public void setProcessRestrictables(boolean processRestrictables) {
		this.processRestrictables = processRestrictables;
	}
	
	
}
