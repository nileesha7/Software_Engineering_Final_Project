
<%@page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  
<html>
<head>
 <style>
        body { background-color: lightgrey};
        * { margin: 0; padding: 0 };
        fieldset { border-color: black; border-style: solid }; 
        
        
        </style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Upload</title>
</head>
<body>
<center>
    <legend><h1>File Upload</h1></legend>
    <form method="post" action="webUI"
        enctype="multipart/form-data">
        Select file to upload: <input type="file" name="file" size="60" /><br />
       
        <br /> <input type="submit" name="chooseFile" value="Choose a file from the server using RMI" />
		
		
	<select name='listSelect'>
<c:forEach items="${item}" var="temp">
    <option value='${temp}'>${temp}</option>
</c:forEach>
</select>
<input type="submit" name="selectFile" value="Merge Files" />
    
    
 	<h2> Notes: </h2>
	    <p> The merged xml file will be saved in the Project3_Servlet Directory in the file output.xml </p>
<p>The JSON file will be saved in the Project3_Servlet Directory in the file JSON.xml</p>
    </form>

    
</center>
</body>
</html>