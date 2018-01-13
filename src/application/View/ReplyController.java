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

public class ReplyController {

	//MainViewController mainViewController;
	Account account;
	@FXML
	private TextField topic;
	@FXML
	private TextField receivers;
	@FXML
	private TextArea message;
	
	private Email email;
	
	private int type;
	@FXML
	private Button closeButton;

	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	
	public String getReceiversReplyAll() {
		ArrayList<String> listReceivers= email.getReceivers();
		System.out.println(listReceivers.size());
		String stringReceivers= "";
		for(String r: listReceivers) {
			System.out.println(r);
			if(!r.equals(email.getSender()) && !r.equals(account.getClient().getName())) {
				stringReceivers= stringReceivers + "; " + r;
			}
		}
		return stringReceivers;
	}
	public void initializeR() throws RemoteException {
		System.out.println("Three and a half");
		type = account.getType();
		switch(type) {
		case 1:  //reply
			
			topic.setText("Re: " + email.getTopic());
			receivers.setText(email.getSender());
			message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
			break;
		
	case 2:  //replyAll
		
		topic.setText("Re: " + email.getTopic());
		//email.getReceivers().remove(email.getSender());
		//receivers.setText(email.getSender() +"; " + email.getReceivers().toString().replaceAll(",", ";"));
		receivers.setText(email.getSender() + getReceiversReplyAll());
		message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
		break;
	case 3:  //forward
		
		topic.setText("Fwd: " + email.getTopic());
		message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
		break;
	}
	
}
	public ArrayList<String> parseReceivers(String string) {
		// TO DO
		return null;

	}
}
