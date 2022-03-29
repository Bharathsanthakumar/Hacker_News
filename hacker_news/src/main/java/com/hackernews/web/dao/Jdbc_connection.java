package com.hackernews.web.dao;
import java.sql.*;
import java.util.Enumeration;

public class Jdbc_connection {
	
	public static Connection connect() throws Exception
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackernews","root","123");
		return con;
	}
	
	public static void deregisterdriver() throws SQLException
	{
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) 
		{
			Driver driver = drivers.nextElement();
		    DriverManager.deregisterDriver(driver);
		}
	}	
}
