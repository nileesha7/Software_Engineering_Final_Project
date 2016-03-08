import java.io.File;
import java.rmi.RemoteException;

/**
 * 
 */

/**
 * @author bon
 *
 */
public interface RMI_BioAPI_AsteriskJava_Interface extends java.rmi.Remote {

	public void RPC_FileRead(String Service_UID, String srcFileName, String socket_ip, int socket_port, String remote_fileName) throws RemoteException;

	//public String[] getFileList() throws RemoteException;
	
	public File [] getFiles(String directory) throws RemoteException;

}