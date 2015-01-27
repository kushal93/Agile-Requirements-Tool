package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import art.database.DatabaseOperations;

public class DbTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseOperations db = new DatabaseOperations();

		new Thread(new Runnable() {
			public void run() {
				// Do whatever
				ResultSet r = db.executeQuery("select * from user_details");
				try {
					while (r.next()) {
						System.out.println("User: " + r.getString("username"));
						System.out.println("passwd: " + r.getString("password"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

}
