package application.View;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import Model.Account;
import Model.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class SendNewMessageController {
	Account account;
	@FXML
	protected TextField topic;
	@FXML
	protected TextField receivers;
	@FXML
	protected TextArea message;

	@FXML
	protected Button closeButton;
	
	@FXML
	protected void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
	
	 public boolean isInvalidEmailAddress(String email) {
         String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@mail.com$";
         java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
         java.util.regex.Matcher m = p.matcher(email);
         return !m.matches();
  }
	
	public void sendNewMessage() throws RemoteException {
		if(this.receivers.getText().isEmpty()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No receiver!");
					alert.setHeaderText(null);
					alert.setContentText("You have to write at leat one receiver!");
					alert.showAndWait();
				}
			});
			return;
		}
		
		if(this.topic.getText().contains("#")) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Wrong topic format!");
					alert.setHeaderText(null);
					alert.setContentText("The topic cannot contain '#' !");
					alert.showAndWait();
				}
			});
			return;
		}
		
		ArrayList<String> receivers = new ArrayList<String>();
		receivers.addAll(Email.parseReceivers(this.receivers.getText())); // Spero che funzioni
		
		for(String name : receivers) {
			if(isInvalidEmailAddress(name)) {
				Platform.runLater( () -> {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Wrong receiver!");
						alert.setHeaderText(null);
						alert.setContentText("The name " +  name + " is incorrect!");
						alert.showAndWait();
					}
				);
				return;
			}
		}
		
	
		
		 receivers.forEach( name -> {
			receivers.set(receivers.indexOf(name), name.substring(0, name.indexOf("@")));
		}
		 );
		
		 if(topic.getText().isEmpty()) topic.setText("(no topic)");
		 

		 
		Email newEmail = new Email(topic.getText(), account.getClientName(), receivers, LocalDate.now(),
				LocalTime.now());
		newEmail.setExisting(true);
		newEmail.setOpened(false);
		
		if(message.getText().isEmpty()) message.setText("(no message)");
		String message = this.message.getText();

		if(	account.sendMessage(newEmail, message)) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
		} else {
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Connection error!");
				alert.setHeaderText(null);
				alert.setContentText("No server connection!");
				alert.showAndWait();
			}
		);
			
		}
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
}