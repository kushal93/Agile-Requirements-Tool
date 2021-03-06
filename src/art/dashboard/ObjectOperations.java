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

import org.primefaces.event.RowEditEvent;

import art.database.DatabaseOperations;
import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;

@ManagedBean
@SessionScoped
public class ObjectOperations implements Serializable {
	private static List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
	/*
	 * this variable is used to bind front end input where user creates a new
	 * attribute byentering name of business object
	 */
	private Map<String, String> dropDownObjectTypes;
	private String objectName;
	private String objectType;
	/*
	 * this variable is used to identify which BO is selected when user clicks
	 * on edit attributes in home page
	 */
	private BusinessObject selectedBO;
	private BusinessObject duplicateCopyofSelectedBO;

	// For attribute operations
	private BOAttribute attribute = new BOAttribute();

	// private List<BOAttribute> attributes = new ArrayList<BOAttribute>();

	public ObjectOperations() {
		// initialize drop down menu for front end
		dropDownObjectTypes = new HashMap<String, String>();
		dropDownObjectTypes.put("Transaction Business Object",
				"Transaction Business Object");
		dropDownObjectTypes.put("Core Business Object (Default)",
				"Core Business Object");

		// List<BOAttribute> temp = new ArrayList<BOAttribute>();
		// temp.add(new BOAttribute(1, "Name", "String", "NA", "length<20"));
		// temp.add(new BOAttribute(1, "ID", "Integer", "NA", "length<20"));

		// read the business objects from DB
		businessObjects = new DatabaseOperations().readBusinessObjects();
	}

	/***************************************************************************
	 * Method: addObject
	 * 
	 * Purpose: This method takes care of adding a business object to current
	 * list check if a business object with same name already exist
	 * 
	 * Attributes: objectName - this is the attribute which contains business
	 * object name as provided by user from front end
	 * 
	 * checks: 1.Checks if method is invoked with empty name 2.Checks if a
	 * business object with same name already exists
	 * 
	 * Notes: 1.Need to add db code
	 ****************************************************************************/
	public String addObject() {
		if (objectName.equals("")) {
			FacesMessage msg = new FacesMessage(
					"Please enter Business Object Name");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if (objectType.equals("")) {
			FacesMessage msg = new FacesMessage(
					"Please enter Business Object Type");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			boolean exists = false;
			for (BusinessObject object : businessObjects) {
				if (object.getObjectName().equalsIgnoreCase(objectName))
					exists = true;
			}
			if (exists) {
				FacesMessage msg = new FacesMessage(
						"Business Object with name "
								+ objectName
								+ " already exists.Please choose a different name");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				businessObjects.add(new BusinessObject(
						businessObjects.size() + 1, objectName, objectType,
						new ArrayList<BOAttribute>()));
				new Thread(new Runnable() {
					public void run() {
						try {
							String query = "insert into business_objects values (nextval('art_seq'),"
									+ "'"
									+ objectName
									+ "'"
									+ ","
									+ "'"
									+ objectType + "'" + " )";
							new DatabaseOperations().updateDB(query);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Error in Class"
									+ this.getClass().getName()
									+ " -> addObject()--" + e.getMessage());
						}

					}
				}).start();

			}
			// objectName = "";
		}
		return null;
	}

