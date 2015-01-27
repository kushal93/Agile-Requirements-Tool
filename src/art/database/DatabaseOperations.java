package art.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import art.datastructures.BOAttribute;
import art.datastructures.BusinessObject;

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
		return resultSet;

	}

	public void updateDB(String query) {
		try {
			System.out.println("Query: " + query);
			// statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			statement.execute(query);
		} catch (Exception e) {
			System.out.println("Error in DatabaseOperations -> updateDB() :"
					+ e.getMessage());
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
			}
		}

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
								.getString("dataFormat")));
			}

		} catch (Exception e) {
			System.out
					.println("Error in Database Operations -> readattributes() :"
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
						.println("Error in Database Operations -> readattributes():"
								+ e.getMessage());
			}
		}

		return attributes;
	}
}
