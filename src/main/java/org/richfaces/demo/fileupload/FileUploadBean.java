package org.richfaces.demo.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.enterprise.context.SessionScoped;

import javax.inject.Inject;
import javax.inject.Named;
 
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.DeleteXSAMSEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.XSAMSUploadEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.UploadedXSAMSContainer;

//@UploadInterceptor
//@ManagedBean  // ManagedBean annotation never let intercepter called. Changed to Named made everything work as it should be.

@SessionScoped @Named
public class FileUploadBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2215468753673161974L;
	private UploadedXSAMS selectedXSAMS;
	
	
	@Inject Event<XSAMSUploadEvent> myFileUploadEvent;
	
	@Inject Event<DeleteXSAMSEvent> deleteXSAMSEvent;
	
	@Inject
	@UploadedXSAMSContainer
	private ArrayList<UploadedXSAMS> uploadedFiles /*uploadedXSAMS*/;

	@Produces
    @Named
    public UploadedXSAMS getSelectedXSAMS() {
        return selectedXSAMS;
    }
	
    //@SuppressWarnings("rawtypes")
    //@UploadInterceptor
    public void listener(FileUploadEvent event) throws Exception {
    	
    	UploadedFile item = event.getUploadedFile();
    	 if(item != null){
    		 InputStream inputStream = item.getInputStream();
    		// write the inputStream to a FileOutputStream
    			OutputStream out = new FileOutputStream(new File("/opt/jboss/VAMDCData/tempXSAMS/" + item.getName()));
    			int read = 0;
    			byte[] bytes = new byte[1024];
    		 
    			while ((read = inputStream.read(bytes)) != -1) {
    				out.write(bytes, 0, read);
    			}
    		 
    			inputStream.close();
    			out.flush();
    			out.close();
    		 
    			System.out.println("New file created!");
    	 }
        System.out.println("listener(FileUploadEvent event) : " + item.getName() + " "  + this.toString());
        System.out.println("listener(FileUploadEvent event) : " + item.getData().length);
        
        String pattern = "dd/MM/yyyy";
        Format formatter =  new SimpleDateFormat(pattern);
        
        
        UploadedXSAMS file = new UploadedXSAMS();
        file.setFileSize((item.getData().length)/1024);
        file.setFileName(item.getName());
        file.setTimeUploaded(formatter.format(new Date()));
        file.setLastAccessed(formatter.format(new Date()));
        file.setIndex(uploadedFiles.size());
       
        uploadedFiles.add(file);
        
        System.out.println("uploadedFiles.size() : " + uploadedFiles.size());
        myFileUploadEvent.fire(new XSAMSUploadEvent(item.getName(), item.getData().length));       
    }
 
    public String clearUploadData() {
    	uploadedFiles.clear();
        return null;
    }
 
    public int getSize() {
        if (getUploadedFiles().size() > 0) {
            return getUploadedFiles().size();
        } else {
            return 0;
        }
    }
 
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

	public ArrayList<UploadedXSAMS> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(ArrayList<UploadedXSAMS> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}
	
	public void initSelectedXSAMS(int index){
		System.out.println("initSelectedXSAMS(int " + index + ")");
		if(!uploadedFiles.isEmpty()){
			for(int i = 0; i < uploadedFiles.size(); i++){
				UploadedXSAMS tempSelectedXSAMS = uploadedFiles.get(i);
				if(tempSelectedXSAMS.getIndex() == index){
					selectedXSAMS = tempSelectedXSAMS;
					deleteXSAMSEvent.fire(new DeleteXSAMSEvent(tempSelectedXSAMS.getFileName()));
					uploadedFiles.remove(i);
					return;
				}
			}		
		}
	}
}
