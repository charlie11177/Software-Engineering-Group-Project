import app.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import model.Model;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

public class JavaFXTest extends App {

    private Scene scene;

    @Override
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

    @Test
    public void testTest(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#airportConfig").queryAs(TitledPane.class).isExpanded());
    }
}
