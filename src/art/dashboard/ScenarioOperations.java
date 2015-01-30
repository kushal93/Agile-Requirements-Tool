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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;

import art.database.DatabaseOperations;
import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;

@ManagedBean(value = "scenarioOperations")
@SessionScoped
public class ScenarioOperations implements Serializable {
	/**
	 * Added serialversion ID
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
	private ArrayList <BusinessObject> preReqObjects = new ArrayList <BusinessObject>() ;
	private ArrayList<BusinessObject> inputObject = new ArrayList <BusinessObject>();
	private ArrayList<BusinessObject> outputObject = new ArrayList <BusinessObject>();
	private BusinessObject selectedPreReqObject = new BusinessObject();
	private BusinessObject selectedInputObject = new BusinessObject();
	private BusinessObject selectedOutputObject = new BusinessObject();
	
	public ScenarioOperations() {
		
		// read the business objects from DB
		businessObjects = new DatabaseOperations().readBusinessObjects();
		/*businessObjects.add(new BusinessObject(50,"Core Business Object","test1",null));
				businessObjects.add(new BusinessObject(50,"Core Business Object","test2",null));*/
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
	
	public void onDragDropPreReq(DragDropEvent event) {  
        BusinessObject object = (BusinessObject) event.getData();  
  
        preReqObjects.add(object);  
        businessObjects.remove(object);
  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(object.getObjectName() + " added", "Position:" + event.getDropId()));  
    }  
	
	public void onDragDropInput(DragDropEvent event) {  
        BusinessObject object = (BusinessObject) event.getData();  
  
        inputObject.add(object);  
        businessObjects.remove(object);
  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(object.getObjectName() + " added", "Position:" + event.getDropId()));  
    }
	
	public void onDragDropOutput(DragDropEvent event) {  
        BusinessObject object = (BusinessObject) event.getData();  
  
        outputObject.add(object);  
        businessObjects.remove(object);
  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(object.getObjectName() + " added", "Position:" + event.getDropId()));  
    }
	
	public void setSelectedObject(BusinessObject object) {
        this.selectedPreReqObject = object;
    }
}
