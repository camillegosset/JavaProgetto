package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import Client.ClientInterface;
import Model.Email;
import Server.View.ServerViewController;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	private HashMap<String, ClientInterface> onlineClients = new HashMap<String, ClientInterface>();
	private ArrayList<String> clients = new ArrayList<String>();
	private HashMap<Integer, String> messageList = new HashMap<Integer, String>();
	private ServerViewController viewController;
	
	protected Server() throws RemoteException {
		super();
	}
	
	public void setViewController(ServerViewController viewController) {
		this.viewController = viewController;
	}


	public void updateMessageList(Integer ID, String message) {
		messageList.put(ID, message);
	}
	
	public void informAboutMistakenName(Email email, ArrayList<String> mistakenReceiversList) {
		
		if (onlineClients.containsKey(email.getSender())) { //se  stiamo ancora on-line
			try {
				onlineClients.get(email.getSender()).informAboutMistakenName(email.getTopic(), mistakenReceiversList);
			} catch (RemoteException e) {
			}
			}
		viewController.write(email.getSender() + " attempting to write a message to a non-existent client(s): " + mistakenReceiversList);
		
	}

	public void updateClients(ArrayList<String> newClients) {
		synchronized(clients) {
		clients.addAll(newClients);
		}
	}

	public boolean egsistsLogin(String name) {
		if (clients.contains(name)) {
			return true;
		} else {
			return InputMethods.egsistsLogin(name, this);
		}
	}

	public void registerClient(String clientname, ClientInterface client) {
		if (egsistsLogin(clientname)) {
			synchronized (onlineClients) {
				this.onlineClients.put(clientname, client);
				viewController.write(clientname + " is logged in succesfully!");
			}
		} else {
			viewController.write(clientname + " logging attempt failed: no such a name.");
		}

	}
	
	@Override
	public void sendMessage(Email email, String message)  {
		
		viewController.write(email.getSender() + " tries to send n e-mail to:" +  Email.getStringReceivers(email.getReceivers()));
		
		Integer ID;
		email.setID(ID = getNewID());
		
		
		
		Thread addingEmail = new Thread(() -> {
			OutputMethods.addEmail(email, this);
		});
		addingEmail.start();
		
		Thread addingMessage = new Thread(() -> {
			OutputMethods.writeMessage(message, ID);
		}); 
		addingMessage.start();

		try {
			addingEmail.join();
			addingMessage.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		new Thread(() -> {
			for (String person : email.getReceivers() ) {
				if (onlineClients.containsKey(person)) {
					try {
						onlineClients.get(person).retrieveMessage();
						viewController.write(person + " receives a message from: " + email.getSender());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private String objectID = "ID";
	
	private Integer getNewID() {
		Integer id;
		synchronized(objectID) {
		id = InputMethods.getID();
		OutputMethods.incrementID();
		}
		return id;
	}

	@Override
	public String getMessage(Integer ID, ClientInterface client) {
		try {
			viewController.write(client.getName() + " retrieves content of a message nr: " + ID);
		} catch (RemoteException e) {
		}
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
	public ArrayList<Email> getEmailList(ClientInterface client)  {
		try {
			viewController.write(client.getName() + " retrieves received messages list.");
			return InputMethods.getEmailList(client.getName(),0);
		} catch (RemoteException e) {
			return null;
		}
	
	}
	
	@Override
	public ArrayList<Email> getSentEmailList(ClientInterface client) {
		try {
		viewController.write(client.getName() + " retrieves sent messages list.");
		return InputMethods.getEmailList(client.getName(),1);
		} catch (RemoteException e) {
			return null;
		}
	}
	@Override
	public ArrayList<Email> getDeletedEmailList(ClientInterface client) {
		try {
		viewController.write(client.getName() + " retrieves deleted messages list.");
		return InputMethods.getEmailList(client.getName(),2);
		} catch (RemoteException e) {
			return null;
		}
		
	}
	@Override
	public void unregisterClient(String clientName) {
		this.onlineClients.remove(clientName);
		viewController.write(clientName + " unregistered.");

	}

	@Override
	public void changeOpenedStatus(Integer id)  {
		OutputMethods.changeOpenedStatus(id);
	}

	@Override
	public void deleteMessage(Integer id, String clientName)  {
		OutputMethods.removeEmail(id, clientName);
		viewController.write(clientName + " deletes a message nr:" + id);
		
	}

	@Override
	public void isServerOn() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	


}
