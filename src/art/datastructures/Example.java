package art.datastructures;

import java.io.Serializable;
import java.util.List;

public class Example implements Serializable {
	private String exampleString;
	private int numAttributes;
	private List<BOAttribute> exampleAttributeList;
	private List<String> exampleDataList;
	public String getExampleString() {
		return exampleString;
	}
	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}
	public int getNumAttributes() {
		return numAttributes;
	}
	public void setNumAttributes(int numAttributes) {
		this.numAttributes = numAttributes;
	}
	public List<BOAttribute> getExampleAttributeList() {
		return exampleAttributeList;
	}
	public void setExampleAttributeList(List<BOAttribute> exampleAttributeList) {
		this.exampleAttributeList = exampleAttributeList;
	}
	public List<String> getExampleDataList() {
		return exampleDataList;
	}
	public void setExampleDataList(List<String> exampleDataList) {
		this.exampleDataList = exampleDataList;
	}
	public Example(String exampleString, int numAttributes,
			List<BOAttribute> exampleAttributeList, List<String> exampleDataList) {
		super();
		this.exampleString = exampleString;
		this.numAttributes = numAttributes;
		this.exampleAttributeList = exampleAttributeList;
		this.exampleDataList = exampleDataList;
	}
	public Example() {
		// TODO Auto-generated constructor stub
	}
}
