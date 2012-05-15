package uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Startup;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.Returnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SelectedReturnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SubmittedQuery;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SubmittedQueriesContainer;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.XMLDatabase;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.thread.QueryExecuter;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.QueryBuilderUtility;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase.BerkeleyXMLDatabase;

@SessionScoped @Named
public class QueryFilterController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2816916846492091228L;

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	@Inject
	private XMLDatabaseController xmlDatabaseController;
	
	@Inject
	@SubmittedQueriesContainer
	private ArrayList<SubmittedQuery> submittedQuereis;

	private String queryVersion = "0.3";
	private String queryToExecute;

	private String queryResult;

	public String getQueryVersion() {
		return queryVersion;
	}

	public void setQueryVersion(String queryVersion) {
		this.queryVersion = queryVersion;
	}

	public String getQueryToExecute() {
		return queryToExecute;
	}

	public void setQueryToExecute(String queryToExecute) {
		this.queryToExecute = queryToExecute;
	}

	public String getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}

	public ArrayList<SubmittedQuery> getSubmittedQuereis() {
		return submittedQuereis;
	}

	public void executeQuery() {
		log.info("executeQuery: " + queryVersion);
		log.info("executeQuery: " + queryToExecute);

		try {
			queryResult = xmlDatabaseController.getXmlDatabase().doQuery(queryToExecute, queryVersion);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String executeQueryFromSelectable(
			List<SelectedReturnable> selectedReturnablesList) {
		if(xmlDatabaseController != null){
			System.out.println("xmlDatabaseController: " + xmlDatabaseController.toString());
			if(xmlDatabaseController.getXmlDatabase() != null){
				System.out.println("xmlDatabaseController.xmlDatabase");
				if(xmlDatabaseController.getXmlDatabase() != null){
					System.out.println("xmlDatabaseController.xmlDatabase.getDatabaseName(): " + xmlDatabaseController.getXmlDatabase().getDatabaseName());
				}
			}
		} else {
			System.out.println("xmlDatabaseController is null");
		}
		
		/**/
		String flworQuery =new QueryBuilderUtility().buildQuery(selectedReturnablesList, xmlDatabaseController.getXmlDatabase().getDatabaseName());
		
		SubmittedQuery submittedQuery = new SubmittedQuery();
		
		submittedQuery.setQuery(flworQuery);
		submittedQuery.generateUUID();
		submittedQuery.setSelectedReturnablesList(selectedReturnablesList);
		submittedQuery.setIndexInList(submittedQuereis.size());
		
		submittedQuereis.add(submittedQuery);
		
		QueryExecuter queryExecuter = new QueryExecuter();
		//queryExecuter.setSelectedReturnablesList(selectedReturnablesList);
		queryExecuter.setXmlDatabase(xmlDatabaseController.getXmlDatabase());
		queryExecuter.setSubmittedQuery(submittedQuery);
		
		Thread xQueryThread = null;
		xQueryThread = new Thread(queryExecuter);
		
		xQueryThread.start();
		return "next";
	}
			
}
