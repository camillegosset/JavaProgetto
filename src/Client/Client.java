package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
	private final static String chatServerURL = "rmi://localhost:5099/RMIChatServer";

	private static final long serialVersionUID = 1L;
	private String name;
	private static ServerInterface Server;

	public Client(String name, Account account) throws RemoteException, MalformedURLException, NotBoundException {
		ServerInterface chatServer = (ServerInterface) Naming.lookup(chatServerURL);
		this.setName(name);
		this.setServer(chatServer);
		chatServer.registerClient(name, this);
		clientList.put(name, this);
		this.account = account;
	}

	public boolean sendMessage(Email email, String message) {
		boolean result = true;
		try {
			Server.sendMessage(email, message);
		} catch (RemoteException e) {	
			if(reconnect()) {
				return sendMessage(email, message);
			}
			result = false;
		}
		return result;
	}

	private boolean reconnect() {

					try {
						setServer((ServerInterface) Naming.lookup(chatServerURL));
						Server.registerClient(name, this);
						return true;
					} catch (RemoteException | MalformedURLException | NotBoundException e) {
						return false;
					}
	}

	public void retrieveMessage() throws RemoteException {
		account.newMessageArrived();
	}

	public String getMessage(Integer ID) throws RemoteException {
		String message = Server.getMessage(ID, this);
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

	public void changeOpenedStatus(Integer id) throws RemoteException {
		Server.changeOpenedStatus(id);
	}

	public ObservableList<Email> getSentEmailList() throws RemoteException {
		ObservableList<Email> sentEmailList = FXCollections.observableArrayList();
		sentEmailList.addAll(Server.getSentEmailList(this));
		return sentEmailList;
	}

	public ObservableList<Email> getDeletedEmailList() throws RemoteException {
		ObservableList<Email> sentEmailList = FXCollections.observableArrayList();
		sentEmailList.addAll(Server.getDeletedEmailList(this));
		return sentEmailList;
	}

	public void deleteMessage(Integer id) throws RemoteException {
		Server.deleteMessage(id, name);
	}

	@Override
	public void informAboutMistakenName(String topic, ArrayList<String> mistakenReceiversList) throws RemoteException {
		account.informAboutMistakenName(topic, mistakenReceiversList);

	}

}
