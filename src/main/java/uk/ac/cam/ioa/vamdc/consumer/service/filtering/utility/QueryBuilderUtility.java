package uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility;

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

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.model.SelectedReturnable;

public class QueryBuilderUtility {

	String flworQuery = "";
	
	String flworQueryHtml = "";
	
	

	public String getFlworQueryHtml() {
		return flworQueryHtml;
	}

	public void setFlworQueryHtml(String flworQueryHtml) {
		this.flworQueryHtml = flworQueryHtml;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildQuery(List<SelectedReturnable> selectedReturnablesList,
			String databaseName) {

		String flworQuery = "";
		boolean atomsRestrictable = false;
		boolean processRestrictables = false;

		for (int xi = 0; xi < selectedReturnablesList.size(); xi++) {
			System.out.print(selectedReturnablesList.get(xi).getColumnOrder()
					+ ", " + selectedReturnablesList.get(xi).getColumnName()
					+ "\t");
		}
		System.out.println();
		Collections.sort(selectedReturnablesList, new Comparator() {
			public int compare(Object o1, Object o2) {
				SelectedReturnable sr1 = (SelectedReturnable) o1;
				SelectedReturnable sr2 = (SelectedReturnable) o2;
				return new Integer(sr1.getColumnOrder()).compareTo(new Integer(
						sr2.getColumnOrder()));
			}
		});

		for (int xi = 0; xi < selectedReturnablesList.size(); xi++) {
			System.out.print(selectedReturnablesList.get(xi).getColumnOrder()
					+ ", " + selectedReturnablesList.get(xi).getColumnName()
					+ "\t");
		}
		System.out.println();

		ArrayList<String> returnables = new ArrayList<String>();
		ArrayList<String> queries = new ArrayList<String>();
		
		Set<String> columnNames = new HashSet<String>();
		
		//headers are not used
		//ArrayList<String> headers = new ArrayList<String>();

		// xQueryHashMap is also not used
		//HashMap<String, String> xQueryHashMap = new HashMap<String, String>();

		Iterator<SelectedReturnable> iterator = selectedReturnablesList
				.iterator();
		while (iterator.hasNext()) {
			SelectedReturnable tempReturnable = iterator.next();
			if (!tempReturnable.isRemoved()) {
				if (tempReturnable.getxQueryMapping() != null)
					if (tempReturnable.getxQueryMapping().trim().length() > 0) {
						queries.add(tempReturnable.getxQueryMapping());
						returnables.add(tempReturnable.getName());
						//headers.add(tempReturnable.getColumnName());
						columnNames.add(tempReturnable.getName());
						/*
						xQueryHashMap.put(tempReturnable.getName(),
								tempReturnable.getxQueryMapping()); */
						System.out.println(tempReturnable.getName() + " "
								+ tempReturnable.getColumnName() + " "
								+ tempReturnable.isRemoved());
					}
			}
		}

		atomsRestrictable = isReturnableSelected(columnNames, "Atom*");
		processRestrictables = isReturnableSelected(columnNames, "RadTrans*");

		if (atomsRestrictable && !processRestrictables) {
			flworQuery = atomOnlyQuery(queries, databaseName);
		} else if ((!atomsRestrictable && processRestrictables)
				|| (atomsRestrictable && processRestrictables)) {

			boolean upperRef = isReturnableSelected(columnNames,
					"RadTransInitialStateRef");
			boolean lowerRef = isReturnableSelected(columnNames,
					"RadTransFinalStateRef");

			if (upperRef && lowerRef) {
				System.out.println("both upperRef && lowerRef");
				flworQuery = atomRadiativeQueryBoth(
						/* xQueryHashMap */returnables, queries, databaseName);
			} else if (upperRef && !lowerRef) {
				System.out.println("only upperRef");
				flworQuery = atomRadiativeQuery(/* xQueryHashMap */returnables,
						queries, databaseName, upperRef);
			} else if (!upperRef && lowerRef) {
				System.out.println("only lowerRef");
				flworQuery = atomRadiativeQuery(/* xQueryHashMap */returnables,
						queries, databaseName, upperRef);
			} else if (!upperRef && !lowerRef) {
				System.out.println("neither upperRef nor lowerRef");
				flworQuery = atomRadiativeQuery(/* xQueryHashMap */returnables,
						queries, databaseName, upperRef);
			}
		}

		System.out.println(flworQuery);

		return flworQuery;
	}

	// valuesArray is the XQuery Mapping part 
	// let $Kappa \:\= $xsams/Isotope/Ion/AtomicState[@stateID\=$stateID]/AtomicQuantumNumbers/Kappa \n
	public String atomOnlyQuery(ArrayList<String> valuesArray,
			String databaseName) {
		//java.util.Map<String, Integer> temList = new HashMap<String, Integer>();

		ArrayList<String> returnVar = new ArrayList<String>();
		flworQuery = "";

		flworQuery = flworQuery + " for $xsams in collection('" + databaseName
				+ "')/XSAMSData/Species/Atoms/Atom \n";
		flworQuery = flworQuery
				+ " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";

		for (int i = 0; i < valuesArray.size(); i++) {
			flworQuery = flworQuery + valuesArray.get(i);
			
			// index of $
			int index = valuesArray.get(i).indexOf("$");
			// index of first space after $
			int index2 = valuesArray.get(i).indexOf(" ", index);

			// returns $Kappa
			returnVar.add(valuesArray.get(i).substring(index, index2));

			//System.out.println(valuesArray.get(i).substring(index, index2));

		}
/*
		String returnStatement = "return concat(";

		for (int i = 0; i < returnVar.length; i++) {
			if (i == returnVar.length - 1) {
				returnStatement = returnStatement + returnVar[i];
			} else {
				returnStatement = returnStatement + returnVar[i] + ", \",\", ";
			}
		}

		returnStatement = returnStatement + ")";
		flworQuery = flworQuery + returnStatement;
		*/
		finalizeFlworQuery(returnVar);

		return flworQuery;

	}
	
	private void finalizeFlworQuery(ArrayList<String> returnVar){
		// should be called before finalizing the flworQuery
		finalizeFlworQueryHTML(returnVar);
		
		String returnStatement = "return concat(";
		
		for (int i = 0; i < returnVar.size(); i++) {
			if (i == returnVar.size() - 1) {
				returnStatement = returnStatement + returnVar.get(i);
			} else {
				returnStatement = returnStatement + returnVar.get(i)
						+ ", \",\", ";
			}
		}

		returnStatement = returnStatement + ")";
		flworQuery = flworQuery + returnStatement;
		
	}
	
	private void finalizeFlworQueryHTML(ArrayList<String> returnVar){
		flworQueryHtml = flworQuery;
		String returnStatement = "return concat(";

		for (int i = 0; i < returnVar.size(); i++) {
			if (i == returnVar.size() - 1) {
				returnStatement = returnStatement + returnVar.get(i);
			} else {
				returnStatement = returnStatement + returnVar.get(i)
						+ ", \",\", ";
			}
		}

		returnStatement = returnStatement + ")";
		flworQueryHtml = flworQueryHtml + returnStatement;
		
	}

	private void parseFlworQuery(String flworQueryValue,
			ArrayList<String> forQuery, ArrayList<String> letQuery,
			ArrayList<String> returnVar) {

		if (flworQueryValue.trim().startsWith("for")) {
			forQuery.add(flworQueryValue);
		} else if (flworQueryValue.trim().startsWith("let")) {
			letQuery.add(flworQueryValue);
		}

		int index = flworQueryValue.toString().indexOf("$");
		int index2 = flworQueryValue.toString().indexOf(" ", index);

		returnVar.add(flworQueryValue.toString().substring(index, index2));

	}

	public String atomRadiativeQuery(/*
									 * HashMap<String, String>
									 * XQueryMappingArray
									 */ArrayList<String> returnables,
			ArrayList<String> queries, String databaseName, boolean upperState) {

		Object[] keysArray = /* XQueryMappingArray.keySet() */returnables
				.toArray();
		Object[] valuesArray = /* XQueryMappingArray.values() */queries
				.toArray();

		for (int i = 0; i < keysArray.length; i++) {
			System.out.println(keysArray[i] + "\t" + valuesArray[i]);
		}
		ArrayList<String> returnVar = new ArrayList<String>();

		ArrayList<String> forQuery = new ArrayList<String>();
		ArrayList<String> letQuery = new ArrayList<String>();

		flworQuery = "";

		flworQuery = flworQuery + " for $xsams in collection('" + databaseName
				+ "')/XSAMSData/Species/Atoms/Atom \n";
		flworQuery = flworQuery + " for $radTrans in collection('"
				+ databaseName + "')/XSAMSData/Processes \n";

		flworQuery = flworQuery
				+ " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";

		if (upperState) {
			flworQuery = flworQuery
					+ " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";

			for (int i = 0; i < keysArray.length; i++) {
				String tempReturnable = keysArray[i].toString();

				if (tempReturnable.startsWith("Atom")) {
					String flworQueryValue = (String) valuesArray[i].toString()
					/*
					 * XQueryMappingArray .get(tempReturnable)
					 */;

					parseFlworQuery(flworQueryValue, forQuery, letQuery,
							returnVar);

				} else if (tempReturnable.startsWith("RadTrans")) {

					if (tempReturnable
							.equalsIgnoreCase("RadTransFinalStateRef")) {
						// do nothing
					} else {
						// HashMap valuesArray order can be different from
						// keysArray order
						// best thing is to use
						// XQueryMappingArray.get(tempReturnable)
						// which due to some reasons is not working
						// not applicable to ArrayList which retains the order

						String flworQueryValue = valuesArray[i].toString(); // XQueryMappingArray.get(tempReturnable);

						// This to " let $speciesID := $xsams/Isotope/Ion/@speciesID "
						// turned into " let $speciesID := $upperStateRef/*/@speciesID "
						String tempUpdatedQuery = getUpdatedRadTransQuery(
								flworQueryValue, " $upperStateRef/*");
						
						parseFlworQuery(tempUpdatedQuery, forQuery, letQuery,
								returnVar);
					}
				}
			}
		} else if (!upperState) {
			flworQuery = flworQuery
					+ " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n";

			for (int i = 0; i < keysArray.length; i++) {
				String tempReturnable = keysArray[i].toString();

				if (tempReturnable.startsWith("Atom")) {
					String flworQueryValue = (String) valuesArray[i].toString();

					parseFlworQuery(flworQueryValue, forQuery, letQuery,
							returnVar);

				} else if (tempReturnable.startsWith("RadTrans")) {
					if (tempReturnable
							.equalsIgnoreCase("RadTransInitialStateRef")) {
						// do nothing
					} else {
						String flworQueryValue = valuesArray[i].toString(); 					
						
						// This is to " let $speciesID := $xsams/Isotope/Ion/@speciesID "
						// turned into " let $speciesID := $lowerStateRef/*/@speciesID "
						String tempUpdatedQuery = getUpdatedRadTransQuery(
								flworQueryValue, " $lowerStateRef/*");
						
						parseFlworQuery(tempUpdatedQuery, forQuery, letQuery,
								returnVar);
					}

				}
			}
		}

		Object[] elements = forQuery.toArray();

		for (int i = 0; i < elements.length; i++) {
			flworQuery = flworQuery + elements[i].toString();
		}

		elements = letQuery.toArray();

		for (int i = 0; i < elements.length; i++) {
			flworQuery = flworQuery + elements[i].toString();
		}

		flworQuery = flworQuery
				+ " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n";

		// System.out.println("*****  " + flworQuery);

		/*
		String returnStatement = "return concat(";

		// "return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";

		for (int i = 0; i < returnVar.size(); i++) {
			if (i == returnVar.size() - 1) {
				returnStatement = returnStatement + returnVar.get(i);
			} else {
				returnStatement = returnStatement + returnVar.get(i)
						+ ", \",\", ";
			}
		}

		returnStatement = returnStatement + ")";
		flworQuery = flworQuery + returnStatement;
	*/
		finalizeFlworQuery(returnVar);
		return flworQuery;
	}

	public String atomRadiativeQueryBoth(/*
										 * HashMap<String, String>
										 * XQueryMappingArray
										 */ArrayList<String> returnables,
			ArrayList<String> queries, String databaseName) {

		Object[] keysArray = /* XQueryMappingArray.keySet() */returnables
				.toArray();
		Object[] valuesArray = /* XQueryMappingArray.values() */queries
				.toArray();

		for (int i = 0; i < keysArray.length; i++) {
			System.out.print(keysArray[i] + "\t");
		}

		ArrayList<String> returnVar = new ArrayList<String>();

		ArrayList<String> forQuery = new ArrayList<String>();
		ArrayList<String> letQuery = new ArrayList<String>();

		flworQuery = "";

		flworQuery = flworQuery + " for $xsams in collection('" + databaseName
				+ "')/XSAMSData/Species \n";
		
		flworQuery = flworQuery + " for $radTrans in collection('"
				+ databaseName
				+ "')/XSAMSData/Processes/Radiative/RadiativeTransition \n";

		flworQuery = flworQuery + " for $radTransID in $radTrans/@id \n";
		flworQuery = flworQuery + " for $atoms in $xsams/Atoms/Atom \n";

		letQuery.add(" let $lowerStateRef := $radTrans/LowerStateRef \n");
		letQuery.add(" let $upperStateRef := $radTrans/UpperStateRef \n");

		letQuery.add(" let $lowerAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef] \n");
		letQuery.add(" let $upperAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef] \n");

		for (int i = 0; i < keysArray.length; i++) {
			String tempReturnable = keysArray[i].toString();

			if (tempReturnable.startsWith("Atom")) {
				String flworQueryValue = (String) valuesArray[i].toString()
				/*
				 * XQueryMappingArray .get(tempReturnable)
				 */;
				if (tempReturnable.startsWith("AtomState")) {

					
					// let $upperAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef] 
					String flworQueryValueUpper = flworQueryValue.replaceAll(
							"\\$stateID", "\\$upperStateRef");
					
					// let $lowerAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef] 
					String flworQueryValueLower = flworQueryValue.replaceAll(
							"\\$stateID", "\\$lowerStateRef");

					// let $StateEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value 
					// let $StateEnergy := $upperAtomicState/AtomicNumericalData/StateEnergy/Value 		
					String flworQueryValueUpperUpdated = getUpdatedAtomQuery(
							flworQueryValueUpper, "$upperAtomicState");
					
					// let $StateEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value 
					// let $StateEnergy := $lowerAtomicState/AtomicNumericalData/StateEnergy/Value 
					String flworQueryValueLowerUpdated = getUpdatedAtomQuery(
							flworQueryValueLower, "$lowerAtomicState");

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					String tempReturnableVar = flworQueryValue.substring(index,
							index2);

					// let $StateEnergy := $upperAtomicState/AtomicNumericalData/StateEnergy/Value 
					// let $StateEnergyUpper := $upperAtomicState/AtomicNumericalData/StateEnergy/Value
					flworQueryValueUpperUpdated = flworQueryValueUpperUpdated
							.replaceAll("\\" + tempReturnableVar, "\\"
									+ tempReturnableVar + "Upper");
					

					// let $StateEnergy := $lowerAtomicState/AtomicNumericalData/StateEnergy/Value
					// let $StateEnergyLower := $lowerAtomicState/AtomicNumericalData/StateEnergy/Value
					flworQueryValueLowerUpdated = flworQueryValueLowerUpdated
							.replaceAll("\\" + tempReturnableVar, "\\"
									+ tempReturnableVar + "Lower");

					if (flworQueryValueUpperUpdated.trim().startsWith("for")) {
						System.out.println("for: "
								+ flworQueryValueUpperUpdated);
						forQuery.add(flworQueryValueUpperUpdated);
					} else if (flworQueryValueUpperUpdated.trim().startsWith(
							"let")) {
						System.out.println("let: "
								+ flworQueryValueUpperUpdated);
						letQuery.add(flworQueryValueUpperUpdated);

					}

					if (flworQueryValueLowerUpdated.trim().startsWith("for")) {
						System.out.println("for: "
								+ flworQueryValueLowerUpdated);
						forQuery.add(flworQueryValueLowerUpdated);
					} else if (flworQueryValueLowerUpdated.trim().startsWith(
							"let")) {
						System.out.println("let: "
								+ flworQueryValueLowerUpdated);
						letQuery.add(flworQueryValueLowerUpdated);

					}

					returnVar.add(tempReturnableVar + "Upper");
					returnVar.add(tempReturnableVar + "Lower");
				} else {
					String flworQueryValueUpdated = "";
					if (tempReturnable.endsWith("")) {
						System.out.println(flworQueryValueUpdated);
						flworQueryValueUpdated = getUpdatedRadTransQuery(
								flworQueryValue, "$atoms/*");
						System.out.println(flworQueryValueUpdated);
					} else if (tempReturnable.endsWith("")) {

					}
					if (tempReturnable.endsWith("")) {

					}
					
					parseFlworQuery(flworQueryValueUpdated, forQuery, letQuery,
							returnVar);
				}
			} else if (tempReturnable.startsWith("RadTrans")) {
				if (tempReturnable.equalsIgnoreCase("RadTransInitialStateRef")) {
					// do nothing
				} else if (tempReturnable
						.equalsIgnoreCase("RadTransFinalStateRef")) {
					// do nothing
				} else {
					String flworQueryValue = valuesArray[i].toString(); // XQueryMappingArray.get(tempReturnable);
					// System.out.print(tempReturnable + " " );
					// System.out.println(flworQueryValue);

					String tempUpdatedQuery = getUpdatedRadTransQuery(
							flworQueryValue, " $radTrans/*");
					
					parseFlworQuery(tempUpdatedQuery, forQuery, letQuery,
							returnVar);
				}
			}

		}

		Object[] elements = forQuery.toArray();

		for (int i = 0; i < elements.length; i++) {
			flworQuery = flworQuery + elements[i].toString();
		}

		elements = letQuery.toArray();

		for (int i = 0; i < elements.length; i++) {
			flworQuery = flworQuery + elements[i].toString();
		}

		flworQuery = flworQuery
				+ " where $radTrans/@id=$radTransID and $atoms/Isotope/Ion/AtomicState/@stateID=$lowerStateRef \n";

		// System.out.println("*****  " + flworQuery);

		/*
		String returnStatement = "return concat(";

		// "return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";

		for (int i = 0; i < returnVar.size(); i++) {
			if (i == returnVar.size() - 1) {
				returnStatement = returnStatement + returnVar.get(i)
						+ ", \"\t\" ";
			} else {
				returnStatement = returnStatement + returnVar.get(i)
						+ ", \"\t\", ";
			}
		}

		returnStatement = returnStatement + ")";
		flworQuery = flworQuery + returnStatement;
		*/
		
		finalizeFlworQuery(returnVar);
		
		return flworQuery;
	}

	private String getUpdatedRadTransQuery(String originalValue,
			String replacementValue) {
		// System.out.append(counter++ + "  " + originalValue);
		String updatedValue = "";

		if (originalValue != null) {

			// Find the index of $
			// " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			int index = originalValue.toString().indexOf("$");

			// Find the index of first "space" after index of $
			// " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			int index2 = originalValue.toString().indexOf(" ", index);

			// This will return speciesID from
			// " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			// index + 1 will not select the $ sign
			String tempQueryVariable = originalValue.toString().substring(
					index + 1, index2);

			// Starting from the end of query to find the last Reference of
			// "speciesID"
			// XQuery mapping is done in the way that returnable is used twice
			// to make the overall
			// logic simpler.
			// " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			int tempQueryVariableIndex = originalValue.lastIndexOf(
					tempQueryVariable, originalValue.length());

			// tempQueryVariableIndex -1 will include one character just before
			// the returnable variable i.e. @speciesID for
			// " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			// In few cases it will be /xxxx
			String partAfterReplacement = originalValue
					.substring(tempQueryVariableIndex - 1);

			// This to " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			// turned into " let $speciesID := $atoms/*/@speciesID "
			// rather than " let $speciesID := $atoms/*@speciesID "
			if (partAfterReplacement.startsWith("@")) {
				partAfterReplacement = "/" + partAfterReplacement;
			}

			// " let $speciesID := $xsams/Isotope/Ion/@speciesID " will be
			// split into 2 parts 
			// "let $speciesID := " Note use of index2 + 4
			// index2 is the first space after $ sign
			// and /@speciesID
			// replacement value is inserted between two splits
			// This to " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			// turned into " let $speciesID := $atoms/*/@speciesID "
			updatedValue = originalValue.substring(0, index2 + 4)
					+ replacementValue + partAfterReplacement;
		}

		return updatedValue;
	}

