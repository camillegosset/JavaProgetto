package Server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.Month;
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
	
	@SuppressWarnings("unlikely-arg-type")
	//to be changed totally!!!
	@Override
	public synchronized void sendMessage(Email email, String message) throws RemoteException {
		Integer ID;
		email.setID(ID = getNewID());
		OutputMethods.addEmail(email);
		OutputMethods.writeMessage(message, ID);  // ID.txt
		
		
		//onlineClients.get(email.getReceivers()).retrieveMessage();
		
	}
	private Integer getNewID() {
		InputMethods.getNewID();
		OutputMethods.incrementID();
		return null;
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
	public ArrayList<Email> getEmailList(ClientInterface Client) {
		 
		//------------temporary--------
		final Email email1 = new Email("Meeting","john@mail.com",new ArrayList<String>(), LocalDate.of(2014, Month.MAY, 21),1);
		final Email email2 = new Email("Pieczenie pierników","piotr@mail.com",new ArrayList<String>(), LocalDate.of(1952, Month.OCTOBER, 21),2);
		final Email email3 = new Email("Zakupy","john@mail.com", new ArrayList<String>(), LocalDate.of(2017, Month.JANUARY, 21),3);
		
		ArrayList<Email> emailList = new ArrayList<Email>();
		emailList.add(email1);
		emailList.add(email2);
		emailList.add(email3);

		
		
		//---------non puo restituire Email perche email contiene SimpleStringProperty che non e serializzabile-----------
		return emailList;
	}

	@Override
	public void unregisterClient(String Clientname) throws RemoteException {
		this.onlineClients.remove(Clientname);
		
	}
}
