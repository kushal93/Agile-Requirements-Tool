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

import com.github.javafaker.Faker;

import nl.flotsam.xeger.Xeger;
import dk.brics.automaton.*;
import art.database.DatabaseOperations;
import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;
import art.datastructures.Example;
@ManagedBean(value = "exampleGeneration")
@ViewScoped

public class ExampleGeneration implements Serializable{
	private List<Example> exampleList;
	
	public String generateEmailData()
	{
		Faker faker = new Faker();
		return faker.internet().emailAddress();
	}
	public String generateNameData()
	{
		Faker faker = new Faker();
		return faker.name().fullName();
	}
	public String generateAddressData()
	{
		Faker faker = new Faker();
		String address="";
		address=faker.address().streetName() +", "+faker.address().streetAddress(true);
		return address;
	}
	public String/*ArrayList<String>*/ generateExampleData(/*ArrayList<BusinessObject> pBusinessObjectList*/)
	{
		ArrayList<String> exampleDataList = new ArrayList<String>();
		DatabaseOperations dbOperation = new DatabaseOperations();
		ArrayList<BusinessObject> pBusinessObjectList = new ArrayList<BusinessObject>();
		
		//Example example = new Example();
		ArrayList<BOAttribute> attributeList = new ArrayList<BOAttribute>();

		ArrayList<BOAttribute> tempArray = new ArrayList<BOAttribute>();
		//Populating the sample BO List
		pBusinessObjectList = dbOperation.readBusinessObjects();
		
		//Collecting the list of attributes from the pBusinessObjectList 
		for(BusinessObject bObject:pBusinessObjectList)
		{
			dbOperation = new DatabaseOperations();
			tempArray = dbOperation.readAttributes(bObject.getObjectName());
			attributeList.addAll(tempArray);
		}
		
		//Initialising for the xeger utility
		String regex="";
		Xeger generator = new Xeger(regex);
		
		//generating the data using xeger and customised faker methods
		for(BOAttribute boa:attributeList)
		{
			//System.out.println(boa.getAttributeName()+"--"+boa.getAttributeType()+"---"+boa.getBusinessRule());
			if(boa.getAttributeType().equals("RegEx") && !boa.getBusinessRule().equalsIgnoreCase("null"))
			{
				generator=new Xeger(boa.getBusinessRule());
				exampleDataList.add(generator.generate());
				//System.out.println(generator.generate());
			}
			else if(boa.getAttributeType().equals("Name"))
			{
				exampleDataList.add(generateNameData());
				//System.out.println(generateNameData());
			}
			else if(boa.getAttributeType().equals("Email"))
			{
				exampleDataList.add(generateEmailData());
				//System.out.println(generateEmailData());
			}
			else if(boa.getAttributeType().equals("Address"))
			{
				exampleDataList.add(generateAddressData());
				//System.out.println(generateAddressData());
			}
		}	
		String exampleData="";
		for(String data:exampleDataList)
		{
			exampleData+=data+"\t";
		}
		return exampleData.substring(0, exampleData.length()-4);
	}

	public void generateExampleBatch()
	{
		int n=10;
		for(int i=0;i<n;i++)
		{
			System.out.println(generateExampleData());
		}
	}
}
