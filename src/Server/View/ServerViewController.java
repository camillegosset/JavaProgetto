package Server.View;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ServerViewController {

	@FXML
	TextArea textArea;
	
	@FXML
	private void initialize() {
		textArea.setStyle("-fx-text-fill: black; ");
		textArea.setText("Connection set: " + LocalTime.now());
	}
	public void write(String text) {
		textArea.appendText("\n >"+ LocalDate.now().toString() + "  " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + " "+ text);
	}
}
