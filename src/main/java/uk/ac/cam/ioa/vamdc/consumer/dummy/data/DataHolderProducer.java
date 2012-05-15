package uk.ac.cam.ioa.vamdc.consumer.dummy.data;


import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;

public class DataHolderProducer {
	
	
	private List<MyData> dataHolder;

	@Produces
	@DataHolder
	public List<MyData> getDataHolder() {
		if(dataHolder == null){
			dataHolder = new ArrayList<MyData>();
			
			MyData tempData = new MyData();
			
			tempData.setUrl1("URL 1 A");
			tempData.setUrl2("URL 2 B");
			tempData.setUrl3("URL 3 C");
			
			dataHolder.add(tempData);
		}
		return dataHolder;
	}	

}
