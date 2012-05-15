package uk.ac.cam.ioa.vamdc.consumer.service.filtering.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SelectedReturnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SubmittedQuery;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase.BerkeleyXMLDatabase;

public class QueryExecuter implements Runnable {
	
	private SubmittedQuery submittedQuery;
	private BerkeleyXMLDatabase xmlDatabase;
	

	public BerkeleyXMLDatabase getXmlDatabase() {
		return xmlDatabase;
	}

	public void setXmlDatabase(BerkeleyXMLDatabase xmlDatabase) {
		this.xmlDatabase = xmlDatabase;
	}


	public SubmittedQuery getSubmittedQuery() {
		return submittedQuery;
	}

	public void setSubmittedQuery(SubmittedQuery submittedQuery) {
		this.submittedQuery = submittedQuery;
	}

	public void run(){
		if(submittedQuery.getQuery() != null){
			xmlDatabase.doQuery(submittedQuery);
		}
		File file;
		file = new File("/opt/jboss/VAMDCData/csv/" + submittedQuery.getQueryID() + ".csv");
		int fileSize = writeCSV(submittedQuery.getQueryResult(), file);
		
		submittedQuery.setCsvFileSize(fileSize);
		
		boolean fileNotexist = true;
		while(fileNotexist){
			if (!file.exists()) {
				continue;
			} else {
				submittedQuery.setQueryStatus(true);
			}
			fileNotexist = false;
		}	
	}
	
	public int writeCSV(String queryResult, File file){
		FileOutputStream fop = null;
		int fileSize = 0;
		try {
			
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			byte[] contentInBytes = queryResult.getBytes();
			
			fileSize = (contentInBytes.length / 1024);
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("CSV File Written");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return fileSize;
	}

}
