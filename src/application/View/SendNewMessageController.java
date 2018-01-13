package application.View;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import Model.Account;
import Model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendNewMessageController {

	Account account;
	@FXML
	private TextField topic;
	@FXML
	private TextField receivers;
	@FXML
	private TextArea message;

	@FXML
	private Button closeButton;

	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void sendNewMessage() throws RemoteException {
		// verification();
		ArrayList<String> receivers = new ArrayList<String>();
		receivers.addAll(Email.parseReceivers(this.receivers.getText())); // Spero che funzioni
		Email newEmail = new Email(topic.getText(), account.getClientName(), receivers, LocalDate.now(),
				LocalTime.now());
		newEmail.setExisting(true);
		newEmail.setOpened(false);
		String message = this.message.getText();

		account.sendMessage(newEmail, message);

		// closing the window
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	public ArrayList<String> parseReceivers(String string) {
		// TO DO
		return null;

	}
}
