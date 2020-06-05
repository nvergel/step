<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

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