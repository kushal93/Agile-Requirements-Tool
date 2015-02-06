package art.datastructures;

import java.util.ArrayList;

public class Scenario {
 
	private String scenarioName;
	private int scenarioID;
	private ArrayList<BusinessObject> preReqObjects;
	private ArrayList<BusinessObject> inputObjects;
	private ArrayList<BusinessObject> outputObjects;
	
	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public int getScenarioID() {
		return scenarioID;
	}
	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}
	public ArrayList<BusinessObject> getPreReqObjects() {
		return preReqObjects;
	}
	public void setPreReqObjects(ArrayList<BusinessObject> preReqObjects) {
		this.preReqObjects = preReqObjects;
	}
	public ArrayList<BusinessObject> getInputObjects() {
		return inputObjects;
	}
	public void setInputObjects(ArrayList<BusinessObject> inputObjects) {
		this.inputObjects = inputObjects;
	}
	public ArrayList<BusinessObject> getOutputObjects() {
		return outputObjects;
	}
	public void setOutputObjects(ArrayList<BusinessObject> outputObjects) {
		this.outputObjects = outputObjects;
	}
	
}
