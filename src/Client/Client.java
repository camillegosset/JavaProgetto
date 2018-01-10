 package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import Model.Account;
import Model.Email;
import Server.ServerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Client extends UnicastRemoteObject implements ClientInterface {
	HashMap<Integer, Email> emailList = new HashMap<Integer, Email>();
	HashMap<Integer, String> messageList = new HashMap<Integer, String>();
	public static HashMap<String, Client> clientList = new HashMap<String, Client>();
	Account account;
	
	
	private static final long serialVersionUID = 1L;
	private String name;
	private static ServerInterface Server;
	

	public Client(String name, ServerInterface chatServer) throws RemoteException {
	this.setName(name);
	this.setServer(chatServer);
	chatServer.registerClient(name, this);
	clientList.put(name, this);
	}
	public void retrieveMessage() throws RemoteException {
		account.newMessage();	
	}
	// to do
	public String getMessage(Integer ID) throws RemoteException {
		String message = Server.getMessage(ID);
		//System.out.println(ID);
		return message;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ServerInterface getServer() {
		return Server;
	}
	public void setServer(ServerInterface server) {
		Client.Server = server;
	}
	public static Client getClient(String name) {
		System.out.println("Poproszono o klienta: " + name);
		return clientList.get(name);
	}
	public ObservableList<Email> getEmailList() throws RemoteException {
		ObservableList<Email> emailList = FXCollections.observableArrayList();
		emailList.addAll(Server.getEmailList(this));
		return emailList;
	}
	public void unregister() throws RemoteException {
		Server.unregisterClient(name);
		
	}

}
