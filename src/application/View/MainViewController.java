package application.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import Model.Account;
import Model.Email;
import application.Main;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainViewController implements Observer {

	String name;
	int viewTable = 1;
	Account account;
	@FXML
	Label nameLabel;
	@FXML
	TextArea mailPreview;

	@FXML
	private void showSelectedMessage() {
		Email selectedEmail = tableView.getSelectionModel().getSelectedItem();
		if (selectedEmail != null) {
			/*
			if (selectedEmail.isOpened() == false) {
				account.changeOpenedStatus(selectedEmail.getID());// it does nothing
			}
			*/
			try {
				mailPreview.setText(account.getMessage(selectedEmail.getID()));
			} catch (RemoteException e) {
				serverconnectionerror();
			}

			from.setText(selectedEmail.getSender());
			to.setText(selectedEmail.getStringReceivers());
			topic.setText(selectedEmail.getTopic());
			date.setText(selectedEmail.getDate().toString());
			hour.setText(selectedEmail.getTime().toString());
		}

	}
	@FXML
	private void showSelectedMessage2(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER))
        {
			showSelectedMessage();
        }
	
	}
	
	private void serverconnectionerror() {

		if (account == null) {
			try {
				this.account = new Account(name, this);
			} catch (RemoteException | MalformedURLException | NotBoundException e) {
			}
		}
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Connection error!");
			alert.setHeaderText(null);
			alert.setContentText("No server connection!");
			alert.showAndWait();
		});
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
	private TableColumn<Email, LocalTime> timeColumn;
	// --------reply--Pannel
	@FXML
	private Button reply;
	@FXML
	private Button replyToAll;
	@FXML
	private Button forward;
	// ------------left Pannel---------------
	@FXML
	private Button received;
	@FXML
	private Button sent;
	@FXML
	private Button deleted;
	// ------email--informations---labels
	@FXML
	private Label from = new Label();
	@FXML
	private Label to = new Label();
	@FXML
	private Label topic = new Label();
	@FXML
	private Label date = new Label();
	@FXML
	private Label hour = new Label();

	// -------------------------------------
	@FXML
	private void populateTableView(ActionEvent event) {
		topicColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("Topic"));
		senderColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("Sender"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Email, LocalDate>("Date"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<Email, LocalTime>("Time"));

		if (event == null) {
			switch (viewTable) {
			case 1:
				tableView.setItems(getMessages());
				break;
			case 2:
				tableView.setItems(getSentMessages());
				break;
			case 3:
				tableView.setItems(getDeletedMessages());
				break;
			default:
				tableView.setItems(getDeletedMessages());
				break;
			}
			if (tableView.getSelectionModel() != null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						tableView.requestFocus();
						tableView.getSelectionModel().select(0);
						tableView.getFocusModel().focus(0);
						// () -> {implementation();}
					}
				});
			}
		} else {
			if (event.getSource().equals(received)) {
				tableView.setItems(getMessages());
				viewTable = 1;
			}
			if (event.getSource().equals(sent)) {
				tableView.setItems(getSentMessages());
				viewTable = 2;
			}
			if (event.getSource().equals(deleted)) {
				tableView.setItems(getDeletedMessages());
				viewTable = 3;
			}

		}
	}

	public Account getAccount() {
		return account;
	}

	@FXML
	private ObservableList<Email> getMessages() {
		ObservableList<Email> messages = null;
		try {
			if (account == null) {
				serverconnectionerror();
				return messages;
			}
			messages = account.getEmailList();
			if (messages != null)
				Collections.sort(messages);
		} catch (RemoteException e) {
			serverconnectionerror();
		}
		return messages;

	}

	// -----------new--Message--------------------
	@FXML
	private void newMessageWindow() {
		try {
			Main.showNewMessageWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void replyWindow(ActionEvent event) throws IOException {
		Email email = tableView.getSelectionModel().getSelectedItem();
		if (email == null) {
			return;
		}
		if (event.getSource().equals(reply)) {
			Main.showReplyWindow(1, email);

		}

		if (event.getSource().equals(replyToAll)) {
			Main.showReplyWindow(2, email);
		}

		if (event.getSource().equals(forward)) {
			Main.showReplyWindow(3, email);
		}

	}

	@FXML
	public void onCloseAction() {
		try {
			{
				if (account != null) {
					if (account.getClient() != null)
						account.unregister();
				}
				System.exit(0);

			}
		} catch (Exception e) {//
		}
	}

	public void initialize2() {

		try {
			this.account = new Account(name, this);
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			//serverconnectionerror();
		}
		nameLabel.setText(name + "@mail.com");
		mailPreview.setText("");
		populateTableView(null);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg1 == null) {
			ObservableList<Email> updatedMessages = getupdatedMessages();
			tableView.setItems(updatedMessages);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("New message !");
					alert.setHeaderText(null);
					if(updatedMessages != null && updatedMessages.size()>0) {
					alert.setContentText("You have a message entitled: \n" + updatedMessages.get(0).getTopic() + "\n"
							+ "From " + updatedMessages.get(0).getSender() + ".");
					}
					alert.showAndWait();

				}

			});
		} else {
			viewTable = 2;
			populateTableView(null);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Wrong email address!");
					alert.setHeaderText(null);
					alert.setContentText((String) arg1);
					alert.showAndWait();

				}

			});
		}

	}

	private ObservableList<Email> getupdatedMessages() {
		ObservableList<Email> messages = null;
		try {
		messages = account.getMessages();
		Collections.sort(messages);
		} catch (NullPointerException e) {
			serverconnectionerror();
			return null;
		}
		return messages;
	}

	@FXML
	private ObservableList<Email> getSentMessages() {
		ObservableList<Email> messages = null;
		try {
			if (account == null) {
				serverconnectionerror();
				return null;
			}
			messages = account.getSentEmailList();
			if(messages != null) {
			Collections.sort(messages);
			}
			tableView.setItems(messages);
			
		} catch (RemoteException | IllegalStateException e) {
			serverconnectionerror();
		}

		return messages;
	}

	@SuppressWarnings("finally")
	@FXML
	private ObservableList<Email> getDeletedMessages() {
		ObservableList<Email> messages = null;
		try {
			if (account == null) {
				serverconnectionerror();
				return null;
			}
			messages = account.getDeletedEmailList();
			Collections.sort(messages);
			tableView.setItems(messages);
		} catch (RemoteException e) {
			serverconnectionerror();
		} finally {
			return messages;
		}
	}

	@FXML
	private void deleteMessage() {

		if (tableView.getSelectionModel().getSelectedItem() != null) {
			Email selectedEmail = tableView.getSelectionModel().getSelectedItem();
			if (viewTable != 3) {
				// selectedEmail.setDeleted(LocalDate.now());
				try {
					account.deleteMessage(selectedEmail.getID());
				} catch (NullPointerException | RemoteException e) {
					serverconnectionerror();
				}
				populateTableView(null);// une methode pour supprimer un mail
			} else {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Deleting a deleted message!");
						alert.setHeaderText(null);
						alert.setContentText("You cannot delete a deleted message!");

						alert.showAndWait();

					}

				});
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}
}
