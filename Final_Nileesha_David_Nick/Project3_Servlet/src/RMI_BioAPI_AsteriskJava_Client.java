import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;


/**
 * @author bon
 *
 */
public class RMI_BioAPI_AsteriskJava_Client {
	
	File [] fileNamesArray;
	RMI_BioAPI_AsteriskJava_Interface service;
	String host_ip;
	String Service_UID;
	String srcFileName;
	String socket_ip;
	int socket_port;
	String remote_fileName;
	
    public RMI_BioAPI_AsteriskJava_Client(String host_ip, String Service_UID, String srcFileName, String socket_ip, int socket_port, String remote_fileName)
    {
    	this.host_ip=host_ip;
    	this.Service_UID=Service_UID;
    	this.srcFileName=srcFileName;
    	this.socket_ip=socket_ip;
    	this.socket_port=socket_port;
    	this.remote_fileName=remote_fileName;
    	
    	try {
    		service = (RMI_BioAPI_AsteriskJava_Interface) 
			Naming.lookup("rmi://" + host_ip+ "/RMI_BioAPI_AsteriskJava");
    		

    	} catch (Exception e) {
            e.printStackTrace();
    		System.out.println("RMI_BioAPI_AsteriskJava Naming lookup fails!");
    	}
    }
    
    public void fileRead(){
    	try {
			service.RPC_FileRead(Service_UID, srcFileName, socket_ip, socket_port, remote_fileName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    public File[] getFileNamesArray(String directory) throws RemoteException{
    	fileNamesArray=service.getFiles(directory);
    	return fileNamesArray;
    }
    
}
