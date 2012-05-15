package uk.ac.cam.ioa.vamdc.consumer.service.filtering.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.Returnable;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.AtomReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.EnvironmentReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.MoleculeReturnableList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.ProcessReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.SolidReturnablesList;
import uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.ReturnableXQueryMapping;

@SessionScoped
@Named
public class ReturnableProducer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7275493583998452748L;

	private List<Returnable> returnables;

	private boolean firstAttempt = false;

	public ReturnableProducer() {
	}

	@Inject
	@Category("vamdc-xml-db-consumer-service")
	private Logger log;

	@Produces
	@Named
	public List<Returnable> getReturnables() {
		if (returnables == null) {
			sqliteQuery();
		}
		//
		return returnables;
	}

	// @PostConstruct
	public void retrieveAllReturnables() {
		if (firstAttempt == false) {
			sqliteQuery();
			firstAttempt = !firstAttempt;
		}
	}

	private synchronized void sqliteQuery() {
		ReturnableXQueryMapping mappingProperties = new ReturnableXQueryMapping();

		System.out.println(" @@@@@@@@@@@   sqliteQuery ");
		returnables = new ArrayList<Returnable>();
		// atomReturnablesList = new ArrayList<Returnable>();
		Returnable tempReturnable;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/opt/jboss/dictionary/dict.sqlite");
			Statement stat = conn.createStatement();

			ResultSet rs = stat
					.executeQuery("select bk.name, bk.sdescr, bk.ldescr, bk.type, bk.unit from keyword as bk order by bk.name;");
			while (rs.next()) {
				String tempUnit = rs.getString("unit");
				tempReturnable = new Returnable(rs.getString("name"),
						rs.getString("sdescr"), rs.getString("ldescr"),
						rs.getString("type"), rs.getString("unit"));
				if (tempUnit != null) {
					if (tempUnit.trim().length() > 0) {
						tempReturnable.setHasUnit(true);
					}
				}
				String xqueryValue = mappingProperties
						.getPropertyValue(tempReturnable.getName());
				if (xqueryValue != null) {
					tempReturnable.setxQueryMapping(xqueryValue);
					//log.info(tempReturnable.getxQueryMapping());
					returnables.add(tempReturnable);
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
