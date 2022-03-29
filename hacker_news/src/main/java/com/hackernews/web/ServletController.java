package com.hackernews.web;


import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hackernews.web.dao.Jdbc_connection;
import com.hackernews.web.parse.Checker;


public class ServletController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		Checker.periodical();
	}
	
	public void destroy()
	{
		Checker.to_terminate();
		try 
		{
			Jdbc_connection.deregisterdriver();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
}