	/***************************************************************************
	 * Method: onEdit
	 * 
	 * Purpose: This method is invoked when user opts to edit a row in datatable
	 * User will be able to just edit name of the object
	 * 
	 * Attributes:
	 * 
	 * checks:
	 * 
	 * Notes: 1.Need to add db code
	 ****************************************************************************/
	public void onEdit(RowEditEvent event) {
		UIData table = (UIData) event.getComponent();
		String updateClientId = table.getClientId() + ":" + table.getRowIndex()
				+ ":objectForm:table";
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add(updateClientId);

		BusinessObject objectSelected = (BusinessObject) event.getObject();
		if (objectSelected.getObjectName().equals("")) {
			FacesMessage msg = new FacesMessage("Please enter a name");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			boolean exists = false;
			for (BusinessObject object : businessObjects) {
				if (object.getObjectName().equalsIgnoreCase(objectName))
					exists = true;
			}
			if (exists) {
				FacesMessage msg = new FacesMessage(
						"Business Object with name "
								+ objectSelected.getObjectName()
								+ " already exists.Please choose a different name");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				for (BusinessObject object : businessObjects) {
					if (object.getObjectID() == objectSelected.getObjectID())
						object.setObjectName(objectSelected.getObjectName());
				}
				FacesMessage msg = new FacesMessage("Business Object Edited: ",
						((BusinessObject) event.getObject()).getObjectName());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public void onDelete(RowEditEvent event) {
		BusinessObject businessObject = (BusinessObject) event.getObject();
		// System.out.println(businessObject.getObjectName());
		businessObjects.remove((BusinessObject) event.getObject());
		FacesMessage msg = new FacesMessage("Object Deleted");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		new Thread(new Runnable() {
			public void run() {
				try {
					String query = "delete from business_objects where objectName = '"
							+ businessObject.getObjectName() + "'";
					new DatabaseOperations().updateDB(query);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error in Class"
							+ this.getClass().getName() + " -> onDelete()--"
							+ e.getMessage());
				}
			}
		}).start();
		
		//this is to force reload the page from backend.
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Once this method has been invoked the selected business attribute has to
	 * be identified and its attributes have to be loaded
	 */
	public String editAttributes(BusinessObject object) {
		selectedBO = object;
		// Check if the attributes are populated. Else load from DB
		if (selectedBO.getAttributes() == null
				|| selectedBO.getAttributes().size() == 0) {
			// get from DB
			selectedBO.attributes = new DatabaseOperations()
					.readAttributes(selectedBO.getObjectName());

			// reset it to null if no values have been returned => nothing is
			// populated yet

			// create a duplicate copy also - useful in deleteAttribute
			// Operation
			duplicateCopyofSelectedBO = new BusinessObject();
			duplicateCopyofSelectedBO.attributes = new ArrayList<BOAttribute>();
			duplicateCopyofSelectedBO.attributes.addAll(selectedBO.attributes);
		}

		return "editAttributes";
	}

	/***************************************************************************
	 * Method: addAttribute
	 * 
	 * Purpose: This method takes care of adding a attribute to current business
	 * object selected
	 * 
	 * Attributes: attributeName - this is the backing bean variable which
	 * contains Attribute name as provided by user from front end
	 * 
	 * attributeType - this is the backing bean variable which contains
	 * Attribute Type as provided by user from front end
	 * 
	 * businessRule - this is the backing bean variable which contains Business
	 * Rule as provided by user from front end
	 * 
	 * 
	 * checks: 1.Checks if method is invoked with empty values 2.Checks if a
	 * attribute with same name already exists
	 * 
	 * 
	 ****************************************************************************/
	public void addAttribute() {
		boolean exists = false;

		if (attribute.getAttributeName().equals("")
				|| attribute.getAttributeType().equals("")) {
			// || attribute.getBusinessRule().equals("")) {
			FacesMessage msg = new FacesMessage(
					"Please enter all the input values correctly");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			for (BOAttribute temp : selectedBO.attributes) {
				if (attribute.getAttributeName().equalsIgnoreCase(
						temp.getAttributeName())) {
					exists = true;
				}
				System.out.println("Test---------------"
						+ temp.getAttributeName() + "-----"
						+ attribute.getAttributeName() + "-----" + exists);
			}
			if (exists) {
				FacesMessage msg = new FacesMessage("Attribute name "
						+ attribute.getAttributeName()
						+ " already exists.Please choose a different name");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				
				
			} else {
				try {

					selectedBO.addAttribute(attribute.getAttributeName(),
							attribute.getAttributeType(),
							attribute.getMandatoryType(),
							attribute.getBusinessRule());

					duplicateCopyofSelectedBO.addAttribute(
							attribute.getAttributeName(),
							attribute.getAttributeType(),
							attribute.getMandatoryType(),
							attribute.getBusinessRule());

					System.out.println(attribute.getConditionalMandatroy());

					// push values to DB also
					// Note - using THREADS gave raise to ERRORS- So not using
					try {
						String query = "insert into attributes values (" + "'"
								+ selectedBO.getObjectName() + "'" + ","
								+ "nextval('art_seq')" + "," + "'"
								+ attribute.getAttributeName() + "'" + ","
								+ "'" + attribute.getAttributeType() + "'"
								+ "," + "'" + attribute.getMandatoryType()
								+ "'" + "," + "'" + attribute.getBusinessRule()
								+ "'" + " )";
						System.out.println(query);
						new DatabaseOperations().updateDB(query);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error in Class"
								+ this.getClass().getName()
								+ " -> addAttribute()--" + e.getMessage());
					}
				} finally {
					// reset all values
					attribute = new BOAttribute();
				}
			}
		}
	}

	/***************************************************************************
	 * Method: onDeleteAttribute
	 * 
	 * Purpose: This method is invoked when user opts to delete an attribute
	 ****************************************************************************/
	public void onDeleteAttribute() {
		// Find out the attribute that is deleted - for this puropse only
		// duplicateBO is created & updated.
		BOAttribute attributeToBeDeleted = new BOAttribute();
		for (BOAttribute iterator : duplicateCopyofSelectedBO.attributes) {
			if (!selectedBO.attributes.contains(iterator)) {
				// implies that is the attribute to be deleted
				attributeToBeDeleted = iterator;
			}

		}
		// First delete old BO from main list
		businessObjects.remove(selectedBO);
		// edit the selected BO
		selectedBO.attributes.remove(attributeToBeDeleted);
		// Add it again to the main BO's List
		businessObjects.add(selectedBO);

		FacesMessage msg = new FacesMessage("Attribute Deleted");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		// Dont use threads for attributes page - has some problem
		try {
			String query = "delete from attributes where objectName = '"
					+ selectedBO.getObjectName() + "' and attributeName = '"
					+ attributeToBeDeleted.getAttributeName() + "'";
			new DatabaseOperations().updateDB(query);
			System.out.println(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Class" + this.getClass().getName()
					+ " -> onDeleteAttribute()--" + e.getMessage());
		}
		// remove from duplicate copy also
		duplicateCopyofSelectedBO.attributes.remove(attributeToBeDeleted);
	}

	/***************************************************************************
	 * Method: onEdit
	 * 
	 * Purpose: This method is invoked when user opts to edit a row in datatable
	 * 
	 * Attributes:
	 * 
	 * checks:
	 * 
	 * Notes: 1.Need to add db code
	 ****************************************************************************/
	public void onEditAttribute(RowEditEvent event) {

	}

	public void onDeleteAttribute(RowEditEvent event) {
		// attributes.remove((BOAttribute) event.getObject());
		selectedBO.attributes.remove((BOAttribute) event.getObject());
		FacesMessage msg = new FacesMessage("Attribute Deleted");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String reinit() {
		setAttribute(new BOAttribute());
		return null;

	}

	public List<BusinessObject> getBusinessObjects() {
		return businessObjects;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public BusinessObject getSelectedBO() {
		return selectedBO;
	}

	public void setSelectedBO(BusinessObject selectedBO) {
		this.selectedBO = selectedBO;
	}

	public BOAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(BOAttribute attribute) {
		this.attribute = attribute;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Map<String, String> getDropDownObjectTypes() {
		return dropDownObjectTypes;
	}

	public void setDropDownObjectTypes(Map<String, String> dropDownObjectTypes) {
		this.dropDownObjectTypes = dropDownObjectTypes;
	}
}
