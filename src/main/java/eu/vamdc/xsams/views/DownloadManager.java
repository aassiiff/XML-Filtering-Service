package eu.vamdc.xsams.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.UploadedXSAMS;

public class DownloadManager implements Runnable{

	private URL[] urls;
	private ArrayList<UploadedXSAMS> uploadedFiles;

	//private List<Future<UploadedXSAMS>> futures;

	public DownloadManager(URL[] urlValues,
			ArrayList<UploadedXSAMS> uploadedFilesValues) {
		this.urls = urlValues;
		this.uploadedFiles = uploadedFilesValues;
	}

	// "/opt/jboss/VAMDCData/tempXSAMS/"
	public void run() {

		//futures = new ArrayList<Future<UploadedXSAMS>>();

		ExecutorService service = Executors.newFixedThreadPool(25);
		CompletionService<UploadedXSAMS> cs = new ExecutorCompletionService<UploadedXSAMS>(service);

		UploadedXSAMS tempXSAMS;
		for (int i = 0; i < urls.length; i++) {
			tempXSAMS = new UploadedXSAMS();
			String fileName = generateUUID();
			
			tempXSAMS.setFileName(fileName);
			tempXSAMS.setStatus("downloading ...");
			cs.submit(new DownloadThread(urls[i].toString(),
					fileName, tempXSAMS));
			uploadedFiles.add(tempXSAMS);
		}
/*
		for (int i =0; i < urls.length; i++){
            
			try {
				tempXSAMS = cs.take().get();
				System.out.println(tempXSAMS.getFileName() + tempXSAMS.getStatus());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
            
        }
*/
	}

	protected String generateUUID() {
		String tempFileName = ""; //"/opt/jboss/VAMDCData/tempXSAMS/";
		tempFileName = tempFileName + UUID.randomUUID().toString();
		tempFileName = tempFileName + ".xsams";

		return tempFileName;
	}
}
