package uk.ac.cam.ioa.vamdc.consumer.service.filtering.model;

import org.metawidget.inspector.annotation.UiLabel;

public class SelectedReturnable {
	
	private String name;
	private String shortDescription;
	private String longDescription;
	private String type;
	
	private String unit;
	
	@UiLabel( "" )
	private String columnName;
	@UiLabel( "" )
	private int columnOrder;
	
	@UiLabel( "" )
	private boolean removed = false;
	
	@UiLabel( "" )
	private String xQueryMapping = "";
	
	public SelectedReturnable(){
		
	}
	
	@UiLabel( "" )
	private boolean hasUnit = false;

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

	public boolean isHasUnit() {
		return hasUnit;
	}

	public void setHasUnit(boolean hasUnit) {
		this.hasUnit = hasUnit;
	}

	@UiLabel( "" )
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@UiLabel( "" )
	public int getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}

	@UiLabel( "" )
	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getxQueryMapping() {
		return xQueryMapping;
	}

	public void setxQueryMapping(String xQueryMapping) {
		this.xQueryMapping = xQueryMapping;
	}		
	
}
