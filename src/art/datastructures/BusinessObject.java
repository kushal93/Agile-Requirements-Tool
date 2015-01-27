package art.datastructures;

import java.util.ArrayList;
import java.util.List;

public class BusinessObject {
	private String objectName;
	private String objectType;
	private int objectID;
	public List<BOAttribute> attributes;

	public BusinessObject(){
		setAttributes(new ArrayList<BOAttribute>());
	}
	
	public BusinessObject(int objectID, String objectName, String objectType, List<BOAttribute> attributes){
		this.objectID = objectID;
		this.objectName = objectName;
		this.objectType = objectType;
		this.attributes = attributes;
	}
	
	public void addAttribute(String attributeName, String attributeType,String mandatoryType, String businessRule){
		this.attributes.add(new BOAttribute(attributes.size()+1, attributeName, attributeType,mandatoryType, businessRule));
	}
	
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public List<BOAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<BOAttribute> attributes) {
		this.attributes = attributes;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
