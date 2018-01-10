package application;

import java.io.IOException;

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
	
	}

	public static void main(String[] args) {
		launch(args);
	}
	public static void showNewMessageWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("View/SendNewMessage.fxml"));
		AnchorPane sendNewMessage = loader.load();
		
		Stage addDialogStage = new Stage();
		addDialogStage.setTitle("New Message");
		addDialogStage.initModality(Modality.WINDOW_MODAL);
		addDialogStage.initOwner(primaryStage);
		Scene scene = new Scene(sendNewMessage);
		addDialogStage.setScene(scene);
		addDialogStage.showAndWait();
	}
}
