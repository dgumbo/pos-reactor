<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>Username : <b><sec:authentication property="principal"></sec:authentication></b></div>
        
        <p>URI: ${uri} <br/>
        User :  ${user} <br/>
        roles:  ${roles} <br/><br/>
        </p> 
        
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <p><a href="/admin" >Admin Page Roles</a></p>
        </sec:authorize>
        
        <sec:authorize access="hasAuthority('ADMIN')">
            <p><a href="/admin" >Admin Page Authorities</a></p>
        </sec:authorize>
    
        <p>&nbsp;</p>
        <p><a href="/token/test" >Token Route Get Users Test</a></p>
        <p><a href="/token/generate" >Token Generate Post Test</a></p>
        <p>&nbsp;</p>
        <p><a href="/rest/users" >Rest Get All Users Test</a></p>
        <p><a href="/rest/username/admin" >Rest Get Admin User Test</a></p>
        <p>&nbsp;</p>
        <p><a href="/t" >T Page</a></p>
        <p><a href="/index.html" >Test HiS Page 1</a></p>
        <p><a href="/" >Test HiS Page 2</a></p>
        <p>&nbsp;</p>
        <p><a href="/logout" >Logout Page</a></p>  
        <p><a href="/login" >Login Page</a></p> 
    </body>
</html>