	private boolean isReturnableSelected(Set<String> keys, String returnable) {

		Pattern pattern = Pattern.compile(returnable, Pattern.CASE_INSENSITIVE);

		Iterator<String> ite = keys.iterator();

		while (ite.hasNext()) {
			String candidate = ite.next();
			// System.out.println(candidate);
			Matcher m = pattern.matcher(candidate);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	private String getUpdatedAtomQuery(String originalValue,
			String replacementValue) {
		String updatedValue = "";

		if (originalValue != null) {
			
			// Find the index of $
			int index = originalValue.toString().indexOf("$");
			
			// Find the index of first "space" after index of $
			int index2 = originalValue.toString().indexOf(" ", index);

			// Starting from the end of query to find the last Reference
			int tempQueryVariableIndex = originalValue.lastIndexOf("]",
					originalValue.length());

			// let $StateEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value
			// let $StateEnergy := $lowerAtomicState/AtomicNumericalData/StateEnergy/Value
			// originalValue.substring(0, index2 + 4) == let $StateEnergy :=
			// replacementValue ==  $lowerAtomicState
			// originalValue.substring(tempQueryVariableIndex + 1) == /AtomicNumericalData/StateEnergy/Value
			updatedValue = originalValue.substring(0, index2 + 4)
					+ replacementValue
					+ originalValue.substring(tempQueryVariableIndex + 1);
		}
		return updatedValue;
	}

}
