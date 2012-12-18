package uk.ac.cam.ioa.vamdc.consumer.service.filtering.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.DeleteXSAMSEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.events.XSAMSUploadEvent;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase.BerkeleyXMLDatabase;

public class DownloadThread implements Callable<UploadedXSAMS> {
	
	private String directoryPath = "/opt/jboss/VAMDCData/tempXSAMS/";
	
	private UploadedXSAMS downloadedXSAMS;
	
	private Event<XSAMSUploadEvent> myFileUploadEvent;
	
	private BerkeleyXMLDatabase xmlDatabase;
	
	private boolean downloadFinished = false;
	private String url;
    private String fileName;
	
    @SuppressWarnings("unused")
	private DownloadThread() {}
  
	public DownloadThread(String urlValue, String fileNameValue, UploadedXSAMS uploadedXSAMSValues, 
			BerkeleyXMLDatabase xmlDatabase){
		this.url = urlValue;
        this.fileName = fileNameValue;
        downloadedXSAMS = uploadedXSAMSValues;
        this.xmlDatabase = xmlDatabase;
	}
	
	public UploadedXSAMS call(){
		
		System.out.println("Thread Started");
		
		String pattern = "dd/MM/yyyy";
        Format formatter =  new SimpleDateFormat(pattern);
		
		try {
            URL tempURL = new URL(url );
            ReadableByteChannel rbc = Channels.newChannel(tempURL.openStream());
            
            File file = new File(directoryPath, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            //System.out.println();
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            System.out.println(fos.getChannel().size() / 1024 + " : " + file.getAbsolutePath());
            
            //downloadedXSAMS.setFileName(fileName);
            downloadedXSAMS.setFileSize(fos.getChannel().size() / 1024);
            
            downloadedXSAMS.setFileMime("xsams");
            
            downloadedXSAMS.setLastAccessed(formatter.format(new Date()));
            downloadedXSAMS.setTimeUploaded(formatter.format(new Date()));
            downloadedXSAMS.setStatus("downloaded");
            
            //myFileUploadEvent.fire(new XSAMSUploadEvent(downloadedXSAMS.getFileName(), (int) downloadedXSAMS.getFileSize())); 
            xmlDatabase.loadXMLFileInContainer("/opt/jboss/VAMDCData/tempXSAMS/", downloadedXSAMS.getFileName());
            
            downloadFinished = true;
        } catch (Exception e) {
        	 downloadedXSAMS.setStatus("download failed");
            e.printStackTrace();
        }
		
		return downloadedXSAMS;
	}

	public UploadedXSAMS getDownloadedXSAMS() {
		return downloadedXSAMS;
	}

	public void setDownloadedXSAMS(UploadedXSAMS downloadedXSAMS) {
		this.downloadedXSAMS = downloadedXSAMS;
	}

	public boolean isDownloadFinished() {
		return downloadFinished;
	}

	public void setDownloadFinished(boolean downloadFinished) {
		this.downloadFinished = downloadFinished;
	}
	
}
