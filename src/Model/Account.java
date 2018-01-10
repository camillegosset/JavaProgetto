package Model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;

import Client.Client;
import Server.ServerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account extends Observable {
	
private Client client;
private String clientName;

ObservableList<Email> messages = FXCollections.observableArrayList();

public Account(String clientName) throws MalformedURLException, RemoteException, NotBoundException {
	String chatServerURL = "rmi://localhost:5099/RMIChatServer";
	ServerInterface chatServer = (ServerInterface) Naming.lookup(chatServerURL);
	this.client = new Client(clientName, chatServer);
	this.clientName = clientName;
	
}

public Client getClient() {
	return client;
}

public String getClientName() {
	return clientName;
}

public void newMessageArrived() throws RemoteException {
	messages = client.getEmailList();
	setChanged();//
	notifyObservers();
}
public String getMessage(Integer id) throws RemoteException {
	return client.getMessage(id);
}
public ObservableList<Email> getEmailList() throws RemoteException {
	return client.getEmailList();
}
public void unregister() throws RemoteException {
	client.unregister();
	
}

public void sendMessage(Email email, String message) throws RemoteException {
	email.setSender(clientName);
	client.sendMessage(email, message);
}

	
}
