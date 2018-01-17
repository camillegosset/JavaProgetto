package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Client.ClientInterface;
import Model.Email;

public interface ServerInterface extends Remote {
	void registerClient(String Clientname, ClientInterface Client) throws RemoteException;
	void unregisterClient(String Clientname) throws RemoteException;
	void sendMessage(Email email, String message) throws RemoteException;
	String getMessage(Integer ID,ClientInterface client) throws RemoteException;
	ArrayList<Email> getEmailList(ClientInterface Client) throws RemoteException;
	void changeOpenedStatus(Integer id) throws RemoteException;
	ArrayList<Email> getSentEmailList(ClientInterface client) throws RemoteException;
	ArrayList<Email> getDeletedEmailList(ClientInterface client) throws RemoteException;
	void deleteMessage(Integer id, String clientName) throws RemoteException;
	}

