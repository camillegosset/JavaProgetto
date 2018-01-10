package Model;

import java.rmi.RemoteException;
import java.util.Observable;

import Client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account extends Observable {
	
private Client client;

ObservableList<Email> messages = FXCollections.observableArrayList();

public Account(Client client) {
	this.client = client;
	
}
public void newMessage() throws RemoteException {
	messages = client.getEmailList();
	setChanged();//
	notifyObservers();
}

	
}
