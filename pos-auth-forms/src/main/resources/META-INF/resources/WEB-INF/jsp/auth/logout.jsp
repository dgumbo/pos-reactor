<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logout Test</title>
    </head>
    <body>
        <h1>Spring Security Logout Test!</h1>
        
         <c:url value="/logout" var="logoutProcessingUrl"/>
         
        <form action="${loginProcessingUrl}" method="post">
           <fieldset>
               <legend>Logout</legend>  
               <div>
                   <button type="submit" class="btn">Log Out</button>
               </div>
               <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        
           </fieldset>
        </form>
               
        <p><a href="/home">Home Page</a></p>
    </body>
</html>
