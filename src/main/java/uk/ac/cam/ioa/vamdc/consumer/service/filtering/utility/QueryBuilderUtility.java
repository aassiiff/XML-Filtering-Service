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

		ArrayList<String> queries = new ArrayList<String>();
		ArrayList<String> headers = new ArrayList<String>();

		HashMap<String, String> xQueryHashMap = new HashMap<String, String>();

		Set<String> columnNames = new HashSet<String>();
		Iterator<SelectedReturnable> iterator = selectedReturnablesList
				.iterator();
		while (iterator.hasNext()) {
			SelectedReturnable tempReturnable = iterator.next();
			if (!tempReturnable.isRemoved()) {
				if (tempReturnable.getxQueryMapping() != null)
					if (tempReturnable.getxQueryMapping().trim().length() > 0) {
						queries.add(tempReturnable.getxQueryMapping());
						headers.add(tempReturnable.getColumnName());
						columnNames.add(tempReturnable.getName());
						xQueryHashMap.put(tempReturnable.getName(),
								tempReturnable.getxQueryMapping());
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
				flworQuery = atomRadiativeQueryBoth(xQueryHashMap, databaseName);
			} else if (upperRef && !lowerRef) {
				System.out.println("only upperRef");
				flworQuery = atomRadiativeQuery(xQueryHashMap, databaseName,
						upperRef);
			} else if (!upperRef && lowerRef) {
				System.out.println("only lowerRef");
				flworQuery = atomRadiativeQuery(xQueryHashMap, databaseName,
						upperRef);
			} else if (!upperRef && !lowerRef) {
				System.out.println("neither upperRef nor lowerRef");
				flworQuery = atomRadiativeQuery(xQueryHashMap, databaseName,
						upperRef);
			}
		}

		System.out.println(flworQuery);

		return flworQuery;
	}

	public String atomOnlyQuery(ArrayList<String> valuesArray,
			String databaseName) {
		String returnVar[] = new String[valuesArray.size()];
		flworQuery = "";

		flworQuery = flworQuery + " for $xsams in collection('" + databaseName
				+ "')/XSAMSData/Species/Atoms/Atom \n";
		flworQuery = flworQuery
				+ " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";

		for (int i = 0; i < valuesArray.size(); i++) {
			flworQuery = flworQuery + valuesArray.get(i);
			int index = valuesArray.get(i).indexOf("$");
			int index2 = valuesArray.get(i).indexOf(" ", index);

			returnVar[i] = valuesArray.get(i).substring(index, index2);

			System.out.println(valuesArray.get(i).substring(index, index2));

		}

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

		return flworQuery;

	}

	public String atomRadiativeQuery(
			HashMap<String, String> XQueryMappingArray, String databaseName,
			boolean upperState) {

		Object[] keysArray = XQueryMappingArray.keySet().toArray();
		Object[] valuesArray = XQueryMappingArray.values().toArray();

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
			/*
			 * System.out.print(keysArray.length + " "); if
			 * (XQueryMappingArray.containsKey("RadTransFinalStateRef")) {
			 * XQueryMappingArray.remove("RadTransFinalStateRef"); }
			 * System.out.println(keysArray.length + " ");
			 */
			for (int i = 0; i < keysArray.length; i++) {
				String tempReturnable = keysArray[i].toString();

				if (tempReturnable.startsWith("Atom")) {
					String flworQueryValue = (String) XQueryMappingArray
							.get(tempReturnable);

					if (flworQueryValue.trim().startsWith("for")) {
						// System.out.println("for: " + flworQueryValue);
						forQuery.add(flworQueryValue);
					} else if (flworQueryValue.trim().startsWith("let")) {
						// System.out.println("let: " + flworQueryValue);
						letQuery.add(flworQueryValue);
					}

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					returnVar.add(flworQueryValue.toString().substring(index,
							index2));
				} else if (tempReturnable.startsWith("RadTrans")) {

					if (tempReturnable
							.equalsIgnoreCase("RadTransFinalStateRef")) {
						// do nothing
					} else {
						String flworQueryValue = valuesArray[i].toString(); // XQueryMappingArray.get(tempReturnable);

						// System.out.print(tempReturnable + " " +
						// XQueryMappingArray.containsKey(tempReturnable) + " "
						// );
						// System.out.println(flworQueryValue);

						String tempUpdatedQuery = getUpdatedRadTransQuery(
								flworQueryValue, " $upperStateRef/*");
						if (tempUpdatedQuery.trim().startsWith("for")) {
							// System.out.println("for: " + tempUpdatedQuery);
							forQuery.add(tempUpdatedQuery);
						} else if (tempUpdatedQuery.trim().startsWith("let")) {
							// System.out.println("let: " + tempUpdatedQuery);
							letQuery.add(tempUpdatedQuery);

						}
						// flworQuery = flworQuery +
						// getUpdatedRadTransQuery(flworQueryValue,
						// " $upperStateRef/*");

						int index = flworQueryValue.toString().indexOf("$");
						int index2 = flworQueryValue.toString().indexOf(" ",
								index);

						returnVar.add(flworQueryValue.toString().substring(
								index, index2));
					}
				}
			}

		} else if (!upperState) {
			flworQuery = flworQuery
					+ " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n";
			/*
			 * System.out.print(keysArray.length + " "); if
			 * (XQueryMappingArray.containsKey("RadTransInitialStateRef")) {
			 * XQueryMappingArray.remove("RadTransInitialStateRef");
			 * System.out.println(keysArray.length + " "); }
			 */

			for (int i = 0; i < keysArray.length; i++) {
				String tempReturnable = keysArray[i].toString();

				if (tempReturnable.startsWith("Atom")) {
					String flworQueryValue = (String) XQueryMappingArray
							.get(tempReturnable);

					if (flworQueryValue.trim().startsWith("for")) {
						System.out.println("for: " + flworQueryValue);
						forQuery.add(flworQueryValue);
					} else if (flworQueryValue.trim().startsWith("let")) {
						System.out.println("let: " + flworQueryValue);
						letQuery.add(flworQueryValue);

					}

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					returnVar.add(flworQueryValue.toString().substring(index,
							index2));
				} else if (tempReturnable.startsWith("RadTrans")) {
					if (tempReturnable
							.equalsIgnoreCase("RadTransInitialStateRef")) {
						// do nothing
					} else {
						String flworQueryValue = valuesArray[i].toString(); // XQueryMappingArray.get(tempReturnable);
						// System.out.print(tempReturnable + " " );
						// System.out.println(flworQueryValue);

						String tempUpdatedQuery = getUpdatedRadTransQuery(
								flworQueryValue, " $lowerStateRef/*");

						if (tempUpdatedQuery.trim().startsWith("for")) {
							forQuery.add(tempUpdatedQuery);
						} else if (tempUpdatedQuery.trim().startsWith("let")) {
							letQuery.add(tempUpdatedQuery);
						}

						// flworQuery = flworQuery +
						// getUpdatedRadTransQuery(flworQueryValue,
						// " $lowerStateRef/*");

						int index = flworQueryValue.toString().indexOf("$");
						int index2 = flworQueryValue.toString().indexOf(" ",
								index);

						returnVar.add(flworQueryValue.toString().substring(
								index, index2));
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

		return flworQuery;
	}

	public String atomRadiativeQueryBoth(
			HashMap<String, String> XQueryMappingArray, String databaseName) {
		Object[] keysArray = XQueryMappingArray.keySet().toArray();
		Object[] valuesArray = XQueryMappingArray.values().toArray();

		ArrayList<String> returnVar = new ArrayList<String>();

		ArrayList<String> forQuery = new ArrayList<String>();
		ArrayList<String> letQuery = new ArrayList<String>();

		flworQuery = "";

		flworQuery = flworQuery + " for $xsams in collection('" + databaseName
				+ "')/XSAMSData/Species \n";
		flworQuery = flworQuery + " for $radTrans in collection('"
				+ databaseName
				+ "')/XSAMSData/Processes/Radiative/RadiativeTransition \n";

		if (XQueryMappingArray.containsKey("RadTransFinalStateRef")) {
			// XQueryMappingArray.remove("RadTransFinalStateRef");
		}

		if (XQueryMappingArray.containsKey("RadTransInitialStateRef")) {
			// XQueryMappingArray.remove("RadTransInitialStateRef");
		}
		flworQuery = flworQuery + " for $radTransID in $radTrans/@id \n";
		flworQuery = flworQuery + " for $atoms in $xsams/Atoms/Atom \n";

		letQuery.add(" let $lowerStateRef := $radTrans/LowerStateRef \n");
		letQuery.add(" let $upperStateRef := $radTrans/UpperStateRef \n");

		letQuery.add(" let $lowerAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef] \n");
		letQuery.add(" let $upperAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef] \n");

		for (int i = 0; i < keysArray.length; i++) {
			String tempReturnable = keysArray[i].toString();

			if (tempReturnable.startsWith("Atom")) {
				String flworQueryValue = (String) XQueryMappingArray
						.get(tempReturnable);
				if (tempReturnable.startsWith("AtomState")) {

					String flworQueryValueUpper = flworQueryValue.replaceAll(
							"\\$stateID", "\\$upperStateRef");
					String flworQueryValueLower = flworQueryValue.replaceAll(
							"\\$stateID", "\\$lowerStateRef");

					String flworQueryValueUpperUpdated = getUpdatedAtomQueryNew(
							flworQueryValueUpper, "$upperAtomicState");
					String flworQueryValueLowerUpdated = getUpdatedAtomQueryNew(
							flworQueryValueLower, "$lowerAtomicState");

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					String tempReturnableVar = flworQueryValue.substring(index,
							index2);

					System.out.println(tempReturnableVar);

					flworQueryValueUpperUpdated = flworQueryValueUpperUpdated
							.replaceAll("\\" + tempReturnableVar, "\\"
									+ tempReturnableVar + "Upper");
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
						flworQueryValueUpdated = getUpdatedRadTransQuery(
								flworQueryValue, "$atoms/*");
					} else if (tempReturnable.endsWith("")) {

					}
					if (tempReturnable.endsWith("")) {

					}

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					String tempReturnableVar = flworQueryValue.substring(index,
							index2);

					if (flworQueryValueUpdated.trim().startsWith("for")) {
						// System.out.println("for: " + flworQueryValueUpdated);
						forQuery.add(flworQueryValueUpdated);
					} else if (flworQueryValueUpdated.trim().startsWith("let")) {
						// System.out.println("let: " + flworQueryValueUpdated);
						letQuery.add(flworQueryValueUpdated);

					}
					returnVar.add(tempReturnableVar);
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

					if (tempUpdatedQuery.trim().startsWith("for")) {
						forQuery.add(tempUpdatedQuery);
					} else if (tempUpdatedQuery.trim().startsWith("let")) {
						letQuery.add(tempUpdatedQuery);
					}

					// flworQuery = flworQuery +
					// getUpdatedRadTransQuery(flworQueryValue,
					// " $lowerStateRef/*");

					int index = flworQueryValue.toString().indexOf("$");
					int index2 = flworQueryValue.toString().indexOf(" ", index);

					returnVar.add(flworQueryValue.toString().substring(index,
							index2));
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
		return flworQuery;
	}

	private String getUpdatedRadTransQuery(String originalValue,
			String replacementValue) {
		// System.out.append(counter++ + "  " + originalValue);
		String updatedValue = "";

		if (originalValue != null) {

			int index = originalValue.toString().indexOf("$");
			int index2 = originalValue.toString().indexOf(" ", index);

			String tempQueryVariable = originalValue.toString().substring(
					index + 1, index2);

			// int tempQueryVariableIndex =
			// originalValue.lastIndexOf(tempQueryVariable, index2);

			// Starting from the end of query to find the last Reference
			int tempQueryVariableIndex = originalValue.lastIndexOf(
					tempQueryVariable, originalValue.length());

			// This to " let $speciesID := $xsams/Isotope/Ion/@speciesID "
			// turned into " let $speciesID := $atoms/*/@speciesID "
			// rather than " let $speciesID := $atoms/*@speciesID "
			String partAfterReplacement = originalValue
					.substring(tempQueryVariableIndex - 1);

			if (partAfterReplacement.startsWith("@")) {
				partAfterReplacement = "/" + partAfterReplacement;
			}

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

	private String getUpdatedAtomQueryNew(String originalValue,
			String replacementValue) {
		// System.out.append(counter++ + "  " + originalValue);
		String updatedValue = "";

		if (originalValue != null) {

			int index = originalValue.toString().indexOf("$");
			int index2 = originalValue.toString().indexOf(" ", index);

			String tempQueryVariable = originalValue.toString().substring(
					index + 1, index2);

			// int tempQueryVariableIndex =
			// originalValue.lastIndexOf(tempQueryVariable, index2);

			// Starting from the end of query to find the last Reference
			int tempQueryVariableIndex = originalValue.lastIndexOf("]",
					originalValue.length());

			updatedValue = originalValue.substring(0, index2 + 4)
					+ replacementValue
					+ originalValue.substring(tempQueryVariableIndex + 1);
		}

		return updatedValue;
	}
}
