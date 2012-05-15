package uk.ac.cam.ioa.vamdc.consumer.service.filtering.events;

public class XSAMSUploadEvent {

	private String fileName;
	private int fileSize;

	public XSAMSUploadEvent() {

	}

	public XSAMSUploadEvent(String fileNameValue, int fileSizeValue) {
		this.fileName = fileNameValue;
		this.fileSize = fileSizeValue;
	}

	public String getFileName() {
		return fileName;
	}

	public int getFileSize() {
		return fileSize;
	}
}
