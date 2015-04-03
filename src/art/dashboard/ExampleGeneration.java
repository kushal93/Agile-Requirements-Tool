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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.github.javafaker.Faker;

import nl.flotsam.xeger.Xeger;
import dk.brics.automaton.*;
import art.database.DatabaseOperations;
import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;
import art.datastructures.Example;

import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceRequest;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.base.ReasonType;
import com.google.visualization.datasource.base.ResponseStatus;
import com.google.visualization.datasource.base.StatusType;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;

@ManagedBean(value = "exampleGeneration")
@ViewScoped
public class ExampleGeneration implements Serializable {
	private List<Example> exampleList;

	public String generateEmailData() {
		Faker faker = new Faker();
		return faker.internet().emailAddress();
	}

	public String generateNameData() {
		Faker faker = new Faker();
		return faker.name().fullName();
	}

	public String generateAddressData() {
		Faker faker = new Faker();
		String address = "";
		address = faker.address().streetName() + ", " + faker.address().streetAddress(true);
		return address;
	}

	public ArrayList<BOAttribute> getAttributeList(/*
													 * ArrayList<BusinessObject>
													 * pBusinessObjectList
													 */) {
		DatabaseOperations dbOperation = new DatabaseOperations();
		ArrayList<BusinessObject> pBusinessObjectList = new ArrayList<BusinessObject>();

		// Example example = new Example();
		ArrayList<BOAttribute> attributeList = new ArrayList<BOAttribute>();

		ArrayList<BOAttribute> tempArray = new ArrayList<BOAttribute>();

		// Populating the sample BO List
		pBusinessObjectList = dbOperation.readBusinessObjects();

		// Collecting the list of attributes from the pBusinessObjectList
		for (BusinessObject bObject : pBusinessObjectList) {
			dbOperation = new DatabaseOperations();
			tempArray = dbOperation.readAttributes(bObject.getObjectName());
			attributeList.addAll(tempArray);
		}
		return attributeList;
	}

	public ArrayList<String> generateExampleData(ArrayList<BOAttribute> attributeList) {
		ArrayList<String> exampleDataList = new ArrayList<String>();

		// Initialising for the xeger utility
		String regex = "";
		Xeger generator = new Xeger(regex);

		// generating the data using xeger and customised faker methods
		for (BOAttribute boa : attributeList) {
			// System.out.println(boa.getAttributeName()+"--"+boa.getAttributeType()+"---"+boa.getBusinessRule());
			if (boa.getAttributeType().equals("RegEx") && !boa.getBusinessRule().equalsIgnoreCase("null")) {
				generator = new Xeger(boa.getBusinessRule());
				exampleDataList.add(generator.generate());
				// System.out.println(generator.generate());
			} else if (boa.getAttributeType().equals("Name")) {
				exampleDataList.add(generateNameData());
				// System.out.println(generateNameData());
			} else if (boa.getAttributeType().equals("Email")) {
				exampleDataList.add(generateEmailData());
				// System.out.println(generateEmailData());
			} else if (boa.getAttributeType().equals("Address")) {
				exampleDataList.add(generateAddressData());
				// System.out.println(generateAddressData());
			}
		}
		return exampleDataList;
	}

	public String getExampleString(ArrayList<String> stringList) {
		String exampleString = "";
		for (String str : stringList)
			exampleString += str.replace(", ", "-") + ",";
		exampleString = exampleString.substring(0, exampleString.length() - 1);
		System.out.println(exampleString);
		return exampleString;
	}

	public void generateExampleBatch(int size) {
		String exampleHeaderString = "";
		ArrayList<BOAttribute> attributeList = getAttributeList();
		for (BOAttribute boa : attributeList) {
			exampleHeaderString += boa.getAttributeName().replace(",", "") + ",";
		}
		exampleHeaderString = exampleHeaderString.substring(0, exampleHeaderString.length() - 1);
		System.out.println(exampleHeaderString+"\n");
		File file = new File(System.getProperty("user.dir")+"/MSITProject/ART_Project/WebContent/DataTable2.csv");
		// if file doesn't exists, then create it
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(exampleHeaderString+"\n");
			for (int i = 0; i < size; i++) {
				bw.append(getExampleString(generateExampleData(attributeList)) + "\n");
			}
			bw.close();
			System.out.println("Done");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("exampleGeneration.xhtml");
		} catch (IOException e) {
			System.out.println("Page Error");
			e.printStackTrace();
		}
		
	}
}