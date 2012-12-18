package uk.ac.cam.ioa.vamdc.consumer.service.filtering.xmldatabase;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.enterprise.context.SessionScoped;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SubmittedQuery;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.XMLDatabaseEnvironment;

import com.sleepycat.db.*;
import com.sleepycat.dbxml.*;

//@SessionScoped
public class BerkeleyXMLDatabase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546106267157271162L;
	private XmlManager theMgr = null;
	private XmlContainer openedContainer = null;
	private Environment env = null;
	
	private String databaseName = null;
	private String collection = null; // "collection('" +
												// TestProject171011_TestXMLDB.dbxml')";

	private void xmlManager(String containerPath) {

		System.out.println("xmlManager(String containerPath)");
		File path2DbEnv = new File(containerPath);
		try {
			env = XMLDatabaseEnvironment.createEnvironment(path2DbEnv);

			theMgr = new XmlManager(env, new XmlManagerConfig());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public XmlContainer createXMLDatabase(String containerPath,
			String databaseName) {
		xmlManager(containerPath);
		XmlContainerConfig config = new XmlContainerConfig();
		try {
			if (theMgr.existsContainer(databaseName + ".dbxml") == 0) {
				openedContainer = theMgr.createContainer(databaseName
						+ ".dbxml", config);
			}

			openedContainer = theMgr.openContainer(databaseName + ".dbxml",
					config);
			this.databaseName = databaseName + ".dbxml";
			collection = "collection('" + databaseName + ".dbxml')";
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		System.out.println("XML Container Called");
		return openedContainer;
	}

	public void loadXMLFileInContainer(String pathToFileValue, String fileValue) {
		System.out.println(pathToFileValue + ":  " + fileValue);
		try {
			/* */
			if(theMgr != null){
			XmlInputStream inputSteam = theMgr
					.createLocalFileInputStream(pathToFileValue + fileValue);

			openedContainer.putDocument(fileValue, inputSteam,
					XmlDocumentConfig.DEFAULT);

			System.out.println("Added " + fileValue + " to container "
					+ openedContainer.getName() + " : " + openedContainer.getNumDocuments() + "\n");		
			System.out.println(this.toString());
			
			} else {
				System.out.println("The Manager is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadXMLFileInContainer(String pathToFileValue, String fileValue, String containerPath,
			String databaseName) {
		if(theMgr == null){
			createXMLDatabase(containerPath, databaseName);
		}
		
		loadXMLFileInContainer(pathToFileValue, fileValue);
		
	}

	public void deleteXMLFile(String fileName) {
		System.out.println("deleteXMLFile(String " + fileName + ")");
		try {
			openedContainer.deleteDocument(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String doQuery(String query, String version) throws Throwable {
		// Perform a single query against the referenced container.
		// No context is used for this query.
		String fullQuery = collection + query;
		System.out.println("Exercising query: '" + fullQuery + "'.");

		String queryResult = "";

		// Perform the query

		try {
			XmlQueryContext context = theMgr.createQueryContext();
			if (version.equals("0.3")) {
				context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
			} else {
				context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
			}
			XmlResults results = theMgr.query(fullQuery, context, null);
			// Iterate over the results of the query using an XmlValue object
			XmlValue value;
			while ((value = results.next()) != null) {
				// System.out.println(value.asString());
				queryResult = queryResult + value.asString() + "\n";
			}

			System.out.println(results.size() + " results returned for query '"
					+ fullQuery + "'.");
			results.delete();
		} catch (Exception e) {
			queryResult = "Failed to execute query";
		}

		return queryResult;
	}

	public String doQuery(ArrayList<String> queries, ArrayList<String> headers) {
		String queryResult = "";
		System.out.println(queries.size() + "  " + headers.size());
		String fullQuery = collection + queries.get(0);
		
		for (int i = 0; i < headers.size(); i++) {
			if (i == 0) {
				queryResult = headers.get(i);
			} else {
				queryResult = queryResult + "\t \t" + headers.get(i);
			}
		}
		queryResult = queryResult + "\n";
		try {
			XmlQueryContext context = theMgr.createQueryContext();
			context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
			
			XmlResults results = theMgr.query(fullQuery, context, null);
			XmlValue value;
			
			while ((value = results.next()) != null) {
				// System.out.println(value.asString());
				queryResult = queryResult + value.getNodeValue() + "\n";
			}
		} catch (Exception e) {
			queryResult = "Failed to execute query";
		}

		return queryResult;

	}
	
	
	public String doQuery(String flworQuery){
		String queryResult = "";
		
		queryResult = queryResult + "\n";
		/**/
		try {
			XmlQueryContext context = theMgr.createQueryContext();
			context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
			
			XmlResults results = theMgr.query(flworQuery, context, null);
			XmlValue value;
			System.out.println(results.size());
			while ((value = results.next()) != null) {
				System.out.print(value.asString());
				queryResult = queryResult + value.asString() + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			queryResult = "Failed to execute query";
		}
		
		System.out.println("Query Finished");				
		return queryResult;
	} //
	public void doQuery(SubmittedQuery submittedQuery){
		StringBuilder queryResult = new StringBuilder();
		
		queryResult.append("\n");
		/**/
		try {
			XmlQueryContext context = theMgr.createQueryContext();
			context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
			
			XmlResults results = theMgr.query(submittedQuery.getQuery(), context, null);
			XmlValue value;
			
			//int resultsNumber =  results.size();
			//System.out.println("XML Rsults Size: " + resultsNumber);
			
			while ((value = results.next()) != null) {
				queryResult.append(value.asString() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			queryResult =queryResult.append("Failed to execute query");
		}
		
		System.out.println("Query Finished");				
		submittedQuery.setQueryResult(queryResult.toString());
	}
}
