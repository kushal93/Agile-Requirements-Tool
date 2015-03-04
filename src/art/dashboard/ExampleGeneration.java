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
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import art.database.DatabaseOperations;
import art.datastructures.*;
@ManagedBean(value = "exampleGeneration")
@ViewScoped

public class ExampleGeneration implements Serializable{
	private List<Example> exampleList;
	
	public void generateExampleData(/*ArrayList<BusinessObject> pBusinessObjectList*/)
	{
		DatabaseOperations dbOperation = new DatabaseOperations();
		ArrayList<BusinessObject> pBusinessObjectList = new ArrayList<BusinessObject>();
		
		//Example example = new Example();
		ArrayList<BOAttribute> attributeList = new ArrayList<BOAttribute>();
		//BusinessObject bObject = new BusinessObject();
		//BOAttribute boAttribute = new BOAttribute();
		ArrayList<BOAttribute> tempArray = new ArrayList<BOAttribute>();
		//Populating the sample BO LIst
		pBusinessObjectList = dbOperation.readBusinessObjects();
		
		
		for(BusinessObject bObject:pBusinessObjectList)
		{
			dbOperation = new DatabaseOperations();
			tempArray = dbOperation.readAttributes(bObject.getObjectName());
			attributeList.addAll(tempArray);
			/*System.out.println("---::>"+bObject.getAttributes().size());*/
		}
		/*;
		tempArray = dbOperation.readAttributes("sairam");
		attributeList.addAll(tempArray);
		*/System.out.println("--------------------->");
		for(BOAttribute boa:attributeList)
			System.out.println(boa.getAttributeName());
	}
}
