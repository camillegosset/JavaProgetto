package Server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import Client.ClientInterface;
import Model.Email;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	private HashMap<String,ClientInterface> onlineClients= new HashMap<String, ClientInterface>();
	private ArrayList<String> clients = new ArrayList<String>();
	private HashMap<Integer, String> messageList = new HashMap<Integer, String>();
	
	
	protected Server() throws RemoteException {
		super();
	}
	
	public synchronized void updateMessageList(Integer ID,String message) {
		messageList.put(ID, message);
		}
	
	public synchronized void updateClients(ArrayList<String> newClients) {
	clients.addAll(newClients);
	}
	
	public boolean egsistsLogin(String name){
		if(clients.contains(name)) {return true;}
		else {return InputMethods.egsistsLogin(name,this);}
		}
	
	public synchronized void registerClient(String clientname, ClientInterface client) throws RemoteException {
		if(egsistsLogin(clientname)) {
			synchronized (onlineClients){
		this.onlineClients.put(clientname , client);
		}
		} else {
			//raiseError!!!
		}
		
	}
	/*
	public synchronized void broadcastMessage(String message) throws RemoteException {
		for(ClientInterface client : onlineClients.values()) {
			client.retrieveMessage(message);
		}
	}
	*/
	
	
	@Override
	public synchronized void sendMessage(Email email, String message) throws RemoteException {
		Integer ID;
		email.setID(ID = getNewID());
		//System.out.println(email); null pointer
		OutputMethods.addEmail(email);
		OutputMethods.writeMessage(message, ID);  // ID.txt
		
		////System.out.println(email.getReceivers().get(0));
		if(onlineClients.containsKey(email.getReceivers().get(0))) {
		onlineClients.get(email.getReceivers().get(0)).retrieveMessage();
		}
	}
	private Integer getNewID() {
		Integer id = InputMethods.getID();
		OutputMethods.incrementID();
		return id;
	}

	@Override
	public String getMessage(Integer ID) throws RemoteException {
		if(messageList.containsKey(ID)) {
		return messageList.get(ID);
		} else {
			String l = new String();
			synchronized (messageList){
			messageList.put(ID, l = InputMethods.getMessage(ID));
			}
			return l;
		}
	}

	@Override
	public ArrayList<Email> getEmailList(ClientInterface Client) throws RemoteException {
		return InputMethods.getEmailList(Client.getName());
	}

	@Override
	public void unregisterClient(String Clientname) throws RemoteException {
		this.onlineClients.remove(Clientname);
		
	}
}
