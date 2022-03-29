package com.hackernews.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hackernews.web.dao.Itemdao;
import com.hackernews.web.model.Item;

public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action =request.getParameter("action"); 
		String content=request.getParameter("content");
		String src=request.getParameter("src");
		String url="./";
		if(action.equals("edit")) 
		{
			int commentId =Integer.parseInt(request.getParameter("commentid")); 
			int parentId =Integer.parseInt(request.getParameter("parentid")); 
			String commentEditQuery="update items set content='"+content+"' where id="+commentId;
			Itemdao.editItems(commentEditQuery);
			url="./"+src+"?item="+parentId;

		}
		else if(action.equals("delete"))
		{
			int commentId =Integer.parseInt(request.getParameter("commentid")); 
			int parentId =Integer.parseInt(request.getParameter("parentid")); 
			
			
			String deleteQuery="delete from items where id in (with RECURSIVE comments_paths (id, content, path) AS ( SELECT id, content, CAST(id AS CHAR(200)) FROM "+
			"items WHERE id in (select id from items where parent="+ parentId +") UNION ALL SELECT t.id, t.content, CONCAT(cp.path, ',', t.id) FROM comments_paths AS cp JOIN items AS t"
			+ " ON cp.id = t.parent) SELECT id FROM comments_paths where path like '%"+commentId+"%' );";
			
			
			Item update=null;
			//String descendantsUpdateQuery="update items set descendants=descendants-"+noOfDeleted+" where id in (select parent where id="+commentId+")";
			//int updatingParent=parentId;
			ArrayList < Item > listitems=Itemdao.getitems("select I.*,A.name as authorName from items I,author A where I.author_id=A.id and I.id="+commentId);
			Iterator<Item> iterator = listitems.iterator(); 
			update=iterator.next();
			int updatingParent=update.getParent();
			//to update descendants of parents and parents of parents
			
			int noOfDeleted=Itemdao.deleteItems(deleteQuery);

			while(updatingParent!=0) {
				Itemdao.editItems("update items set descendants=descendants-"+noOfDeleted+" where id ="+updatingParent);
				listitems=Itemdao.getitems("select I.*,A.name as authorName from items I,author A where I.author_id=A.id and I.id="+updatingParent);

				iterator = listitems.iterator(); 
				if(iterator.hasNext()) {
					update=iterator.next();
					updatingParent=update.getParent();
				}
				else
				{
					updatingParent=0;
				}
			}
			
			url="./"+src+"?item="+parentId;
		}
		else if(action.equals("upvote"))
		{
			int id =Integer.parseInt(request.getParameter("id")); 
			String commentScoreQuery="update items set score=score+1 where id="+id;
			Itemdao.editItems(commentScoreQuery);
		}
		response.sendRedirect(url);
	}
}
