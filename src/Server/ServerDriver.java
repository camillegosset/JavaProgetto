package Server;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//
public class ServerDriver {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Registry registry = LocateRegistry.createRegistry(5099);
		registry.rebind("RMIChatServer",new Server()); 
	}
}
