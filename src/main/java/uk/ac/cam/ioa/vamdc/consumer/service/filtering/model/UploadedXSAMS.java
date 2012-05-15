package uk.ac.cam.ioa.vamdc.consumer.service.filtering.model;

import java.io.Serializable;
import java.util.Date;

public class UploadedXSAMS implements Serializable {

    private static final long serialVersionUID = -8192553629588066292L;

    private String fileName = "";
    private String fileMime = "";
    private float fileSize;
    private String timeUploaded = "";
    private String lastAccessed = "";
    private int index;
    
    private String status;
    

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		int extDot = fileName.lastIndexOf('.');
        if (extDot > 0) {
            String extension = fileName.substring(extDot + 1);
            if ("xml".equals(extension)) {
            	fileMime = "application/xml";
            } else if ("xsams".equals(extension)) {
            	fileMime = "application/xsams";
            }  else {
            	fileMime = "image/unknown";
            }
        }
		this.fileName = fileName;
	}

	public String getFileMime() {
		return fileMime;
	}

	public void setFileMime(String fileMime) {
		this.fileMime = fileMime;
	}

	public float getFileSize() {
		return fileSize;
	}

	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}

	public String getTimeUploaded() {
		return timeUploaded;
	}

	public void setTimeUploaded(String timeUploaded) {
		this.timeUploaded = timeUploaded;
	}

	public String getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(String lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
