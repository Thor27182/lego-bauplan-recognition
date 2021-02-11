package tuc.isse.propra;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.opencv.core.Mat;
import tuc.isse.propra.camera.ImageController;
import tuc.isse.propra.database.DatabaseController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * MainviewController handels the Userinput
 * @author thore, Oliver Neumann
 * @version 1.5
 */
public class MainviewController {

    //Image input
    @FXML public ImageView imageview_image_contol_actual_image;
    //Image Recognition
    @FXML public ImageView imageview_recognition_actual_image;
    @FXML public Button button_recognition_recognize_objects;
    @FXML public Label lable_recognition_object_recognized;
    //Database Add
    @FXML public ImageView imageview_database_add_actual_image;
    @FXML public ListView<String> listview_database_add_liststructures;
    @FXML public TextField textfield_database_add_textstructure;
    @FXML public TextField textfield_database_add_filedisplay;
    @FXML public TextField textfield_database_add_structType;
    //Database Remove
    @FXML public ListView<String> listview_database_remove_liststructures;
    @FXML public ImageView imageview_remove;
    @FXML public AnchorPane anchorpane_root;
    //Statistics
    @FXML public PieChart statistic_piechart = new PieChart();

    private Mat actual_image;
    private ArrayList<Integer> structIDs;
    private int imageID_remove;
    private final ArrayList<Integer> knownImageIDs = new ArrayList<>();

    //Image input
    /**
     * Take a Picture via ImageController. Set Image in 3 different Places (Control,
     * Recognition and Database Add) to show the user the Image he currently is working
     * with.
     */
    @FXML
    public void handleTakeAnImage() throws IOException {
        this.actual_image = ImageController.takePicture();
        Image image = ImageController.convertMatToImage(this.actual_image);

        ImageController.writeMatToJPEG(this.actual_image);

        imageview_recognition_actual_image.setImage(image);
        imageview_recognition_actual_image.setVisible(true);

        imageview_database_add_actual_image.setImage(image);
        imageview_database_add_actual_image.setVisible(true);

        imageview_image_contol_actual_image.setImage(image);
        imageview_image_contol_actual_image.setVisible(true);
    }


    //Image recognition
    // TODO: 21.07.2020 right py-file needs to be executed
    public void handleImageRecognition() throws IOException {
        try {
            // command for executing python file has to be manually changed to YOUR path
            final Process runNN = Runtime.getRuntime().exec("python C:\\Users\\Gaming-PC\\IdeaProjects\\lego-bauplan-recognition\\src\\main\\java\\tuc\\isse\\propra\\helloworld.py");

            BufferedReader readCMD = new BufferedReader(new InputStreamReader(runNN.getInputStream()));
            System.out.println("Process started, readCMD ready");
            String result = readCMD.readLine();
            System.out.println("line read");
            System.out.println("Result: " + result);

            lable_recognition_object_recognized.setText(result);
        } catch (IOException e){
            System.out.println(e);
        }
    }


    //General

