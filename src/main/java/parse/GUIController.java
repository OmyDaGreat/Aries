package parse;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GUIController {
    @FXML
    private Parent root;
    @FXML
    private MFXTextField userInput;
    @FXML
    private Label messageLabel;
    @FXML
    public MFXButton btn;

    @FXML
    private void handleButtonAction() {
        String input = userInput.getText();
        messageLabel.setText("You entered: " + input);
        logger.info("Free health insurance");
    }
}
