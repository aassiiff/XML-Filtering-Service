package uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility;

import java.io.IOException;
import java.util.Properties;

public class ReturnableXQueryMapping {
	
	private Properties properties;
	
	public ReturnableXQueryMapping(){
		initializeHashMap();
	}
	
	private void initializeHashMap(){
		properties = new Properties();
		try {
		    properties.load(this.getClass()
		            .getResourceAsStream("returnableMapping.properties"));
		    System.out.println("properties.size(): " + properties.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPropertyValue(String property){
		String value = properties.getProperty(property);
		//System.out.println(property + "  " + value);
		return value;
	}

}
