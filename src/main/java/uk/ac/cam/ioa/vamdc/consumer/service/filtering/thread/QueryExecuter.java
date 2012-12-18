package uk.ac.cam.ioa.vamdc.consumer.service.filtering.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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

	public void run() {
		if (submittedQuery.getQuery() != null) {
			xmlDatabase.doQuery(submittedQuery);
		}
		File file;
		file = new File("/opt/jboss/VAMDCData/csv/"
				+ submittedQuery.getQueryID() + ".csv");

		int fileSize = writeCSV(submittedQuery.getQueryResult(), file);

		submittedQuery.setCsvFileSize(fileSize);

		boolean fileNotexist = true;
		while (fileNotexist) {
			if (!file.exists()) {
				continue;
			} else {
				submittedQuery.setQueryStatus(true);
			}
			fileNotexist = false;
		}
		
		writeHTML(submittedQuery.getQueryID());
	}

	public int writeCSV(String queryResult, File file) {
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

	private void writeHTML(String fileName) {
		StringBuilder content = new StringBuilder();
		content.append("<html><head></head><body><table border=\"1\"> \n");

		FileOutputStream fop = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"/opt/jboss/VAMDCData/csv/" + fileName + ".csv"));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.trim().length() > 0) {
					str = "<tr><td>" + str.replaceAll(",", "</td><td>")
							+ "</td></tr> \n";
					content.append(str + "\n");
				} else {
					// do nothing
				}
			}
			content.append("</table></body></html>");

			File file;
			file = new File("/opt/jboss/VAMDCData/csv/" + fileName + ".html");

			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}

			byte[] contentInBytes = content.toString().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

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

	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) private String
	 * getCSVHeaders(){
	 * 
	 * List<SelectedReturnable> selectedReturnablesList =
	 * submittedQuery.getSelectedReturnablesList(); StringBuilder headers = new
	 * StringBuilder();
	 * 
	 * Collections.sort(selectedReturnablesList, new Comparator() { public int
	 * compare(Object o1, Object o2) { SelectedReturnable sr1 =
	 * (SelectedReturnable) o1; SelectedReturnable sr2 = (SelectedReturnable)
	 * o2; return new Integer(sr1.getColumnOrder()).compareTo(new Integer(
	 * sr2.getColumnOrder())); } });
	 * 
	 * Iterator<SelectedReturnable> iterator = selectedReturnablesList
	 * .iterator(); while (iterator.hasNext()) { SelectedReturnable
	 * tempReturnable = iterator.next(); if (!tempReturnable.isRemoved()) { if
	 * (tempReturnable.getxQueryMapping() != null) if
	 * (tempReturnable.getxQueryMapping().trim().length() > 0) {
	 * 
	 * if(iterator.hasNext()){ headers.append(tempReturnable.getColumnName() +
	 * ","); } else { headers.append(tempReturnable.getColumnName()); }
	 * 
	 * } } } return headers.toString(); }
	 */
}
