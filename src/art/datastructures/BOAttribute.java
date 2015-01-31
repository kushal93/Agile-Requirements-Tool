package art.datastructures;

import java.io.Serializable;
import java.util.ArrayList;

public class BOAttribute implements Serializable {
	private int attributeID;
	private String attributeName;
	private String attributeType;
	private String businessRule;
	private String mandatoryType;
	private ArrayList<String> conditionalMandatory;
	private String conditionalDependencies;

	public BOAttribute() {
		mandatoryType = "NA";
		conditionalMandatory = new ArrayList<String>();
	}

	public BOAttribute(int attributeID, String attributeName,
			String attributeType, String mandatoryType, String conditionalDependencies, String businessRule) {
		this.attributeID = attributeID;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.mandatoryType = mandatoryType;
		this.businessRule = businessRule;
		this.conditionalDependencies = conditionalDependencies;
		
	}
	
	public BOAttribute(String attributeName) {
		this.attributeName = attributeName;
		conditionalMandatory = new ArrayList<String>();
	}

	public boolean equals(Object attribute) {
		if (this.attributeName.equals(((BOAttribute) attribute)
				.getAttributeName()))
			return true;
		else
			return false;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(String businessRule) {
		this.businessRule = businessRule;
	}

	public int getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(int attributeID) {
		this.attributeID = attributeID;
	}

	public String getMandatoryType() {
		return mandatoryType;
	}

	public void setMandatoryType(String isMandatoryType) {
		this.mandatoryType = isMandatoryType;
		if(mandatoryType.equals("Conditional Mandatory"))
			setConditionalMandatory(new ArrayList<String>());
		conditionalDependencies = String.join(",", conditionalMandatory);
	}

	public ArrayList<String> getConditionalMandatory() {
		   
		return conditionalMandatory;
		
	}
	
	public void setConditionalMandatory(ArrayList<String> conditionalMandatory) {
		this.conditionalMandatory = conditionalMandatory;
	}
	
	public String getConditionalDependencies() {
		return conditionalDependencies;
	}
	
//	public boolean CheckMandatoryType() {
//		if(mandatoryType.equals("Conditional Mandatory"))
//			return 
//		
//	}
}
