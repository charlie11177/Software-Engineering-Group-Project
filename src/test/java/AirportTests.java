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
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;


public class AirportTests extends ApplicationTest {

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

    public void cleanUp(){
        Model.currentAirport = null;
        Model.airports.clear();
        Model.currentRunway = null;
        Model.currentObstacle = null;
        Model.obstacles.clear();
    }

    /**------------------------------------Integration tests------------------------------------
     *
     * Integration testing between AirportController and Model classes
     */

    @Test
    @Tag("Integration_test")
    @DisplayName("Testing: model gets updated when a new airport is added")
    public void airportCreationTest() {
        String name = "Southampton Airport";
        String code = "SOU";

        createAirport(name, code);
        assertThat(Model.currentAirport.getName()).isEqualTo(name);
        assertThat(Model.currentAirport.getCode()).isEqualTo(code);
        assertThat(cb.getValue()).isEqualTo(name + " (" + code +")");
        assertThat(Model.airports.size()).isEqualTo(1);

        name = "London Heathrow Airport";
        code = "LHR";
        createAirport(name, code);
        assertThat(Model.currentAirport.getName()).isEqualTo(name);
        assertThat(Model.currentAirport.getCode()).isEqualTo(code);
        assertThat(cb.getValue()).isEqualTo(name + " (" + code +")");
        assertThat(Model.airports.size()).isEqualTo(2);

        cancelAirport(name, code);
        assertThat(Model.currentAirport.getName()).isEqualTo(name);
        assertThat(Model.currentAirport.getCode()).isEqualTo(code);
        assertThat(cb.getValue()).isEqualTo(name + " (" + code +")");
        assertThat(Model.airports.size()).isEqualTo(2);

        name = "London Gatwick Airport";
        code = "LGW";
        editAirport(name,code);
        assertThat(Model.currentAirport.getName()).isEqualTo(name);
        assertThat(Model.currentAirport.getCode()).isEqualTo(code);
        assertThat(cb.getValue()).isEqualTo(name + " (" + code +")");
        assertThat(Model.airports.size()).isEqualTo(2);

        clickOn("#deleteAirportButton");
        assertThat(Model.currentAirport == null);
        assertThat(cb.getValue()).isEqualTo(null);
        assertThat(Model.airports.size()).isEqualTo(1);
        cleanUp();
    }

    //Helper method
    private void editAirport(String name, String code) {
        clickOn("#editAirportButton");

        clickOn("#airportNameTextField");
        clickOn("#airportNameTextField").press(KeyCode.BACK_SPACE);
        clickOn("#airportNameTextField").write(name);

        clickOn("#airportCodeTextField");
        clickOn("#airportCodeTextField").press(KeyCode.BACK_SPACE);
        clickOn("#airportCodeTextField").write(code);
        clickOn("#saveAirport");
    }

    //Helper method
    private void createAirport(String name, String code) {
        clickOn("#airportConfig");
        sleep(250);
        clickOn("#newAirportButton");
        clickOn("#airportNameTextField").write(name);
        clickOn("#airportCodeTextField").write(code);
        clickOn("#saveAirport");
    }

    //Helper method
    private void cancelAirport(String name, String code) {
        clickOn("#airportConfig");
        sleep(250);
        clickOn("#newAirportButton");
        clickOn("#airportNameTextField").write(name);
        clickOn("#airportCodeTextField").write(code);
        clickOn("#cancelAirport");
    }

    @Test
    @Tag("Integration_test")
    @DisplayName("Checking for airport errors when creating new airport")
    public void newAirportErrorsTest(){
        Demo.setup();
        int previousAirports = Model.getAirports().size();
        clickOn("#airportConfig");
        sleep(250);
        clickOn("#newAirportButton");
        clickOn("#saveAirport");
        assertThat(Model.airports.size()).isEqualTo(previousAirports);
        cleanUp();
    }

    @Test
    @Tag("Integration_test")
    @DisplayName("Testing that airport deletion works properly")
    public void deleteAirportTest(){
        Demo.setup();
        Model.currentAirport = Model.airports.get(0);
        clickOn("#airportConfig");
        sleep(250);
        clickOn("#deleteAirportButton");
        assertThat(Model.airports.size()).isEqualTo(1);
        cleanUp();
    }


    @Test
    @Tag("Integration_test")
    @DisplayName("Testing model updates on checkbox changes")
    public void checkCheckBoxUpdates() {
        Demo.setup();
        clickOn("#airportConfig");
        sleep(250);
        clickOn("#airportChoiceBox");
        type(KeyCode.ENTER);
        assertThat(cb.getValue()).isEqualTo("Manchester Airport (MAN)");
        assertThat(Model.currentAirport.getName()).isEqualTo("Manchester Airport");
        assertThat(Model.currentAirport.getCode()).isEqualTo("MAN");

        clickOn("#airportChoiceBox");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertThat(cb.getValue()).isEqualTo("Birmingham International Airport (BHX)");
        assertThat(Model.currentAirport.getName()).isEqualTo("Birmingham International Airport");
        assertThat(Model.currentAirport.getCode()).isEqualTo("BHX");
        cleanUp();
    }
}
