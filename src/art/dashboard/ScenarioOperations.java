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
public class ScenarioOperations implements Serializable {
	private static List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
	
	public ScenarioOperations() {
		// read the business objects from DB
				businessObjects = new DatabaseOperations().readBusinessObjects();
	}
	
}
