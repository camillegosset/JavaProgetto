package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import Client.ClientInterface;
import Model.Email;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	private HashMap<String, ClientInterface> onlineClients = new HashMap<String, ClientInterface>();
	private ArrayList<String> clients = new ArrayList<String>();
	private HashMap<Integer, String> messageList = new HashMap<Integer, String>();

	protected Server() throws RemoteException {
		super();
	}

	public synchronized void updateMessageList(Integer ID, String message) {
		messageList.put(ID, message);
	}

	public synchronized void updateClients(ArrayList<String> newClients) {
		clients.addAll(newClients);
	}

	public boolean egsistsLogin(String name) {
		if (clients.contains(name)) {
			return true;
		} else {
			return InputMethods.egsistsLogin(name, this);
		}
	}

	public synchronized void registerClient(String clientname, ClientInterface client) throws RemoteException {
		if (egsistsLogin(clientname)) {
			synchronized (onlineClients) {
				this.onlineClients.put(clientname, client);
				System.out.println(clientname + " is logged in succesfully!");
			}
		} else {
			System.out.println("Non esisti!!!!");
			// raiseError!!!
		}

	}
	/*
	 * public synchronized void broadcastMessage(String message) throws
	 * RemoteException { for(ClientInterface client : onlineClients.values()) {
	 * client.retrieveMessage(message); } }
	 */

	@Override
	public synchronized void sendMessage(Email email, String message) throws RemoteException {
		Integer ID;
		email.setID(ID = getNewID());
		// System.out.println(email); null pointer
		new Thread(new Runnable() {

			@Override
			public void run() {
				OutputMethods.addEmail(email);
			}
			
		});
		
		OutputMethods.writeMessage(message, ID); // ID.txt


		for (int j = 0; j < email.getReceivers().size(); j++) {
			if (onlineClients.containsKey(email.getReceivers().get(j))) {
				onlineClients.get(email.getReceivers().get(j)).retrieveMessage();
			}
		}
	}

	private Integer getNewID() {
		Integer id = InputMethods.getID();
		OutputMethods.incrementID();
		return id;
	}

	@Override
	public String getMessage(Integer ID) throws RemoteException {
		if (messageList.containsKey(ID)) {
			return messageList.get(ID);
		} else {
			String l = new String();
			synchronized (messageList) {
				messageList.put(ID, l = InputMethods.getMessage(ID));
			}
			return l;
		}
	}

	@Override
	public ArrayList<Email> getEmailList(ClientInterface client) throws RemoteException {
		return InputMethods.getEmailList(client.getName(),0);
	}
	
	@Override
	public ArrayList<Email> getSentEmailList(ClientInterface client) throws RemoteException {
		return InputMethods.getEmailList(client.getName(),1);
	}
	@Override
	public ArrayList<Email> getDeletedEmailList(ClientInterface client) throws RemoteException {
		return InputMethods.getEmailList(client.getName(),2);
	}
	@Override
	public void unregisterClient(String clientName) throws RemoteException {
		this.onlineClients.remove(clientName);

	}

	@Override
	public void changeOpenedStatus(Integer id) throws RemoteException {
		OutputMethods.changeOpenedStatus(id);
	}

	@Override
	public void deleteMessage(Integer id, String clientName) throws RemoteException {
		OutputMethods.removeEmail(id, clientName);
		
	}

	


}
