import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
public class ScenarioTests {

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

    @BeforeEach
    public void cleanUp() {
        Model.currentAirport = null;
        Model.airports.clear();
        Model.currentRunway = null;
        Model.currentObstacle = null;
        Model.obstacles.clear();
        Demo.setup();
    }

    /**
     * Simulates scenario 1
     * @param robot
     */
    @Test
    public void scenarioOneTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#airportConfig");
        // Wait for pane to be expanded
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#airportChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#runwayChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#obstacleConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#obstacleChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#placeObstacleCB");
        robot.clickOn("#calculateButton");

        assertThat(Model.obstaclePlaced).isEqualTo(true);
    }

    @Test
    public void scenarioTwoTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#airportConfig");
        // Wait for pane to be expanded
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#newAirportButton");
        robot.clickOn("#airportNameTextField").write("Southampton Central Airport");
        robot.clickOn("#airportCodeTextField").write("SOU");
        robot.clickOn("#saveAirport");
        robot.clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#newRunway");
        robot.clickOn("#leftDegreeTF").write("12");
        robot.clickOn("#leftToraTF").write("1350");
        robot.clickOn("#rightToraTF").write("1200");
        robot.clickOn("#leftTodaTF").write("1450");
        robot.clickOn("#rightTodaTF").write("1600");
        robot.clickOn("#leftAsdaTF").write("1670");
        robot.clickOn("#rightAsdaTF").write("1320");
        robot.clickOn("#leftLdaTF").write("1325");
        robot.clickOn("#rightLdaTF").write("1430");
        robot.clickOn("#leftThresholdTF").write("400");
        robot.clickOn("#rightThresholdTF").write("0");
        robot.clickOn("#saveRunway");
        robot.clickOn("#obstacleConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#obstacleChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#placeObstacleCB");
        robot.clickOn("#calculateButton");

        assertThat(Model.currentAirport.getCode().equals("SOU")).isEqualTo(true);
    }

    /**
     * Simulates scenario 3
     * @param robot
     */
    @Test
    public void scenarioThreeTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#airportConfig");
        // Wait for pane to be expanded
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#airportChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#runwayChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#obstacleConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#newObstacleButton");
        robot.clickOn("#obstacleNameTF").write("Engine");
        robot.clickOn("#obstacleWidthTF").write("20");
        robot.clickOn("#obstacleHeightTF").write("40");
        robot.clickOn("#saveButton");
        robot.clickOn("#distanceFromLTF").write("20");
        robot.clickOn("#distanceFromRTF").write("1000");
        robot.clickOn("#distanceFromCLTF").write("0");
        robot.clickOn("#dirFromCLChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#placeObstacleCB");
        robot.clickOn("#calculateButton");

        assertThat(Model.currentObstacle.getName()).isEqualTo("Engine");
    }

    /**
     * Testing that it fails when there is a missing value on creating a new runway
     * @param robot
     * @throws InterruptedException
     */
    @Test
    public void scenarioFourTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#airportConfig");
        // Wait for pane to be expanded
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#airportChoiceBox");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#newRunway");
        robot.clickOn("#leftToraTF").write("1350");
        robot.clickOn("#rightToraTF").write("3902");
        robot.clickOn("#leftTodaTF").write("1450");
        robot.clickOn("#rightTodaTF").write("3902");
        robot.clickOn("#leftAsdaTF").write("1670");
        robot.clickOn("#rightAsdaTF").write("3902");
        robot.clickOn("#leftLdaTF").write("1325");
        robot.clickOn("#rightLdaTF").write("3595");
        robot.clickOn("#leftThresholdTF").write("400");
        robot.clickOn("#rightThresholdTF").write("306");
        robot.clickOn("#saveRunway");

        verifyThat("OK", NodeMatchers.isVisible());
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

}
