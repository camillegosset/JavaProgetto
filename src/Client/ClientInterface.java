package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
	void retrieveMessage() throws RemoteException;
	String getName() throws RemoteException;//
	void informAboutMistakenName(String topic, ArrayList<String> mistakenReceiversList) throws RemoteException;
}
