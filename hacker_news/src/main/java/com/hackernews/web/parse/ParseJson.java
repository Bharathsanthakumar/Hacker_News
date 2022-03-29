package com.hackernews.web.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.hackernews.web.dao.Itemdao;
import com.hackernews.web.model.Item;

public class ParseJson {
	private static HttpURLConnection connection;
	static BufferedReader reader;
	static String each_line;
	
	public static String apiHit(String API)
	{
		StringBuffer responseContent=new StringBuffer();
		try {
			
			URL url=new URL(API);
			connection=(HttpURLConnection)url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);	
			if(connection.getResponseCode() <299)
			{
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				while((each_line=reader.readLine())!=null)
				{
					responseContent.append(each_line);
				}
				reader.close();
			}
			else
			{
				reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				
				while((each_line=reader.readLine())!=null)
				{
					responseContent.append(each_line);
				}
				reader.close();
			}
		} 
		
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			connection.disconnect();
		}
		return (responseContent.toString());		
		
	}
	
	public static Item parseItem(String responseBody,int firstItem)
	{
		JSONObject obj=new JSONObject(responseBody);

		int id;
		int authorId;
		String type=obj.optString("type",null);
		String title;
		String storyUrl;
		String content;
		int score;
		int parent = obj.optInt("parent",0);
		int time;
		String dead;
		int poll;
		String deleted;
		int descendants;   		
		int updatingParent=0;
		
		Item item=null;
		Item update=null;

		if((type.equals("story") || (type.equals("comment")  && parent>=firstItem)) && !(obj.has("deleted")) && !(obj.has("dead"))) //either not deleted/dead story/comment or comment for old story 
		{
	    	System.out.println(responseBody);		//test

			item=new Item();
			
			
			try {
				id=obj.optInt("id",0);
				
				String authorName=obj.optString("by","");	
				authorId=Itemdao.insertauthor(authorName);
				title = obj.optString("title","click for this post");
				storyUrl = obj.optString("url","./");
				content =obj.optString("text","OOPS!!! No content");
				score = obj.optInt("score",0);
				time = obj.optInt("time",0);
				dead=obj.optString("dead","false");
				poll=obj.optInt("poll",0);
				deleted=obj.optString("deleted","false");
				descendants=obj.optInt("descendants",0);
				
				//to update descendants of parents and parents of parents
				updatingParent=parent;

				while(updatingParent!=0) {
					Itemdao.editItems("update items set descendants=descendants+1 where id="+updatingParent);
					ArrayList < Item > listitems=Itemdao.getitems("select I.*,A.name as authorName from items I,author A where I.author_id=A.id and I.id="+updatingParent);

					Iterator<Item> iterator = listitems.iterator(); 
					if(iterator.hasNext()) {
						update=iterator.next();
						updatingParent=update.getParent();
					}
					else
					{
						updatingParent=0;
					}
				}
				
				item.setId(id); 
				item.setAuthorId(authorId);	
				item.setType(type);
				item.setTitle(title);
				item.setUrl(storyUrl);
				item.setContent(content);
				item.setScore(score);
				item.setParent(parent);
				item.setTime(time);
				item.setDead(dead);
				item.setPoll(poll);
				item.setDeleted(deleted);
				item.setDescendants(descendants);
				
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return item;
		}
		return item;
	
	}
	
}


