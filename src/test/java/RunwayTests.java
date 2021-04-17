import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;


import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;


import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;


public class RunwayTests extends ApplicationTest {

    private Scene scene;
    private ChoiceBox<String> cb;

    @Start
    public void start(Stage stage) {
        try {
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
    public void fillRunwayDetailsAutomatic() {
        Demo.setup();
        Model.currentAirport = Model.airports.get(0);
        Model.currentRunway = Model.currentAirport.getRunways().get(0);
        clickOn("#runwayConfig");
        WaitForAsyncUtils.waitForFxEvents();
    }

    @AfterEach
    public void cleanUp() {
        Model.currentAirport = null;
        Model.airports.clear();
        Model.currentRunway = null;
        Model.currentObstacle = null;
        Model.obstacles.clear();
    }


    /**------------------------------------Integration tests------------------------------------
     *
     * Integration tests between RunwayConfigController and Model
     */
    @Test
    @Tag("Integration_test")
    @DisplayName("Runway Creation test")
    public void creationTest() {
        clickOn("#runwayConfig");
        sleep(250);
        clickOn("#newRunway");
        clickOn("#leftPosition");
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);
        clickOn("#leftDegreeTF").write("10");
        clickOn("#leftToraTF").write("1300");
        clickOn("#rightToraTF").write("1400");
        clickOn("#leftTodaTF").write("1350");
        clickOn("#rightTodaTF").write("1400");
        clickOn("#leftAsdaTF").write("1320");
        clickOn("#rightAsdaTF").write("1420");
        clickOn("#leftLdaTF").write("1320");
        clickOn("#rightLdaTF").write("1420");
        clickOn("#leftThresholdTF").write("300");
        clickOn("#rightThresholdTF").write("0");
        clickOn("#saveRunway");
        assertThat(Model.currentRunway.toString()).isEqualTo("10R/28L");
        assertThat(Model.currentRunway.getLeftRunway().getDegree()).isEqualTo(10);
        assertThat(Model.currentRunway.getRightRunway().getDegree()).isEqualTo(28);
    }

    @Test
    @Tag("Integration_test")
    @DisplayName("Runway edit test")
    public void editTest() {
        clickOn("#editRunway");
        clickOn("#leftDegreeTF");
        clickOn("#leftDegreeTF").write("15");
        clickOn("#saveRunway");
        assertThat(Model.currentRunway.getLeftRunway().getDegree()).isEqualTo(15);
        assertThat(Model.currentRunway.getRightRunway().getDegree()).isEqualTo(33);
    }


    /**------------------------------------Boundary testing------------------------------------
     *
     * Boundary testing for UI degree textfield functionality
     */
    @Tag("Boundary_test")
    @DisplayName("Boundary checking for degree values")
    @ParameterizedTest (name = "test {index} => {0} degrees")
    @MethodSource("boundaryValues")
    public void degreesBoundaryTests(String degree){
        clickOn("#editRunway");
        clickOn("#leftDegreeTF");
        clickOn("#leftDegreeTF").write(degree);
        int rightDegree = Math.abs(Integer.parseInt(degree));
        if(rightDegree <= 18)
            verifyThat("#rightDegreeLabel", LabeledMatchers.hasText(String.valueOf(rightDegree+18)));
        else
            verifyThat("#rightDegreeLabel", LabeledMatchers.hasText(""));
    }


    /**------------------------------------Verification testing------------------------------------*/
    @Tag("Verification_test")
    @DisplayName("Verification testing for wrong values entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("wrongValues")
    public void testWrongValuesEntered(String val, String query) {
        clickOn("#editRunway");
        doubleClickOn(query).write(val);
        clickOn("#saveRunway");
        verifyThat(query, TextInputControlMatchers.hasText(""));
    }

    /**------------------------------------Validation testing------------------------------------*/
    @Tag("Validation_test")
    @DisplayName("Validation testing for correct values entered")
    @ParameterizedTest (name = "test {index} => value {0} textField {1} ")
    @MethodSource("correctValues")
    public void testCorrectValuesEntered(String val, String query) {
        clickOn("#editRunway");
        doubleClickOn(query).write(val);
        clickOn("#saveRunway");
        verifyThat(query, TextInputControlMatchers.hasText(val));
    }

    //------------------------------------Helper methods------------------------------------
    @SuppressWarnings("boundaryValues")
    private static Stream boundaryValues() {
        return  Stream.of(
                Arguments.of("-36"),
                Arguments.of("-19"),
                Arguments.of("-18"),
                Arguments.of("-1"),
                Arguments.of("0"),
                Arguments.of("1"),
                Arguments.of("18"),
                Arguments.of("19"),
                Arguments.of("36"));
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

    static final String AB = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&\\'()*+,-./:;<=>?@[\\\\]^_`{|}~";
    static SecureRandom rnd = new SecureRandom();

    private static String randomString(){
        int len = (int)(Math.random() * 10 + 4);
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private static String randomQuery() {
        String[] queries = {"#leftDegreeTF","#leftToraTF","#rightToraTF","#leftTodaTF","#rightTodaTF","#leftAsdaTF","#rightAsdaTF",
                "#leftLdaTF","#rightLdaTF", "#leftThresholdTF","#rightThresholdTF"};
        Random generator = new Random();
        int randomIndex = generator.nextInt(queries.length);
        return queries[randomIndex];
    }

    private static String randomNumber() {
        Random generator = new Random();
        int randomNumber = generator.nextInt(10000);
        return String.valueOf(randomNumber);
    }

}