package parse;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GUIController {

    @FXML
    private TextField userInput;
    @FXML
    private Label messageLabel;
    @FXML
    private Parent root;
    @FXML
    public Button btn;

    @FXML
    private void handleButtonAction() {
        String input = userInput.getText();
        messageLabel.setText("You entered: " + input);
        logger.info("Free health insurance");
    }
}
