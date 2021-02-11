package tuc.isse.propra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import nu.pattern.OpenCV;

import java.io.IOException;

/**
 *
 * Program entry class.
 * @author Oliver Neumann
 * @author Thore Jonathan Braun
 * @version 1.2
 */
public class Main extends Application{


    /**
     * The main entry point for all JavaFX applications. The start method is
     * called after the init method has returned, and after the system is ready
     * for the application to begin running.
     *
     * @param stage - the primary stage for this application, onto which the
     *              application scene can be set. The primary stage will be
     *              embedded in the browser if the application was launched
     *              as an applet. Applications may create other stages, if
     *              needed, but they will not be primary stages and will not
     *              be embedded in the browser.
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            //set primary stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Mainview.fxml "));
            AnchorPane anchorpane_root = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorpane_root);
            stage.setTitle("Lego-Bauplan-Recognition");
            stage.setMaxHeight(600.0);
            stage.setMinWidth(800.0);
            stage.setScene(scene);
            stage.show();

            //Connected Controller and start Videostream
            MainviewController mainviewController = loader.getController();
            mainviewController.handleRefresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to start the programm.
     * @param args Unused
     */
    public static void main(String[] args) {
        //initialize the Library
        OpenCV.loadLocally();
        //Launch GUI
        launch(args);
    }
}