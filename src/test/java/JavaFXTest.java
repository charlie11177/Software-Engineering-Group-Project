import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.fail;
import org.testfx.util.WaitForAsyncUtils;

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
     * Simulates the scenario 1 procedure
     * @param robot
     */
    @Test
    public void scenarioOneTest(FxRobot robot) throws InterruptedException {
        Model.demo();
        String name = "Southampton Airport";
        String code = "SOU";
        robot.clickOn("#airportConfig");
        // Wait for pane to be expanded
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#airportChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#runwayChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#editRunway");
        robot.clickOn("#leftDegreeTF").press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE).write("10");
        robot.clickOn("#saveRunway");
        robot.clickOn("#obstacleConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#obstacleChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#placeObstacleCB");
        robot.clickOn("#calculateButton");

        // I don't know if this assertion should be hardcoded like this but it will do for now
        FxAssert.verifyThat(robot.lookup("#topTableView").queryAs(TableView.class), TableViewMatchers.containsRow("10R", 3660, 3660, 3353, 3660, 307));
    }


}
