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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;

import java.sql.ResultSet;

import art.database.DatabaseOperations;
import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;
import art.datastructures.Scenario;

import com.google.visualization.*;
import com.google.visualization.datasource.datatable.DataTable;

@ManagedBean(value = "scenarioListOperations")
@ViewScoped
public class ScenarioListOperations implements Serializable {
	/**
	 * Added serialversion ID
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BusinessObject> preReqObjects = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> inputObject = new ArrayList<BusinessObject>();
	private ArrayList<BusinessObject> outputObject = new ArrayList<BusinessObject>();
	private BusinessObject selectedPreReqObject = new BusinessObject();
	private BusinessObject selectedInputObject = new BusinessObject();
	private BusinessObject selectedOutputObject = new BusinessObject();
	private ArrayList<Scenario> scenarioList = new ArrayList<Scenario>();
	private String scenarioName;

	public ScenarioListOperations() {

		// read scenario list from DataBase
		scenarioList = new DatabaseOperations().getScenarioList();

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

	public void setPreReqObjects(ArrayList<BusinessObject> objects) {
		this.preReqObjects = objects;
	}

	public void setSelectedObject(BusinessObject object) {
		this.selectedPreReqObject = object;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String text) {
		this.scenarioName = text;
	}

	public ArrayList<Scenario> getScenarioList() {
		return scenarioList;
	}

	public void setScenarioList(ArrayList<Scenario> scenarioList) {
		this.scenarioList = scenarioList;
	}

	public void onRemoveScenario(Scenario scenario) {
		String query = "delete from scenario_bo_mapping where scenario_id = '"
				+ scenario.getScenarioID() + "'";

		DatabaseOperations dbOp = new DatabaseOperations();
		dbOp.updateDB(query);

		query = "delete from scenarios where scenario_name = '"
				+ scenario.getScenarioName() + "'";
		dbOp = new DatabaseOperations();
		dbOp.updateDB(query);
		
		scenarioList.remove(scenario);
		
		FacesMessage msg = new FacesMessage("Scenario" + scenario.getScenarioID() + "Deleted");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	

}