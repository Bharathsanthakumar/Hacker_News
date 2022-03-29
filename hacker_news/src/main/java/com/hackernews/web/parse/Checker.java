package com.hackernews.web.parse;


import java.sql.Connection;

//to hit maxitem API every second

import java.util.Timer;
import java.util.TimerTask;

import com.hackernews.web.dao.Jdbc_connection;
import com.hackernews.web.dao.Itemdao;
import com.hackernews.web.model.Item;


public class Checker extends TimerTask {
	Item item=null;
	int maxItem,prevMaxItem=0; 
	int firstItem=0; 
	static Timer timer = new Timer();
	static TimerTask task = new Checker();
    public void run() 
    {
    	
    	String api;
    	String responseBody;
    	api="https://hacker-news.firebaseio.com/v0/maxitem.json?print=pretty"; //maxitem api
    	responseBody=ParseJson.apiHit(api);
		
    	maxItem=Integer.parseInt(responseBody);
    	
    	
    	if(prevMaxItem!=0)
		{
    		try 
			{
				Connection con = Jdbc_connection.connect();
				
				if(prevMaxItem < maxItem)
		    	{
		    		for(int value=prevMaxItem+1 ; value<=maxItem ; value++) //Iterating items since last 5 Seconds 
		    		{
		    			api="https://hacker-news.firebaseio.com/v0/item/" + value + ".json?print=pretty"; //API structured to hit each item
		    	    	responseBody=ParseJson.apiHit(api);
		    	    	if(!(responseBody.equals("null")))	
		    	    	{
		    	    		item=ParseJson.parseItem(responseBody,firstItem);
		    	    		if(item!=null)
			    	    	{
			    	    		Itemdao.pushitems(item,con);
			    	    	}
		    	    	}
		    		}
		    		prevMaxItem=maxItem;
		    	}
				con.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    	else
    	{
    		prevMaxItem=maxItem;
    		try 
    		{
				Connection con=Jdbc_connection.connect();
				firstItem=Itemdao.firstitem(con);
				con.close();
			}
    		catch (Exception e) 
    		{
				e.printStackTrace();
			}
    	}
    }
    
	public static void periodical()
	{
		timer.schedule(task, 0, 5000);
	}
	
	public static void to_terminate()
	{
		System.out.println(task.cancel());			//test
	}
}