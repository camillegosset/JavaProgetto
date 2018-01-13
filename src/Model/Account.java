package Model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;

import Client.Client;
import Server.ServerInterface;
import application.View.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account extends Observable {
	
private Client client;
private String clientName;//
private MainViewController controller;
private int type; //reply =1; reply all =2 forward =3
ObservableList<Email> messages = FXCollections.observableArrayList();

public Account(String clientName, MainViewController controller) throws MalformedURLException, RemoteException, NotBoundException {
	String chatServerURL = "rmi://localhost:5099/RMIChatServer";
	ServerInterface chatServer = (ServerInterface) Naming.lookup(chatServerURL);
	this.client = new Client(clientName, chatServer, this);
	this.clientName = clientName;
	this.controller = controller;
	addObserver(controller);
}


public int getType() {
	return type;
}


public void setType(int type) {
	this.type = type;
}


public Client getClient() {
	return client;
}

public String getClientName() {
	return clientName;
}

public void newMessageArrived() throws RemoteException {
	messages = client.getEmailList();
	setChanged();
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

public ObservableList<Email> getMessages() {
	return messages;
}

public void sendMessage(Email email, String message) throws RemoteException {
	//email.setSender(clientName); DJ fe'
	client.sendMessage(email, message);
}

public void changeOpenedStatus(Integer id) throws RemoteException {
	// TODO Auto-generated method stub
	client.changeOpenedStatus(id);
}

public ObservableList<Email> getSentEmailList() throws RemoteException {
	return client.getSentEmailList();
	
}

public ObservableList<Email> getDeletedEmailList() throws RemoteException {
	return client.getDeletedEmailList();
}

public void deleteMessage(Integer id) throws RemoteException{
	client.deleteMessage(id);	
}

	
}
