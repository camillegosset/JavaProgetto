package application;

import java.io.IOException;

import Model.Account;
import application.View.MainViewController;
import application.View.SendNewMessageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage primaryStage;
	private static BorderPane mainLayout;
	@SuppressWarnings("unused")
	private static MainViewController mainController;// = new MainViewController(); //da cambiare non dev'essere staatico!!
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Main.primaryStage = primaryStage;
		Main.primaryStage.setTitle("E-mail service");
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("Exit handler");
			return;
		});
		showMainView();
	}
	private void showMainView() throws IOException {
	FXMLLoader loader = new FXMLLoader();
	loader.setLocation(Main.class.getResource("View/MainView.fxml"));
	mainLayout = loader.load();
	Scene scene = new Scene(mainLayout);
	primaryStage.setScene(scene);
	primaryStage.show();
	mainController = loader.getController();
	
	}

	public static void main(String[] args) {
		launch(args);
	}
	public static void showNewMessageWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("View/SendNewMessage.fxml"));
		AnchorPane sendNewMessage = loader.load();
		
		
		SendNewMessageController newMessageController = loader.getController();
		if(mainController == null) {
			System.out.println("Boo");
		}
		Account account = mainController.getAccount();
		newMessageController.setAccount(account); 
		
		
		Stage addDialogStage = new Stage();
		addDialogStage.setTitle("New Message");
		addDialogStage.initModality(Modality.WINDOW_MODAL);
		addDialogStage.initOwner(primaryStage);
		Scene scene = new Scene(sendNewMessage);
		addDialogStage.setScene(scene);
		addDialogStage.showAndWait();
		
		
	}
}
