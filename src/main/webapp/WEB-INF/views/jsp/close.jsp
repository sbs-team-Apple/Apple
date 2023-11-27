<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Simple JSP Example</title>
</head>
<body>
    <h1>Hello, JSP!</h1>
    <%  // Embedded Java code
        String name = "Guest";
        if(request.getParameter("name") != null) {
            name = request.getParameter("name");
        }
    %>
    <p>Welcome, <%= name %>!</p> <!-- Using scriptlet to output the variable 'name' -->
</body>
</html>