package uk.ac.cam.ioa.vamdc.consumer.service.filtering.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class URLController {
	
	
	private String homePage = "index.jsf" + getSession();
	private String queryBuilder = "queryBuilderForm1.jsf" + getSession();
	private String submittedQuery = "submittedQueries.jsf" + getSession();
	private String queryForm = "queryFilter.jsf" + getSession();
	
	
	
	public String getHomePage() {
		return homePage;
	}



	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}



	public String getQueryBuilder() {
		return queryBuilder;
	}



	public void setQueryBuilder(String queryBuilder) {
		this.queryBuilder = queryBuilder;
	}



	public String getSubmittedQuery() {
		return submittedQuery;
	}



	public void setSubmittedQuery(String submittedQuery) {
		this.submittedQuery = submittedQuery;
	}



	public String getQueryForm() {
		return queryForm;
	}



	public void setQueryForm(String queryForm) {
		this.queryForm = queryForm;
	}



	private String getSession(){
		FacesContext fc = FacesContext.getCurrentInstance();
/*
		HttpServletRequest origRequest = (HttpServletRequest) fc
				.getExternalContext().getRequest();*/
		
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
				false);
		String sessionId = session.getId();
		
		sessionId = ";jsessionid=" + sessionId;
		
		return sessionId;
	}

}
