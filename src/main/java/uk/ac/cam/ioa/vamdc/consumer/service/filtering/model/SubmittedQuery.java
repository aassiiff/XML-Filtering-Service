package uk.ac.cam.ioa.vamdc.consumer.service.filtering.model;

import java.util.List;
import java.util.UUID;

import java.io.InputStream;  
import javax.faces.context.FacesContext;  
import javax.servlet.ServletContext;  
  
import org.primefaces.model.DefaultStreamedContent;  
import org.primefaces.model.StreamedContent;  

public class SubmittedQuery {
	
	private String csvFileRoot = "/opt/jboss/VAMDCData/csv/";
	private String htmlFileRoot;
	
	private String query;
	private boolean queryStatus = false;
	private String queryResult = "Not Received";
	
	private String csvFile;
	private int csvFileSize = 0;
	private String htmlFile;
	
	private String queryID;
	
	private UUID uuid 					= null;
	private int indexInList = 0;
	
	private List<SelectedReturnable> selectedReturnablesList;
	
	private StreamedContent file;  
	
	public void generateUUID() {
        this.uuid = UUID.randomUUID();
        this.queryID = this.uuid.toString();
    }

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(boolean queryStatus) {
		this.queryStatus = queryStatus;
	}

	public String getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}

	public String getQueryID() {
		return queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}

	public int getIndexInList() {
		return indexInList;
	}

	public void setIndexInList(int indexInList) {
		this.indexInList = indexInList;
	}

	public List<SelectedReturnable> getSelectedReturnablesList() {
		return selectedReturnablesList;
	}

	public void setSelectedReturnablesList(
			List<SelectedReturnable> selectedReturnablesList) {
		this.selectedReturnablesList = selectedReturnablesList;
	}

	public String getCsvFile() {
		csvFile = /*csvFileRoot + */this.queryID + ".csv";
		return csvFile;
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public String getHtmlFile() {
		return htmlFile;
	}

	public void setHtmlFile(String htmlFile) {
		this.htmlFile = htmlFile;
	}

	public StreamedContent getFile() {
		 InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(getCsvFile());  
	     file = new DefaultStreamedContent(stream, "application/csv", this.queryID + ".csv");  
		return file;
	}

	public int getCsvFileSize() {
		return csvFileSize;
	}

	public void setCsvFileSize(int csvFileSize) {
		this.csvFileSize = csvFileSize;
	}		
	
}
