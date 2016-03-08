
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.transform.stream.StreamResult;

 
@WebServlet("/webUI")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
                 maxFileSize=1024*1024*50,      	// 50 MB
                 maxRequestSize=1024*1024*100)   	// 100 MB

public class webUI extends HttpServlet {
 
    private static final long serialVersionUID = 205242440643911308L;
	
    /**
     * Directory where uploaded files will be saved, its relative to
     * the web application directory.
     */
    private static final String UPLOAD_DIR = "uploads";
  
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
    	String hostIP;
    	String directory;
    	String remoteFile;
    	int port=1688;
    	String localFile="localFile.txt";
    	
    	//transfers file names from server to client
    	if (request.getParameter("chooseFile") != null) {
    		
    		hostIP="149.4.223.237";
    	
    		directory="C:\\Users\\BonSyAdmin\\Desktop\\CS370\\Nileesha_David_Nick";

    		RMI_BioAPI_AsteriskJava_Client client = new 

            		RMI_BioAPI_AsteriskJava_Client(hostIP, "my_transact", "N/A", hostIP, 0, 

            		"N/A");
            		    		
    		File []fileNames= client.getFileNamesArray(directory);
    		
    		
    		 request.setAttribute("item", fileNames);
    	     request.getRequestDispatcher("upload.jsp").forward(request, response);

    	 }
    	 
    	 if(request.getParameter("selectFile")!=null){
    		 hostIP="149.4.223.237";
    		 port=1688;
    		 remoteFile=(String)request.getParameter("listSelect");
    		 localFile="localFile.xml";
    		//reads the file from the remote location and saves it in localFile.txt
			RMI_BioAPI_Demo demo = new RMI_BioAPI_Demo(localFile, port, hostIP, "my_transact", remoteFile);
			
			 // gets absolute path of the web application
	        String applicationPath = request.getServletContext().getRealPath("");
	        // constructs path of the directory to save uploaded file
	        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
	         
	        // creates the save directory if it does not exists
	        File fileSaveDir = new File(uploadFilePath);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdirs();
	        }
	        System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());
	        
	        String fileName = "";
	        //Get all the parts from request and write it to the file on server
	        for (Part part : request.getParts()) {
	            fileName = getFileName(part);
	            
	            String[] parseFileName = fileName.split("\\\\");  //for Windows system to get the filename without the path information
	            fileName = parseFileName[parseFileName.length - 1];
	            
	            part.write(uploadFilePath + File.separator + fileName);
	        }

		    try {
		    	String srcFile = uploadFilePath + File.separator + fileName;
		    	System.out.println(srcFile);
		    	mergeFiles(srcFile, localFile);
	    	    request.getRequestDispatcher("upload.jsp").forward(request, response);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	    }
	 
    	 }
    	 

	private void mergeFiles(String uploadFile, String localFile) {
		try {
			StreamResult xml =ReadXMLFile.mergeFiles(uploadFile, localFile);
			FileWriter output= new FileWriter ("JSON.xml");
			String json=ReadXMLFile.getJSON(uploadFile, localFile);
	    	System.out.println(json);
	    	output.write(json);
	    	output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	 private String getFileName(Part part) {
	        String contentDisp = part.getHeader("content-disposition");
	        System.out.println("content-disposition header= "+contentDisp);
	        String[] tokens = contentDisp.split(";");
	        for (String token : tokens) {
	            if (token.trim().startsWith("filename")) {
	                return token.substring(token.indexOf("=") + 2, token.length()-1);
	            }
	        }
	        return "";
	    }
 
}

