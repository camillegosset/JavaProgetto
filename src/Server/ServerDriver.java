package Server;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Server.View.ServerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
//
public class ServerDriver extends Application {
	public static Server server;
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Registry registry = LocateRegistry.createRegistry(5099);
		registry.rebind("RMIChatServer",server = new Server());
		launch(args);
	}//

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("E-mail Server");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ServerDriver.class.getResource("View/ServerView.fxml"));
		BorderPane mainLayout = loader.load();
		ServerViewController viewController = loader.getController();
		server.setViewController(viewController);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		primaryStage.show();
		
	}
}
