package application.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collections;

import Client.Client;
import Model.Email;
import Server.ServerInterface;
import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainViewController {


	Client client ;
	
	@FXML
	TextArea mailPreview;

	@FXML
	private void showSelectedMessage() throws RemoteException {
		Email selectedEmail = tableView.getSelectionModel().getSelectedItem();
		if (selectedEmail != null) {
		mailPreview.setText(client.getMessage(selectedEmail.getID()));
		}
		// -------naive--version
		//mailPreview.setText(selectedEmail.getContent());
	}

	// -------tableView---------
	@FXML
	private TableView<Email> tableView;
	@FXML
	private TableColumn<Email, String> topicColumn;
	@FXML
	private TableColumn<Email, String> senderColumn;
	@FXML
	private TableColumn<Email, LocalDate> dateColumn;

	@FXML
	private void populateTableView() throws RemoteException {
		topicColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("Topic"));
		senderColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("Sender"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Email, LocalDate>("Date"));
		tableView.setItems(getMessages());
	}

	@FXML
	private ObservableList<Email> getMessages() throws RemoteException {
		ObservableList<Email> messages = client.getEmailList();
		Collections.sort(messages);
		return messages;
	}

	// -----------new--Message--------------------
	@FXML
	private void newMessageWindow() throws IOException {
		Main.showNewMessageWindow();
	}
	
	@FXML
	private void onCloseAction() {
		try {
		{
			client.unregister();
			System.exit(0);
			
		}
	} catch (Exception e) {
		
		e.printStackTrace();
		}
	}

	public void initialize() throws MalformedURLException, RemoteException, NotBoundException {
		String chatServerURL = "rmi://localhost:5099/RMIChatServer";
		ServerInterface chatServer = (ServerInterface) Naming.lookup(chatServerURL);
		this.client = new Client("John", chatServer);
		mailPreview.setText("New Message");
		populateTableView();
	}
}
