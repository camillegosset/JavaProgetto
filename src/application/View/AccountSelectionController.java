package application.View;

import java.util.Observable;
import java.util.Observer;

import Model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AccountSelectionController implements Observer{
	
	//private MainViewController mainController;
	private String loginUser;
	
	// Lista di login possibile:
	ObservableList<String> loginList = FXCollections.observableArrayList("Jane","John", "Matteo");
	@FXML
	private ComboBox<String> userChoice;
	

	@FXML
	private void initialize() {
		userChoice.setValue("Jane");
		userChoice.setItems(loginList);
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	public String getUserChoice() {
		return userChoice.getValue();
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	
	public void pressBoutonOK() {
		String login= getUserChoice();
		setLoginUser(login);
	}
	
	
	/*public Account creareAccount() {
		String name= getUserChoice();
		return new Account(name, mainController);
	}*/

}