    /**
     * Small method to refresh the GUI.
     */
    @FXML
    public void handleRefresh() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assert connection != null;
        updateListView(connection);
        updatePieChart(connection);
        connection.close();
    }

    /**
     * Loads StructIDs from Database, convert them to Strings and show them as
     * observable List to the user in the Listview at Database add
     * @param connection Verbindung zur Datenbank.
     */
    public void updateListView(Connection connection) {
        this.structIDs = DatabaseController.selectStruct(connection);
        var struct_profiles = new ArrayList<String>();
        if (structIDs != null) {
            for(Integer structID : structIDs){
                struct_profiles.add(DatabaseController.getProfile(connection, structID));
            }
            this.listview_database_add_liststructures.setItems(FXCollections.observableList(struct_profiles));
            this.listview_database_add_liststructures.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.listview_database_remove_liststructures.setItems(FXCollections.observableList((struct_profiles)));
            this.listview_database_remove_liststructures.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
    }

    @FXML
    public void handelRecognition(){

    }

    //Statistics

    /**
     * Updates the PieChart data.
     * @param connection Verbindung zur Datenbank.
     */
    public void updatePieChart(Connection connection) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int elements;
        for(Integer i : structIDs){
            elements = DatabaseController.countElements(connection, i);
            pieChartData.add(new PieChart.Data(DatabaseController.getProfile(connection, i) + ": " + elements, elements));
        }
        statistic_piechart.setData(pieChartData);
    }

    //Database add

    @FXML
    public void handleCreateNewStruct() throws SQLException {
        if(!textfield_database_add_structType.getText().isEmpty()){
            Connection connection = DatabaseController.getConnection();
            assert connection != null;
            DatabaseController.insertStruct(connection, textfield_database_add_structType.getText());
            updateListView(connection);
            updatePieChart(connection);
            connection.close();
        }
    }

    /**
     * When clicked on an Element of the list, the Element is shown in the Textfield.
     * So the User can easy label an Image
     */
    @FXML
    public void handleSelectStructureID(){
        textfield_database_add_textstructure.setText(
                listview_database_add_liststructures.getSelectionModel().getSelectedItem());
    }

    /**
     * Opens the System file-manager
     */
    @FXML
    public void handleSelectFileFromSystem(){
        textfield_database_add_filedisplay.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open File");
        File file = fileChooser.showOpenDialog(this.anchorpane_root.getScene().getWindow());
        if (file != null) {
            try {
                java.awt.Desktop.getDesktop().open(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            List<File> files = Collections.singletonList(file);
            for (File file2 : files) {
                textfield_database_add_filedisplay.appendText(file2.getAbsolutePath() + "\n");
            }
        }
    }

    /**
     * Add a new Image with Label to the Database
     */
    @FXML
    public void handleAddNewImageToDatabase() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assert connection != null;
        int structureID=0;
        String name;
        for(Integer structID: structIDs) {
            name = DatabaseController.getProfile(connection,structID);
            if (textfield_database_add_textstructure.getText().equals(name)) {
                structureID = structID;
                break;
            }
        }

        if(structureID!=0){
            if(this.actual_image != null)
                DatabaseController.insertImage(
                        connection,
                        ImageController.convertMatToBufferdImage(this.actual_image),structureID);
                updatePieChart(connection);
        }

        connection.close();
    }

    //Database remove

    /**
     * Loads first image of selected structure.
     */
    @FXML
    public void handleSelectImageStructure() throws SQLException {
        knownImageIDs.clear();
        int element = structIDs.get(listview_database_remove_liststructures.getSelectionModel().getSelectedIndex());
        Connection connection = DatabaseController.getConnection();
        assert connection != null;
        imageID_remove = DatabaseController.nextImageID(connection, element,0);
        knownImageIDs.add(imageID_remove);
        Image image = DatabaseController.selectImage(connection, element,imageID_remove);
        connection.close();
        imageview_remove.setImage(image);
    }

    /**
     * Gets the previous image
     */
    @FXML
    public void handlePreviousImage() throws SQLException {
        if(knownImageIDs.size() == 0)
            return;
        imageID_remove = knownImageIDs.get(knownImageIDs.size()-1);
        knownImageIDs.remove(knownImageIDs.size()-1);
        Connection connection = DatabaseController.getConnection();
        assert connection != null;
        imageview_remove.setImage(DatabaseController.selectImage(connection, structIDs.get(listview_database_remove_liststructures.getSelectionModel().getSelectedIndex()),imageID_remove));
        connection.close();
    }

    /**
     * Deletes the whole structure and its contents.
     */
    @FXML
    public void handleDelStruct() throws SQLException {
        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION);
        alertDialog.setTitle("Profil löschen");
        alertDialog.setHeaderText("Wollen sie das Profil löschen?");
        alertDialog.setContentText("Wenn sie ein Profil löschen werden auch alle darin enthaltenen Bilder permanent gelöscht!");
        ButtonType yes = new ButtonType("Ja");
        ButtonType no = new ButtonType("Abbrechen");
        alertDialog.getButtonTypes().clear();
        alertDialog.getButtonTypes().setAll(yes, no);
        alertDialog.showAndWait();

        if(alertDialog.getResult() == yes){
            Connection connection = DatabaseController.getConnection();
            assert connection != null;
            DatabaseController.deleteStruct(connection, structIDs.get(listview_database_remove_liststructures.getSelectionModel().getSelectedIndex()));
            updateListView(connection);
            updatePieChart(connection);
            connection.close();
            imageview_remove.setImage(null);
        }
    }

    /**
     * Deletes the current image.
     */
    @FXML
    public void handleDelImage() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assert connection != null;
        DatabaseController.deleteImage(connection, imageID_remove);
        updatePieChart(connection);
        connection.close();
        if(knownImageIDs.size() == 0)
            handleNextImage();
        else
            handlePreviousImage();

    }

    /**
     * Gets the next element in the structure.
     */
    @FXML
    public void handleNextImage() {
        Connection connection = DatabaseController.getConnection();
        knownImageIDs.add(imageID_remove);
        imageID_remove = DatabaseController.nextImageID(connection, structIDs.get(listview_database_remove_liststructures.getSelectionModel().getSelectedIndex()),imageID_remove);
        imageview_remove.setImage(DatabaseController.selectImage(connection, structIDs.get(listview_database_remove_liststructures.getSelectionModel().getSelectedIndex()),imageID_remove));
    }
}