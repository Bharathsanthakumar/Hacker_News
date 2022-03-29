<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Post</title>
		<%
		response.setIntHeader("Refresh",60);
		%>
	</head>
	<body>
		<script>
			function Openform(si)
			{
			  document.getElementsByClassName("form")[si-1].style.display = 'block';
			}
		</script>
		

		<%
		String itemId = request.getParameter("item");
		String backpage=request.getParameter("p");
		//String commentQuery="select I.*,A.name as authorName from items I,author A where I.parent="+itemId+" and I.author_id=A.id ";
		String parentQuery="select I.*,A.name as authorName from items I,author A where I.id="+itemId+" and I.author_id=A.id";
		String commentQuery="WITH RECURSIVE comments_paths (id, content,score,parent,descendants,path) AS ( SELECT id, content,score,parent,descendants, CAST(id AS CHAR(255)) FROM items WHERE id in (select id "
				  +"from items where parent="+itemId+") UNION ALL SELECT t.id, t.content,t.score,t.parent,t.descendants, CONCAT(cp.path, ',', t.id) FROM comments_paths AS cp JOIN items AS t ON cp.id = t.parent ) "
				+"SELECT * FROM comments_paths ORDER BY path;";
		
		String authorNameQuery="select I.id,A.name as authorName from items I,author A where I.author_id=A.id and I.id in (WITH RECURSIVE comments_paths (id) AS ( SELECT id FROM items "+
				"WHERE id in (select id from items where parent=30819579) UNION ALL SELECT t.id FROM comments_paths AS cp JOIN items AS t ON cp.id = t.parent) "+
				"SELECT id FROM comments_paths order by path) ;";
		%>
		<%@ page import = "java.io.*,java.util.*" %>
		<%@ page import="java.util.ArrayList"%>
		<%@ page import="java.util.Iterator"%> 
		<%@ page import = "java.io.*,java.util.*" %>
		<%@ page import="com.hackernews.web.model.Item"%>
		<%@ page import="com.hackernews.web.dao.Itemdao"%>
		
		<%
		ArrayList < Item > listParent=Itemdao.getitems(parentQuery);
		Iterator<Item> parentIterator = listParent.iterator();  
		Item item = parentIterator.next();

		ArrayList < Item > listComments=Itemdao.getcomments(commentQuery);
		Iterator<Item> commentsIterator = listComments.iterator(); 
		
		ArrayList < Item > listAuthor=Itemdao.getcommentsAuthor(authorNameQuery);
		Iterator<Item> authorIterator = listAuthor.iterator();
		Item author;

		%>
				
		<div style="margin-left:0;margin-right:auto;">
			<button type="button" name="Back" onclick="location.href = './index?p=<%=backpage%>'">Back</button>
		</div>
		<br>
		
		<table style="margin-left: auto; margin-right: auto; border-spacing:0px; width:90%" >
			<tr	 bgcolor="#ff6347" >
				<th colspan="10" style="font-size:30px;">HACKERNEWS</th>
			</tr>
			
			<tr style="background-color:beige;">
				<td	colspan="10" style="text-align:center;font-size:28px;">POST</td>
			</tr>
			<tr style="background-color:beige;">
				<td colspan="8" style="text-align:center;font-size:26px; font-weight: bold;"><br><%=item.getTitle()%><br><br></td>
			</tr>
			<tr style="background-color:beige;">
				<td colspan="8" style="text-align:center"><br>Click here for Full post (External URL):  <a href="<%= item.getUrl()%>" target="_blank"><%=item.getTitle()%></a><br><br></td>
			</tr>
			<tr>
				<td colspan="10"><iframe src="<%= item.getUrl()%>" height="500" width="1700" title="Iframe"></iframe></td>
			</tr>
			
			<% 	
			int serialno=1;
			while(commentsIterator.hasNext()&& authorIterator.hasNext()) 
			{
				item = commentsIterator.next(); 
				author=authorIterator.next();
				String str=item.getPath();
				String[] items = str.split(",");
				String space="&emsp;&emsp;";
				int countOfPaths=items.length;
				space=space.repeat(countOfPaths);
			%>
			<tbody style="background-color:silver; vertical-align: text-top;" >
			
				<tr  style="font-size:18px;">
				
					<td colspan="8">
						<%=space%><%=serialno%>.&nbsp;<%=item.getContent()%>
					</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td colspan="8" style="font-size:15px;">
					<%=space%>&nbsp;by: <%=author.getAuthorName()%>&emsp;|&emsp;Score: <%=item.getScore()%>&emsp;|&emsp;Total comments: <%=item.getDescendants() %>
					</td>
				</tr>
				<tr style="font-size:20px;">
					<td   colspan="8">&emsp;
					</td>
				</tr>	
				<tr>
					<td>
						
						<%=space%><input id="dynamic" type="button" value="EDIT COMMENT" onclick ="Openform(<%=serialno%>);"  style="overflow:hidden;">
						
						<button type="button"  onclick="location.href = './ItemServlet?parentid=<%=itemId%>&commentid=<%=item.getId()%>&action=delete&src=item.jsp'">DELETE</button>
						
						<form  class="form" action="./ItemServlet?parentid=<%=itemId%>&commentid=<%=item.getId()%>&action=edit&src=item.jsp" target = "_parent" style="display: none" ><br>
						    	<p>Edit,then click submit</p>
							<textarea id="content" name="content" rows="3" cols="60"></textarea>
							<br>	
							<input type="hidden" name="parentid" value="<%=itemId%>" /> 
  							<input type="hidden" name="commentid" value="<%=item.getId()%>" /> 
  							<input type="hidden" name="action" value="edit" /> 
  							<input type="hidden" name="src" value="item.jsp" /> 
							<input type="submit" value="Submit">
						</form>

					</td>
				</tr>
				<tr style="font-size:20px;">
					<td   colspan="8">&emsp;
					</td>
				</tr>	
				
					<%
					serialno++;}
					if(serialno==1)
					{%>
					<tr>
					<td colspan="8">NO COMMENTS</td>
					</tr>
					<%}%>
				
			</tbody>
		</table>
	</body>
</html>