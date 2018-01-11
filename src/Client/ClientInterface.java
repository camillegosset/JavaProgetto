package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	void retrieveMessage() throws RemoteException;
	String getName() throws RemoteException;
}
