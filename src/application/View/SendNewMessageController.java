package application.View;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import Model.Account;
import Model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SendNewMessageController {

	Account account;
	@FXML
	private TextField topic;
	@FXML
	private TextField receivers;
	@FXML
	private TextArea message;
	
	
	
	



	public void setAccount(Account account) {
		this.account = account;
	}

	public void sendNewMessage() throws RemoteException {
		//verification
		ArrayList<String> receivers = new ArrayList<String>();
		receivers.add(this.receivers.getText());
		String topic = this.topic.getText();
		String sendersName = account.getClientName();
		LocalDate created = LocalDate.now();
		Email newEmail = new Email(topic,sendersName, receivers , created);
		String message = this.message.getText();
		account.sendMessage(newEmail, message);
		//
	}
	
}
