package uk.ac.cam.ioa.vamdc.consumer.service.filtering.model;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.metawidget.inspector.annotation.UiLabel;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.BeanManagerUtility;

//@ManagedBean
public class Returnable {
	
	
	
	private List<SelectedReturnable> selectedReturnablesList;
	
	private String name = "";
	private String shortDescription = "";
	private String longDescription = "";
	private String type = "";
	
	private String unit;
	
	@UiLabel( "" )
	private boolean hasUnit = false;
	
	private boolean selected = false;
	
	private boolean hasXQueryMapping = false;
	
	private String xQueryMapping = "";
	
	
	public Returnable(String name, String shortDescription,
			String longDescription, String type, String unit) {
		super();
		this.name = name;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.type = type;
		this.unit = unit;
	}
	
	public Returnable(Returnable returnable){
		this.name = returnable.getName();
		this.shortDescription = returnable.getShortDescription();
		this.longDescription = returnable.getLongDescription();
		this.type = returnable.getType();
		this.unit = returnable.getUnit();
		this.xQueryMapping = returnable.getxQueryMapping();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getShortDescription() {
		return shortDescription;
	}


	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}


	public String getLongDescription() {
		return longDescription;
	}


	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@UiLabel( "" )
	public boolean isHasUnit() {
		return hasUnit;
	}


	public void setHasUnit(boolean hasUnit) {
		this.hasUnit = hasUnit;
	}

	@UiLabel( "" )
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		//System.out.println(this.name + " " + selected);
		this.selected = selected;
	}
	
	public String getxQueryMapping() {
		return xQueryMapping;
	}

	public void setxQueryMapping(String xQueryMapping) {
		this.xQueryMapping = xQueryMapping;
	}

	
	public boolean isHasXQueryMapping() {
		if(xQueryMapping != null)
			return true;
		return hasXQueryMapping;
	}

	public void setHasXQueryMapping(boolean hasXQueryMapping) {
		this.hasXQueryMapping = hasXQueryMapping;
	}

	@SuppressWarnings("unchecked")
	public void a4jTest(AjaxBehaviorEvent event){
		this.selected = !this.selected;
		//System.out.println(this.name + "  " + this.selected);
		if(selectedReturnablesList == null){
			System.out.println("selectedReturnablesList is null");
			selectedReturnablesList = (List<SelectedReturnable>)(new BeanManagerUtility().getBeanByName("selectedReturnablesList"));
		} 
		
		if(selectedReturnablesList == null){
			System.out.println("selectedReturnablesList is STILL null");
		}
    }
	
}
