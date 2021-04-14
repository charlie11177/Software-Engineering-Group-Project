import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
public class JavaFXTest {

    private Scene scene;

    @Start
    public void start(Stage stage) {
        try {
            URL url = new File("src/main/resources/view/MainWindow.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            this.scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.toFront();
        } catch (Exception e) {
            fail("couldn't find fxml file");
        }
    }

    /**
     * Tests that model gets updated when a new airport is added
     * @param robot
     */
    @Test
    public void airportCreationTest(FxRobot robot) {
        String name = "Southampton Airport";
        String code = "SOU";
        robot.clickOn("#airportConfig");
        robot.clickOn("#newAirportButton");
        robot.clickOn("#airportNameTextField").write(name);
        robot.clickOn("#airportCodeTextField").write(code);
        robot.clickOn("#saveAirport");
        Assertions.assertThat(Model.airports.size()).isEqualTo(1);
    }

}
