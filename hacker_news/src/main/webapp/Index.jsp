<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<%
		response.setIntHeader("Refresh",5);
		int p;
		if(request.getParameter("p")!=null)
			p = Integer.parseInt(request.getParameter("p"));
		else
			p=1;
		
		p=(p==0)?(1):(p);
		%>
		
		<title>Home</title>
	</head>

	<body>
		
		<%@page import="java.util.ArrayList"%>
		<%@page import="java.util.Iterator"%> 
		<%@ page import = "java.io.*,java.util.*" %>
		<%@page import="com.hackernews.web.model.Item"%>
		<%@page import="com.hackernews.web.dao.Itemdao"%>
		
		<%
		String selectQuery="select I.*,A.name as authorName from items I,author A where I.item_type='story' and I.author_id=A.id order by id desc limit 10 offset "+((p-1)*10);
		%>
		<center>
		<table cellspacing="0" width="90%">
			<tr	style="background-color:tomato;"  >
				<th colspan="10" style="text-decoration:none;font-size:30px;"><a href="./Index.jsp?p=1">HACKERNEWS</a></th>
			</tr>
			
			<tr style="background-color:#d7b38c">
				<td	colspan="10" style="font-size:25px;"><center>NEW POSTS</center></td>
			</tr>
			<tbody style="background-color:powderblue;" >
			<%
			ArrayList < Item > listitems=Itemdao.getitems(selectQuery);
			Iterator<Item> iterator = listitems.iterator();  
			int serialno=(p-1)*10;
			while(iterator.hasNext()) 
			{
				serialno++;
				Item item = iterator.next(); 
				String type="./item.jsp?p="+p+"&item="+item.getId();
				if(!(item.getContent()).equals("OOPS!!! No content"))
				{
					type="./AskItem.jsp?p="+p+"&item="+item.getId();
				}
			%>
				<tr style="font-size:20px;">
					<td  colspan="2">&emsp;
					</td>
					<td   colspan="8">&emsp;
					</td>
				</tr>
				<tr style="font-size:20px;">
					<td  colspan="2">
						<%=serialno%>
					</td>
					<td   colspan="8">
						<a href="<%=type%>" style="text-decoration:none;"><%=item.getTitle()%></a>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					</td>
					<td colspan="8" style="font-size:18px;">
						<button type="button"  onclick="location.href = './ItemServlet?id=<%=item.getId()%>&action=upvote'">Upvote</button>					
						by: <%=item.getAuthorName()%>&emsp;|&emsp;Score: <%=item.getScore()%>&emsp;|&emsp;Total comments: <%=item.getDescendants() %>
					</td>
				</tr>
			<%
				}
			%>
			<tr style="font-size:20px;">
					<td  colspan="2">&emsp;
					</td>
					<td   colspan="8">&emsp;
					</td>
			</tr>	
			<tr>
				<td colspan="2"></td>
				<td><button type="button" name="Back" onclick="location.href = './Index.jsp?p=<%=p-1%>'">Back</button>
				<button type="button" name="More" onclick="location.href = './Index.jsp?p=<%=p+1%>'">More</button></td>
				</tr>
			</tbody>
		</table>
		</center>
	
	</body>
	
</html>