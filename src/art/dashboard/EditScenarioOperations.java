package art.dashboard;

/******************************************************************************************************************
 * Copy rights @ Team ART
 *       
 * Description:
 *		This Class embeds all operations on objects and their attributes
 *           
 * Internal Methods :
 *		
 *
 * Reviewed by : 
 * 
 * Review Comments :
 *
 ******************************************************************************************************************/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.DragDropEvent;

import art.database.DatabaseOperations;
import art.datastructures.BusinessObject;
import art.datastructures.Scenario;

@ManagedBean(value = "editScenarioOperations")
@ViewScoped
public class EditScenarioOperations implements Serializable {
	/**
	 * Added serialversion ID
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> businessObjectsCopy = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> preReqObjects = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> inputObject = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> outputObject = new ArrayList<BusinessObject>();
	private BusinessObject selectedPreReqObject = new BusinessObject();
	private BusinessObject selectedInputObject = new BusinessObject();
	private BusinessObject selectedOutputObject = new BusinessObject();
	private String scenarioName;
	private Scenario selectedScenarioMain;
	private String test;

	public Scenario getSelectedScenarioMain() {
		return selectedScenarioMain;
	}

	public void setSelectedScenarioMain(Scenario selectedScenarioMain) {
		this.selectedScenarioMain = selectedScenarioMain;
	}

	public EditScenarioOperations() {
		selectedScenarioMain = new Scenario();

		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();


		if ((String) sessionMap.get("selectedScenarioName") != null) {
			String scenarioName = (String) sessionMap
					.get("selectedScenarioName");
			System.out.println("FromSession : " + scenarioName);
			this.leftOverOperations((String) sessionMap
					.get("selectedScenarioName"));
		}

	}

	public ArrayList<BusinessObject> getBusinessObjects() {
		return businessObjects;
	}

	public ArrayList<BusinessObject> getPreReqObjects() {
		return preReqObjects;
	}

	public ArrayList<BusinessObject> getInputObjects() {
		return inputObject;
	}

	public ArrayList<BusinessObject> getOutputObjects() {
		return outputObject;
	}

	public BusinessObject getSelectedPreReqObject() {
		return selectedPreReqObject;
	}

	public BusinessObject getSelectedInputObject() {
		return selectedInputObject;
	}

	public BusinessObject getSelectedOutputObject() {
		return selectedOutputObject;
	}

	public void setBusinessObjects(ArrayList<BusinessObject> objects) {
		this.businessObjects = objects;
	}

	public void setPreReqObjects(ArrayList<BusinessObject> objects) {
		this.preReqObjects = objects;
	}

	public ArrayList<BusinessObject> getBusinessObjectsCopy() {
		return businessObjectsCopy;
	}

	public void setBusinessObjectsCopy(
			ArrayList<BusinessObject> businessObjectsCopy) {
		this.businessObjectsCopy = businessObjectsCopy;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void onDragDropPreReq(DragDropEvent event) {
		BusinessObject object = (BusinessObject) event.getData();

		preReqObjects.add(object);
		selectedScenarioMain.setPreReqObjects(preReqObjects);
		businessObjectsCopy.remove(object);

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(object.getObjectName() + " added", "Position:"
						+ event.getDropId()));
	}

	public void onDragDropInput(DragDropEvent event) {
		BusinessObject object = (BusinessObject) event.getData();

		inputObject.add(object);
		selectedScenarioMain.setInputObjects(inputObject);
		businessObjectsCopy.remove(object);

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(object.getObjectName() + " added", "Position:"
						+ event.getDropId()));
	}

	public void onDragDropOutput(DragDropEvent event) {
		BusinessObject object = (BusinessObject) event.getData();

		outputObject.add(object);
		selectedScenarioMain.setOutputObjects(outputObject);
		businessObjectsCopy.remove(object);
		FacesMessage msg = new FacesMessage(object.getObjectName() + " added");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void setSelectedObject(BusinessObject object) {
		this.selectedPreReqObject = object;
	}

	public void reset() {
		businessObjectsCopy.clear();
		businessObjectsCopy.addAll(businessObjects);
		outputObject.clear();
		inputObject.clear();
		preReqObjects.clear();
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String text) {
		this.scenarioName = text;
	}

	public void saveScenario() {
		// remove old information before updating the scenario
		removeScenario();
		// Check if Scenario Name already exists
		ArrayList<String> rScenarioName = new ArrayList<String>();
		String query = "select * from scenarios where scenario_name =" + "'"
				+ scenarioName + "'";
		DatabaseOperations dbOp = new DatabaseOperations();
		rScenarioName = dbOp.executegetColumnQuery(query, 1);
		if (!rScenarioName.isEmpty()) {
			FacesMessage msg = new FacesMessage("Scenario name " + scenarioName
					+ " already exists.Please choose a different name");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			ArrayList<String> rScenarioID = new ArrayList<String>();
			int flag = 0;

			// Insert scenarioName inside scenario Table
			try {
				query = "insert into scenarios(scenario_name) values(" + "'"
						+ scenarioName + "'" + " )";
				System.out.println(query);
				DatabaseOperations dbop = new DatabaseOperations();
				flag = dbop.updateDB(query);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {

				// Select the scenario id for mapping purpose
				query = "select scenarios_id from scenarios "
						+ "WHERE scenario_name='" + scenarioName + "'";

				rScenarioID = new DatabaseOperations().executegetColumnQuery(
						query, 1);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {

				// Insert prereq values into the database
				for (BusinessObject bObject : preReqObjects) {
					query = "insert into scenario_bo_mapping (scenario_id,objectID,objectType) values("
							+ "'"
							+ rScenarioID.get(0)
							+ "'"
							+ ","
							+ "'"
							+ bObject.getObjectID()
							+ "'"
							+ ","
							+ "'"
							+ "Pre-Requisite" + "'" + " )";
					System.out.println(query);
					DatabaseOperations dbop = new DatabaseOperations();
					flag = dbop.updateDB(query);

				}

				// Insert input values into the database
				for (BusinessObject bObject : inputObject) {
					query = "insert into scenario_bo_mapping (scenario_id,objectID,objectType) values("
							+ "'"
							+ rScenarioID.get(0)
							+ "'"
							+ ","
							+ "'"
							+ bObject.getObjectID()
							+ "'"
							+ ","
							+ "'"
							+ "Input"
							+ "'" + " )";
					System.out.println(query);
					DatabaseOperations dbop = new DatabaseOperations();
					flag = dbop.updateDB(query);

				}

				// Insert output values into the database
				for (BusinessObject bObject : outputObject) {
					query = "insert into scenario_bo_mapping (scenario_id,objectID,objectType) values("
							+ "'"
							+ rScenarioID.get(0)
							+ "'"
							+ ","
							+ "'"
							+ bObject.getObjectID()
							+ "'"
							+ ","
							+ "'"
							+ "Output" + "'" + " )";
					System.out.println(query);
					DatabaseOperations dbop = new DatabaseOperations();
					flag = dbop.updateDB(query);

				}

				// Update to user that the save operation has been completed
				FacesMessage msg = new FacesMessage("Scenario name "
						+ scenarioName + " is saved");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				/*
				 * ExternalContext context =
				 * FacesContext.getCurrentInstance().getExternalContext();
				 * context.redirect("scenario_list.xhtml");
				 */

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

	}

	public String editScenario(Scenario selectedScenario) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext()
				.getSessionMap()
				.put("selectedScenarioName", selectedScenario.getScenarioName());
		context.getExternalContext()
		.getSessionMap()
		.put("selectedScenarioID", selectedScenario.getScenarioID());

		return "editScenario";

	}

	public void leftOverOperations(String selectedScenarioName) {

		System.out.println("Scenario Name :" + selectedScenarioName);
		businessObjects = new DatabaseOperations().readBusinessObjects();
		businessObjectsCopy.addAll(businessObjects);

		String query = "select scenarios_id from scenarios where scenario_name = '"
				+ selectedScenarioName + "'";
		// System.out.println(query);

		ArrayList<ArrayList<String>> scenarioDetails = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> objectDetails = new ArrayList<ArrayList<String>>();
		ArrayList<String[]> scenarioMapping = new ArrayList<String[]>();
		ArrayList<String> scenarioNameList = new ArrayList<String>();
		DatabaseOperations dbOp = new DatabaseOperations();

		scenarioNameList = dbOp.executegetColumnQuery(query, 1);
		String selectedScenarioID = scenarioNameList.get(0);
		scenarioName = selectedScenarioName;

		query = "select * from scenario_bo_mapping where scenario_id ='"
				+ selectedScenarioID + "'";
		scenarioMapping = dbOp.getResultsetString(query);

		for (String[] temp : scenarioMapping) {
			ArrayList<String> stringList = new ArrayList<String>(
					Arrays.asList(temp));
			scenarioDetails.add(stringList);
		}

		query = "select * from business_objects";
		scenarioMapping = dbOp.getResultsetString(query);

		for (String[] temp : scenarioMapping) {
			ArrayList<String> stringList = new ArrayList<String>(
					Arrays.asList(temp));
			objectDetails.add(stringList);
		}

		for (ArrayList<String> temp : scenarioDetails) {
			System.out.println(temp.get(3));
			if (temp.get(3).equals("Pre-Requisite")) {
				ArrayList<String> objInfo = new ArrayList<String>();
				BusinessObject bo = new BusinessObject();
				bo.setObjectID(Integer.parseInt(temp.get(2)));

				query = "select objectName from business_objects where objectID = '"
						+ temp.get(2) + "'";
				dbOp = new DatabaseOperations();
				objInfo = dbOp.executegetColumnQuery(query, 1);

				bo.setObjectName(objInfo.get(0));

				preReqObjects.add(bo);

				for (BusinessObject ob : businessObjects) {
					if (ob.getObjectName().equals(bo.getObjectName())) {
						businessObjectsCopy.remove(ob);
					}
				}

			}

			if (temp.get(3).equals("Input")) {
				ArrayList<String> objInfo = new ArrayList<String>();
				BusinessObject bo = new BusinessObject();
				bo.setObjectID(Integer.parseInt(temp.get(2)));

				query = "select objectName from business_objects where objectID = '"
						+ temp.get(2) + "'";
				dbOp = new DatabaseOperations();
				objInfo = dbOp.executegetColumnQuery(query, 1);

				bo.setObjectName(objInfo.get(0));

				inputObject.add(bo);

				for (BusinessObject ob : businessObjects) {
					if (ob.getObjectName().equals(bo.getObjectName())) {
						businessObjectsCopy.remove(ob);
					}
				}

			}

			if (temp.get(3).equals("Output")) {
				ArrayList<String> objInfo = new ArrayList<String>();
				BusinessObject bo = new BusinessObject();
				bo.setObjectID(Integer.parseInt(temp.get(2)));

				query = "select objectName from business_objects where objectID = '"
						+ temp.get(2) + "'";
				dbOp = new DatabaseOperations();
				objInfo = dbOp.executegetColumnQuery(query, 1);

				bo.setObjectName(objInfo.get(0));

				outputObject.add(bo);

				for (BusinessObject ob : businessObjects) {
					if (ob.getObjectName().equals(bo.getObjectName())) {
						businessObjectsCopy.remove(ob);
					}
				}

			}

			selectedScenarioMain.setPreReqObjects(preReqObjects);
			selectedScenarioMain.setInputObjects(inputObject);
			selectedScenarioMain.setOutputObjects(outputObject);

		}
	}

	public void removeScenario() {

		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		try {
			int scenarioID = (Integer) sessionMap.get("selectedScenarioID");

			String query = "delete from scenario_bo_mapping where scenario_id = '"
					+ scenarioID + "'";

			DatabaseOperations dbOp = new DatabaseOperations();
			dbOp.updateDB(query);

			query = "delete from scenarios where scenarios_id = '" + scenarioID
					+ "'";
			dbOp = new DatabaseOperations();
			dbOp.updateDB(query);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
