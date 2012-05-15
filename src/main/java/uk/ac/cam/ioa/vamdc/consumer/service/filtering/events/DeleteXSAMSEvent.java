package uk.ac.cam.ioa.vamdc.consumer.service.filtering.events;

public class DeleteXSAMSEvent {
	private String fileName;

	public DeleteXSAMSEvent(){
		
	}
	
	public DeleteXSAMSEvent(String fileName) {
		super();
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
