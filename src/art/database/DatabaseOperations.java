package art.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.google.visualization.*;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;

import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;
import art.datastructures.Scenario;

public class DatabaseOperations {

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public DatabaseOperations() {
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/art_db?"
							+ "user=root&password=1");

			System.out
					.println("Connection Successfull and the DATABASE NAME IS:"
							+ connection.getMetaData().getDatabaseProductName());
		} catch (Exception e) {
			System.out.println("Error in Database Operations :"
					+ e.getMessage());
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery(query);
			
		} catch (Exception e) {
			System.out.println("Error in Database Operations :"
					+ e.getMessage());
	
		}
		return resultSet;

	}
	
	public ArrayList<String> executegetColumnQuery(String query,int columnIndex) {
		ArrayList<String> resultString = new ArrayList<String>();
		
		try {
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery(query);
		    //get the values from resultSet
			while (resultSet.next()) {
			resultString.add(resultSet.getString(columnIndex));
		    }
		    
		} catch (Exception e) {
			System.out.println("Error in Database Operations :"
					+ e.getMessage());
		} finally {
			try {
				// close all connections
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Database Operations :"
						+ e.getMessage());
			}
		}
		return resultString;

	}
	
	public int updateDB(String query) {
		int flag=0;
		try {
			System.out.println("Query: " + query);
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			statement.execute(query);
		} catch (Exception e) {
			System.out.println("Error in DatabaseOperations -> updateDB() :"
					+ e.getMessage());
			flag=-1;
		} finally {
			try {
				// close all connections
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out
						.println("Error in DatabaseOperations -> updateDB() :"
								+ e.getMessage());
				flag=-1;
			}
		}
		return flag;
	}
	
	public int deleteObjectsDB(String query) {
		int flag=0;
		try {
			System.out.println("Query: " + query);
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			statement.execute(query);
		} catch (Exception e) {
			System.out.println("Error in DatabaseOperations -> updateDB() :"
					+ e.getMessage());
			flag=-1;
			
		} finally {
			try {
				// close all connections
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out
						.println("Error in DatabaseOperations -> updateDB() :"
								+ e.getMessage());
				flag=-1;
			}
		}
		return flag;
	}
	

	public ArrayList<BusinessObject> readBusinessObjects() {
		ArrayList<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
		String query = "select * from business_objects";
		try {
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery(query);

			// iterate through result set and populate arraylist
			// attributes will not be populated. They will be lazy loaded when
			// required
			while (resultSet.next()) {
				businessObjects.add(new BusinessObject(resultSet
						.getInt("objectID"), resultSet.getString("objectName"),
						resultSet.getString("objectType"), null));
			}

		} catch (Exception e) {
			System.out
					.println("Error in Database Operations -> readBusinessObjects() :"
							+ e.getMessage());
		} finally {
			try {
				// close all connections
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out
						.println("Error in Database Operations -> readBusinessObjects():"
								+ e.getMessage());
			}
		}

		return businessObjects;
	}

	public ArrayList<BOAttribute> readAttributes(String businessObjectName) {
		ArrayList<BOAttribute> attributes = new ArrayList<BOAttribute>();
		String query = "select * from attributes where objectName='"
				+ businessObjectName + "'";
		try {
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery(query);

			// iterate through result set and populate arraylist
			// attributes will not be populated. They will be lazy loaded when
			// required
			while (resultSet.next()) {
				attributes.add(new BOAttribute(resultSet.getInt("attributeID"),
						resultSet.getString("attributeName"), resultSet
								.getString("dataType"), resultSet
								.getString("mandatoryType"), resultSet
								.getString("conditionalData"), resultSet
								.getString("dataFormat")));
			}

		} catch (Exception e) {
			System.out
					.println("Error in Database Operations -> readattributes() :"
							+ e.getMessage());
		} 

		return attributes;
	}
	
	public ArrayList<Scenario> getScenarioList() {
		ArrayList<String> scenarioNameList = new ArrayList<String>();
		ArrayList<String> scenarioIDList = new ArrayList<String>();
		ArrayList<Scenario> scenario = new ArrayList<Scenario>();
		String query1 = "select scenario_name from scenarios";
		String query2 = "select scenarios_id from scenarios";
		try {
			DatabaseOperations dbOp = new DatabaseOperations();
			scenarioNameList = dbOp.executegetColumnQuery(query1,1);
			dbOp = new DatabaseOperations();
			scenarioIDList = dbOp.executegetColumnQuery(query2, 1);
	        
			for(int i=0; i< scenarioNameList.size();i++) {
	          Scenario temp = new Scenario();
	          temp.setScenarioName(scenarioNameList.get(i));
	          temp.setScenarioID(Integer.parseInt(scenarioIDList.get(i)));
	          scenario.add(temp);
	        }
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
				
		return scenario;
	}
	
	public ArrayList<String[]> getResultsetString (String query) {
		ArrayList <String[]> result = new ArrayList<String[]>();
		DatabaseOperations dbOp = new DatabaseOperations();
		ResultSet rs = dbOp.executeQuery(query);
		try {
		int columnCount = rs.getMetaData().getColumnCount();
		while(rs.next())
		{
		    String[] row = new String[columnCount];
		    for (int i=0; i <columnCount ; i++)
		    {
		       row[i] = rs.getString(i + 1);
		    }
		    result.add(row);
		}
		} catch (SQLException e) {
		  System.out.println(e.getMessage());	
		}
		return result;
	}
	
}
