package application.View;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountSelectionController {
	
	@FXML
	private Label label;
	ObservableList<String> loginList = FXCollections.observableArrayList("Jane","John", "Matteo");
	@FXML
	private ComboBox<String> userChoice;
	

	@FXML
	private void initialize() {
		userChoice.setValue("Jane");
		userChoice.setItems(loginList);
	}
	
	public String getUserChoice() {
		return userChoice.getValue();
	}
	//public String getLoginUser() {
	//	return loginUser;
	//}
	//public void setLoginUser(String loginUser) {
	//	this.loginUser = loginUser;
	//}
	
	public void pressBoutonOK(ActionEvent event) {
		String login= getUserChoice();
		Main.setName(login);
		Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		stage.close();
		
	}
	
	
	/*public Account creareAccount() {
		String name= getUserChoice();
		return new Account(name, mainController);
	}*/

}
