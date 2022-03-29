package com.hackernews.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hackernews.web.model.Item;



public class Itemdao {
	
	
	public static ArrayList<Item> getitems(String selectQuery)
	{
		ArrayList < Item > itemsList = new ArrayList < Item> ();
		try 
		{
			Connection con=Jdbc_connection.connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(selectQuery);
			while(rs.next())
			{
				   Item each=new Item();
				   each.setId(rs.getInt("id"));  
				   each.setAuthorId(rs.getInt("author_id")); 
				   each.setType(rs.getString("item_type"));
				   each.setTitle(rs.getString("title"));
				   each.setUrl(rs.getString("url"));
				   each.setContent(rs.getString("content"));
				   each.setScore(rs.getInt("score"));
				   each.setParent(rs.getInt("parent"));
				   each.setTime(rs.getInt("unixtime"));
				   each.setDead(rs.getString("dead"));
				   each.setPoll(rs.getInt("poll"));
				   each.setDeleted(rs.getString("deleted"));
				   each.setDescendants(rs.getInt("descendants"));
				   each.setAuthorName(rs.getString("authorName"));
				   each.setPath(rs.getString("path"));
				   itemsList.add(each);
			}
			st.close();
			con.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return itemsList;
	}
	
	public static void pushitems(Item item, Connection con)
	{
		String query="insert into items(id,author_id,item_type,title,url,content,score,parent,unixtime,dead,poll,deleted,descendants) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try 
		{
			PreparedStatement st=con.prepareStatement(query);
			st.setInt(1,item.getId());
			st.setInt(2, item.getAuthorId());
			st.setString(3,item.getType());
			st.setString(4,item.getTitle());
			st.setString(5,item.getUrl());
			st.setString(6, item.getContent());
			st.setInt(7, item.getScore());
			st.setInt(8, item.getParent());
			st.setInt(9,item.getTime());   // id type authorid title url content score parent time dead poll deleted descendants
			st.setString(10, item.getDead());
			st.setInt(11,item.getPoll());
			st.setString(12,item.getDeleted());
			st.setInt(13, item.getDescendants());
			st.executeUpdate();
			st.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int insertauthor(String authorName)
	{
		int newAuthorId = 0;
		String insertQuery="insert into author(id,name) values(?,?)";
		String authorQuery="select id from author where name='"+authorName+"'";
		String lastAuthorQuery="select max(id)+1 as new_author from author";
		try 
		{
			Connection conn=Jdbc_connection.connect();
			PreparedStatement st;
			
			st=conn.prepareStatement(authorQuery);
			ResultSet rs = st.executeQuery();
			if(rs.next())
			{
				newAuthorId=rs.getInt("id");
				
			}
			else
			{
				st=conn.prepareStatement(lastAuthorQuery);
				rs=st.executeQuery();
				rs.next();
				newAuthorId=rs.getInt("new_author");
				st=conn.prepareStatement(insertQuery);
				st.setInt(1, newAuthorId);
				st.setString(2,authorName);
				st.executeUpdate();
			}
			
			st.close();
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return newAuthorId;
	}
	
	public static ArrayList<Item> getcomments(String selectQuery)
	{
		ArrayList < Item > commentList = new ArrayList < Item> ();
		try 
		{
			Connection con=Jdbc_connection.connect();
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(selectQuery);
			while(rs.next())
			{
				   Item each=new Item();
				   each.setId(rs.getInt("id"));  
				   each.setContent(rs.getString("content"));
				   each.setScore(rs.getInt("score"));
				   each.setParent(rs.getInt("parent"));
				   each.setDescendants(rs.getInt("descendants"));
				   each.setPath(rs.getString("path"));
				   commentList.add(each);
				   
			}
			st.close();
			con.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return commentList;
	}
	
	public static ArrayList<Item> getcommentsAuthor(String selectQuery)
	{
		ArrayList < Item > commentList = new ArrayList < Item> ();
		try 
		{
			Connection con=Jdbc_connection.connect();
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(selectQuery);
			while(rs.next())
			{
				   Item each=new Item();
				   each.setId(rs.getInt("id"));  
				   each.setAuthorName(rs.getString("authorName"));
				   commentList.add(each);
				   
			}
			st.close();
			con.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return commentList;
	}
	
	public static int firstitem(Connection con)
	{
		int firstId = 0;
		String firstItemQuery="select min(id) as first from items";
		try 
		{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(firstItemQuery);
			rs.next();
			firstId=rs.getInt("first");
			st.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return firstId;
	}
	
	public static int deleteItems(String deleteQuery)
	{
		int deletedItems = 0;
		try 
		{
			Connection con=Jdbc_connection.connect();
			Statement st = con.createStatement();
			//String deleteQuery="delete from items where id="+id;
			deletedItems=st.executeUpdate(deleteQuery);
			st.close();
			con.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return deletedItems;
	}
	
	public static int editItems(String updateQuery) 
	{
		int updatedItems=0;
		try 
		{
			Connection con=Jdbc_connection.connect();
			Statement st = con.createStatement();
			updatedItems=st.executeUpdate(updateQuery);
			st.close();
			con.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return updatedItems;
	}
	
}
