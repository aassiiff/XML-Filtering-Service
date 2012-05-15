package uk.ac.cam.ioa.vamdc.consumer.dummy.data;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@Named
@SessionScoped
public class TestController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8592698487585075373L;

	/*
	 * @Inject private HttpSession session;
	 */

	private String sessionId;

	@Inject
	@DataHolder
	private List<MyData> localDataHolder;

	public TestController() {
		System.out.println("TestController constructor called");
		/**/
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc != null) {
			HttpSession session = (HttpSession) fc.getExternalContext()
					.getSession(true);
			if (session != null) {
			sessionId = session.getId();
			System.out.println("Session ID: " + sessionId);
			}
		}

	}

	@Named
	public List<MyData> getLocalDataHolder() {
		return localDataHolder;
	}

	public void setLocalDataHolder(List<MyData> localDataHolder) {
		this.localDataHolder = localDataHolder;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void doNothing() {
		 FacesContext fc = FacesContext.getCurrentInstance();
		 if (fc != null) {
				HttpSession session = (HttpSession) fc.getExternalContext()
						.getSession(true);
				if (session != null) {
				sessionId = session.getId();
				System.out.println("Session ID: " + sessionId);
				}
			}

		System.out.println("doNothing() " + localDataHolder.size());
	}

	public void addData() {

		System.out.println(localDataHolder.size());

		MyData tempData = new MyData();

		tempData.setUrl1("Another URL 1");
		tempData.setUrl2("Another URL 2");
		tempData.setUrl3("ANother URL 3");

		localDataHolder.add(tempData);

		System.out.println(localDataHolder.size());
	}
	
	public void addData(String data1, String data2, String data3){
		System.out.println(localDataHolder.size());

		MyData tempData = new MyData();

		tempData.setUrl1(data1);
		tempData.setUrl2(data2);
		tempData.setUrl3(data3);

		localDataHolder.add(tempData);

		System.out.println(localDataHolder.size());
	}

}