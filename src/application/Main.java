package application;
 
import java.io.IOException;

import Model.Account;
import Model.Email;
import application.View.MainViewController;
import application.View.ReplyController;
import application.View.SendNewMessageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage primaryStage;
	private static BorderPane mainLayout;
	private static MainViewController mainController;//
	private static String name;
	@Override
	public void start(Stage primaryStage) {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("View/AccountSelection.fxml"));
		try {
			BorderPane firstLayout = loader.load();
			Scene firstScene = new Scene(firstLayout);
			Stage stage = new Stage();
			stage.setScene(firstScene);
			stage.setTitle("Choose an acccount");
			stage.setOnCloseRequest(e -> {
				System.exit(0);
			});
			stage.showAndWait();
		} catch (IOException e2) {
		}
			
		Main.primaryStage = primaryStage;
		Main.primaryStage.setTitle("E-mail service");
		primaryStage.setOnCloseRequest(e -> {
			mainController.onCloseAction();
			return;
		});
		try {
			showMainView();
		} catch (Exception e1) {
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
	
	public static MainViewController getMainController() {
		return mainController;
	}

	public static void setMainController(MainViewController mainController) {
		Main.mainController = mainController;
	}

	private void showMainView() throws Exception {
	FXMLLoader loader = new FXMLLoader();
	loader.setLocation(Main.class.getResource("View/MainView.fxml"));
	mainLayout = loader.load();
	Scene scene = new Scene(mainLayout);
	primaryStage.setScene(scene);
	primaryStage.show();
	mainController = loader.getController();
	mainController.setName(name);
	mainController.initialize2();
	
	}

	public static void main(String[] args) {
		launch(args);
	}
	public static void showNewMessageWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("View/SendNewMessage.fxml"));
		AnchorPane sendNewMessage = loader.load();
		
		
		SendNewMessageController newMessageController = loader.getController();
		
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
	public static void showReplyWindow(int i, Email email) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("View/Reply.fxml"));
		AnchorPane sendNewMessage = loader.load();
		
		
		ReplyController newMessageController = loader.getController();
		
		Account account = mainController.getAccount();
		newMessageController.setAccount(account); 
		account.setType(i);
		newMessageController.setEmail(email);
		Stage addDialogStage = new Stage();
		addDialogStage.setTitle("New Message");
		addDialogStage.initModality(Modality.WINDOW_MODAL);
		addDialogStage.initOwner(primaryStage);
		Scene scene = new Scene(sendNewMessage);
		addDialogStage.setScene(scene);
		
		newMessageController.initializeR();
		addDialogStage.showAndWait();
		

	}
	public static void setName(String login) {
		name = login;
		
	}
}
