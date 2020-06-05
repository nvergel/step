<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="javax.servlet.annotation.WebServlet" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Nicolas Perez Vergel</title>
    <link rel="stylesheet" href="style.css">
  </head>
  <body>

    <% 
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
    %>
      <h2>Set Nickname</h2>
      <p>Set your nickname here:</p>
      <form method="POST" action="/nickname">
        <input name="nickname"/>
        <br/>
        <button>Submit</button>
      </form>
    <% } else {%>
      <h2> Login in to chose a Nickname </h2>
    <% } %>

</body>
</html>