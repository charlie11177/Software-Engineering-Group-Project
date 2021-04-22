import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;


public class ObstacleTests extends ApplicationTest {

    private Scene scene;
    private ChoiceBox<String> cb;

    @Start
    public void start(Stage stage) {
        try {
            System.getProperties().put("testfx.robot", "glass");
            URL url = new File("src/main/resources/view/MainWindow.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            this.scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            cb = lookup("#airportChoiceBox").query();
        } catch (Exception e) {
            fail("couldn't find fxml file");
        }
    }

    @BeforeEach
    public void setup(){
        Demo.setup();
        Model.currentObstacle = null;
    }

    @AfterEach
    public void cleanUp() {
        Model.currentAirport = null;
        Model.airports.clear();
        Model.currentRunway = null;
        Model.currentObstacle = null;
        Model.obstaclePlaced = false;
        Model.obstacles.clear();
    }

    /**------------------------------------Integration tests------------------------------------
     *
     * Integration tests between ObstacleConfigController and Model
     */
    @Test
    @Tag("Integration_test")
    @DisplayName("Obstacle Creation test")
    public void creationTest() {
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#newObstacleButton");
        clickOn("#obstacleNameTF").write("Airbus_A320");
        clickOn("#obstacleWidthTF").write("25");
        clickOn("#obstacleHeightTF").write("13");
        clickOn("#saveButton");
        assertThat(Model.currentObstacle.getName()).isEqualTo("Airbus_A320");
        assertThat(Model.obstacles.size()).isEqualTo(2);
        ChoiceBox cb = lookup("#obstacleChoiceBox").query();
        assertThat(cb.getValue()).isEqualTo("Airbus_A320");
        assertThat(Model.obstaclePlaced).isEqualTo(false);

        clickOn("#newObstacleButton");
        clickOn("#obstacleNameTF").write("Boeing_737_MAX");
        clickOn("#obstacleWidthTF").write("30");
        clickOn("#obstacleHeightTF").write("14");
        clickOn("#saveButton");
        assertThat(Model.currentObstacle.getName()).isEqualTo("Boeing_737_MAX");
        assertThat(Model.obstacles.size()).isEqualTo(3);
        assertThat(cb.getValue()).isEqualTo("Boeing_737_MAX");
        assertThat(Model.obstaclePlaced).isEqualTo(false);
    }

    @Test
    @Tag("Integration_test")
    @DisplayName("Obstacle edit test")
    public void editTest() {
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        clickOn("#editObstacleButton");
        write("Airbus_A380");
        doubleClickOn("#obstacleWidthTF").write("30");
        doubleClickOn("#obstacleHeightTF").write("15");
        clickOn("#saveButton");
        assertThat(Model.currentObstacle.getName()).isEqualTo("Airbus_A380");
        assertThat(Model.obstacles.size()).isEqualTo(1);
        assertThat(Model.obstaclePlaced).isEqualTo(false);
    }

    @Test
    @Tag("Integration_test")
    @DisplayName("Obstacle remove test")
    public void removeTest() {
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        clickOn("#deleteObstacleButton");
        assertThat(Model.currentObstacle).isEqualTo(null);
        assertThat(Model.obstacles.size()).isEqualTo(0);
        assertThat(Model.obstaclePlaced).isEqualTo(false);
    }

    /**
     * Integration test between UI controller classes and calculator
     */
    @Test
    @Tag("Integration_test")
    @DisplayName("Validation testing for placing an obstacle with correct input")
    public void testPlacingObstacleCorrectValues() {
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        clickOn("#placeObstacleCB");
        clickOn("#calculateButton");
        Button b = lookup("#calculateButton").query();
        assertThat(b).hasText("Edit");
        verifyThat(lookup("#topTableView").queryAs(TableView.class), TableViewMatchers.hasNumRows(2));
    }




    /**------------------------------------Unit tests------------------------------------
     *
     * Unit test for correct width/height values
     */
    @Tag("Unit_test")
    @DisplayName("Unit testing for correct distances entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("correctDistances")
    public void testCorrectDistancesEntered(String val, String query) {
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        doubleClickOn(query).write(val);
        verifyThat(query, TextInputControlMatchers.hasText(val));
    }

    /**
     * Unit test for correct distance values
     */
    @Tag("Unit_test")
    @DisplayName("Unit testing for correct width/height entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("correctValues")
    public void testCorrect(String val, String query) {
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#newObstacleButton");
        clickOn(query);
        write(val);
        verifyThat(query, TextInputControlMatchers.hasText(val));
    }


     /**
     * Unit test for wrong width/height values
     */
    @Tag("Unit_test")
    @DisplayName("Unit testing for wrong width/height entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("wrongValues")
    public void testWrongValuesEntered(String val, String query) {
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        clickOn("#editObstacleButton");
        doubleClickOn(query).write(val);
        verifyThat(query, TextInputControlMatchers.hasText(""));
    }

    /**
     * Unit tests for wrong distance values
     */
    @Tag("Unit_test")
    @DisplayName("Unit testing for wrong distances entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("wrongDistances")
    public void testWrongDistancesEntered(String val, String query) {
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        doubleClickOn(query).press(KeyCode.BACK_SPACE);
        write(val);
        verifyThat(query, TextInputControlMatchers.hasText(""));
    }

    @Test
    @Tag("Unit_test")
    @DisplayName("Unit for placing an obstacle with incorrect input")
    public void testPlacingObstaclesWrongValues() {
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        Model.currentObstacle = Model.obstacles.get(0);
        clickOn("#obstacleConfig");
        sleep(250);
        doubleClickOn("#distanceFromLTF").write("-");
        clickOn("#placeObstacleCB");
        clickOn("#calculateButton");
        Button b = lookup("#calculateButton").query();
        assertThat(b).doesNotHaveText("Edit");
        verifyThat(lookup("#topTableView").queryAs(TableView.class), TableViewMatchers.hasNumRows(0));
    }

    @Tag("Unit_test")
    @DisplayName("Unit testing for correct distances entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("correctValues")
    public void testCorrectValuesEntered(String val, String query) {
        clickOn("#obstacleConfig");
        sleep(250);
        clickOn("#obstacleChoiceBox");
        press(KeyCode.ENTER);
        clickOn("#editObstacleButton");
        doubleClickOn(query).write(val);
        String posVal = String.valueOf(Math.abs(Integer.parseInt(val)));
        verifyThat(query, TextInputControlMatchers.hasText(posVal));
    }


    //------------------------------------Helper methods------------------------------------
    @SuppressWarnings("wrongDistances")
    private static Stream wrongDistances() {
        return  Stream.of(
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()),
                Arguments.of(randomString(),randomDistanceQuery()));
    }

    @SuppressWarnings("wrongValues")
    private static Stream wrongValues() {
        return  Stream.of(
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()),
                Arguments.of(randomString(),randomQuery()));
    }

    @SuppressWarnings("correctDistances")
    private static Stream correctDistances() {
        return  Stream.of(
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()),
                Arguments.of(randomSignedNumber(),randomDistanceQuery()));
    }

    @SuppressWarnings("correctValues")
    private static Stream correctValues() {
        return  Stream.of(
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()),
                Arguments.of(randomNumber(),randomQuery()));
    }

    static final String AB = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&\\'()*+,./:;<=>?@[\\\\]^_`{|}~";
    static SecureRandom rnd = new SecureRandom();

    private static String randomString(){
        int len = (int)(Math.random() * 10 + 4);
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private static String randomQuery() {
        String[] queries = {"#obstacleWidthTF","#obstacleHeightTF"};
        Random generator = new Random();
        int randomIndex = generator.nextInt(queries.length);
        return queries[randomIndex];
    }

    private static String randomDistanceQuery() {
        String[] queries = {"#distanceFromLTF","#distanceFromRTF","#distanceFromCLTF"};
        Random generator = new Random();
        int randomIndex = generator.nextInt(queries.length);
        return queries[randomIndex];
    }

    private static String randomNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 10000 + 1);
        return String.valueOf(randomNumber);
    }

    private static String randomSignedNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(-10000, 10000 + 1);
        return String.valueOf(randomNumber);
    }
}