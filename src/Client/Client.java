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
	public static HashMap<String, Client> clientList = new HashMap<String, Client>();
	private Account account;
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
			if (reconnect()) {
				return sendMessage(email, message);
			}
			result = false;
		}
		return result;
	}

	private boolean reconnect() {

		/*if(Server == null) {
			Main.getMainController().initialize2();
		}
		*/
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

		String message;
		try {
			message = Server.getMessage(ID, this);
		} catch (RemoteException e) {
			if (reconnect()) {
					message = Server.getMessage(ID, this);
			} else {
				return "We coudn't getthe message - server connection problem.";
			}

		}
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

	public ObservableList<Email> getEmailList() throws RemoteException  {
		ObservableList<Email> emailList = FXCollections.observableArrayList();
		try {
			emailList.addAll(Server.getEmailList(this));
		} catch (RemoteException e) {
			if(reconnect()) {
					emailList.addAll(Server.getEmailList(this));
			} else {
				throw new RemoteException();
			}
		}
		return emailList;
	}

	public void unregister() throws RemoteException  {
		try {
			Server.unregisterClient(name);
		} catch (RemoteException e) {
			if(reconnect()) {
					Server.unregisterClient(name);
			}
		}

	}

	/*public void changeOpenedStatus(Integer id)  {
		try {
			Server.changeOpenedStatus(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	*/

	public ObservableList<Email> getSentEmailList() throws RemoteException {
		ObservableList<Email> sentEmailList = FXCollections.observableArrayList();
		try {
			sentEmailList.addAll(Server.getSentEmailList(this));
		} catch (RemoteException e) {
			if(reconnect()) {
				//System.out.println("Reconnected succefully!");
			sentEmailList.addAll(Server.getSentEmailList(this));
			} else {
				throw new RemoteException();
			}
		}
		return sentEmailList;
	}

	public ObservableList<Email> getDeletedEmailList() throws RemoteException  {
		ObservableList<Email> sentEmailList = FXCollections.observableArrayList();
		try {
			sentEmailList.addAll(Server.getDeletedEmailList(this));
		} catch (RemoteException e) {
			if(reconnect()) {
					sentEmailList.addAll(Server.getDeletedEmailList(this));
			} else {
				throw new RemoteException();
			}
		}
		return sentEmailList;
	}

	public void deleteMessage(Integer id) throws RemoteException {
		try {
			Server.deleteMessage(id, name);
		} catch (RemoteException e) {
			if(reconnect()) {
					Server.deleteMessage(id, name);
			} else {
				throw new RemoteException();
			}
		}
	}

	@Override
	public void informAboutMistakenName(String topic, ArrayList<String> mistakenReceiversList)  {
		account.informAboutMistakenName(topic, mistakenReceiversList);

	}

	public static boolean isServerOn() {
		try {
		Server.isServerOn();
		return true;
		} catch (Exception e) {
		return false;
		}
	}

}
