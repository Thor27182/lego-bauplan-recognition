package tuc.isse.propra.database;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Oliver Neumann
 * @version 1.3
 */
public class DatabaseController {
    public static Logger logger = Logger.getLogger(DatabaseController.class.getName());

    /**
     * Methode um die Verbindung zum MySQL Server  aufzubauen.
     * @return Connection wird zurückgegeben wenn die Verbindung erfolgreich war. Sonst null.
     */
    public static Connection getConnection() {
        try{
            String url = "jdbc:mysql://cocb.zapto.org:3306/propra_lego";
            String username = "propra_lego";
            String password = "legodb2020";
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url,username,password);
            if(conn != null){
                logger.info("Verbunden mit DB.");
                return conn;
            }else{
                logger.severe("Verbindung mit DB fehlgeschlagen.");
                return null;
            }
        } catch (Exception e){
            logger.severe("Verbindung mit DB fehlgeschlagen.");
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Methode um ein SQL Befehl zum Server zu senden.
     * @param conn Verbindung zur Datenbank.
     * @param prepareStatement SQL INSERT Statement
     */
    private static boolean post(Connection conn, String prepareStatement) {
        try{
            if(conn == null)
                return false;
            PreparedStatement post = conn.prepareStatement(prepareStatement);
            post.executeUpdate();
            post.close();
            return true;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Methode um das Prepare Statement der block Tabelle zu erstellen.
     * @param conn Verbindung zur Datenbank.
     * @param blockClassID Zugehoerige blockClassID als foreigen key.
     */
    public static boolean insertBlock(Connection conn, int blockClassID, int structID) {
        boolean postResult = post(conn,"INSERT INTO block (blockClassID, struct) VALUES (" + blockClassID +", "+ structID +")");
        if(postResult)
            logger.info("Block hinzugefuegt.");
        else
            logger.severe("Block konnte nicht hinzugefuegt werden!");
        return postResult;
    }

    /**
     * Merhode um eine Structure der struct Tabelle hinzuzufuegen.
     * @param conn Verbindung zur Datenbank.
     * @param profile  Profiltyp der structure.
     */
    public static boolean insertStruct(Connection conn, String profile) {
        try{
            if(conn == null)
                return false;
            ArrayList<Integer> ids = selectStruct(conn);
            assert ids != null;
            int nextID = ids.get(ids.size()-1) + 1;
            PreparedStatement post;
            post = conn.prepareStatement("INSERT INTO struct (profile, structID) VALUES ('"+profile+"', '" + nextID +"')");
            post.executeUpdate();
            logger.info("Element added.");
            post.close();
            return true;
        }catch (Exception e){
            logger.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Merhode um ein Bild der struct_img Tabelle hinzuzufuegen.
     * @param conn Verbindung zur Datenbank.
     * @param img Bild der Structure.
     * @param structID Zugehoerige structID als foreigen key.
     */
    public static boolean insertImage(Connection conn, File img, int structID) {
        try{
            InputStream inputStream = new FileInputStream(img);
            if(conn == null)
                return false;
            PreparedStatement post = conn.prepareStatement("INSERT INTO struct_img (imgData, struct) VALUES (?, "+structID+")");
            post.setBinaryStream(1, inputStream, (int) img.length());
            post.executeUpdate();
            logger.info("Image added.");
            post.close();
            inputStream.close();
            return true;
        }catch (Exception e){
            logger.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Methode um ein Bild in die Datenbank zu speichern.
     * @param conn Verbindung zur Datenbank.
     * @param img Von der Kamera aufgenommenes Bild.
     * @param structID ID der Structure in der das Bild gespeichert wird.
     * @return Gibt bei erfolg true zurueck.
     */
    public static boolean insertImage(Connection conn, BufferedImage img, int structID) {
        try{
            File imageFile = File.createTempFile("image-",".jpg",null);
            ImageIO.write(img, "jpg", imageFile);
            InputStream inputStream = new FileInputStream(imageFile);
            if(conn == null)
                return false;
            PreparedStatement post = conn.prepareStatement("INSERT INTO struct_img (imgData, struct) VALUES (?, "+structID+")");
            post.setBinaryStream(1, inputStream, (int) imageFile.length());
            post.executeUpdate();
            logger.info("Image added.");
            post.close();
            inputStream.close();
            return true;
        }catch (Exception e){
            logger.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Methode um das Prepare Statement der block_calss Tabelle zu erstellen.
     * @param conn Verbindung zur Datenbank.
     * @param width Breite des Blockes.
     * @param height Hoehe des Blockes.
     * @param length Laenge des Blockes.
     * @param color Farbe als Hex Wert.
     */
    public static boolean insertBlockClass(Connection conn, int length, int width, int height, String color) {
        if(color.length() > 6){
            JOptionPane.showMessageDialog(null, "Untgültige Farbwerte!","Inputerror",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean postResult = post(conn,"INSERT INTO block_class (color, length, width, height) VALUES ('" + color + "'," + length + "," + width + ","+ height + ")");
        if(postResult)
            logger.info("BlockClass hinzugefuegt.");
        else
            logger.severe("BlockClass konnte nicht hinzugefuegt werden!");
        return postResult;
    }

    /**
     * Methode um die IDs aller Structures zu bekommen.
     * @param conn Verbindung zur Datenbank.
     * @return ArrayList mit den strictIDs als Integer.
     */
    public static ArrayList<Integer> selectStruct(Connection conn){
        try{
            if(conn == null)
                return null;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT structID FROM struct");
            ArrayList<Integer> structIDs = new ArrayList<>();
            while (resultSet.next()){
                structIDs.add(resultSet.getInt("structID"));
            }
            statement.close();
            resultSet.close();
            logger.info("StructIDs aus DB geladen.");
            return structIDs;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     *
     * Methode um die Bilder einer Structure zu bekommen.
     * @param conn Verbindung zur Datenbank.
     * @param structID ID der struct von welcher die Bilder geholt werden.
     * @return ArrayList mit den Bildern als Image.
     *
     */
    public static ArrayList<Image> selectIMG(Connection conn, int structID){
        try{
            if(conn == null)
                return null;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT imgData FROM struct_img WHERE struct = " + structID);
            ArrayList<Image> images = new ArrayList<>();
            while (resultSet.next()){
                Blob blob = resultSet.getBlob("imgData");
                byte[] array = blob.getBytes(1,(int) blob.length());
                File file = File.createTempFile("image-",".jpg",null);
                FileOutputStream out = new FileOutputStream(file);
                out.write(array);
                out.close();
                FileInputStream inputStream = new FileInputStream(file);
                images.add(new Image(inputStream));
            }
            logger.info("Images aus DB geladen");
            statement.close();
            resultSet.close();
            return images;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return null;
        }
    }


    /**
     * Select a signle Image from a structure.
     * @param conn Verbindung zur Datenbank.
     * @param structID Structure of the image.
     * @param imageID ID of the image.
     * @return An Image
     */
    public static Image selectImage(Connection conn, int structID, int imageID){
        try{
            if(conn == null)
                return null;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT imgData FROM struct_img WHERE struct = '" + structID + "' AND imgID = '"+imageID+"'");

            Image image;
            if (resultSet.next()){
                Blob blob = resultSet.getBlob("imgData");
                byte[] array = blob.getBytes(1,(int) blob.length());
                File file = File.createTempFile("image-",".jpg",null);
                FileOutputStream out = new FileOutputStream(file);
                out.write(array);
                out.close();
                FileInputStream inputStream = new FileInputStream(file);
                image = new Image(inputStream);
            }else
                return null;
            logger.info("Image aus DB geladen");
            statement.close();
            resultSet.close();
            return image;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Method to get the next ImageID of a specific structure.
     * @param conn Verbindung zur Datenbank.
     * @param structID Structure ID of the Image.
     * @param prevImageID ImageID of the current Image.
     * @return ImageID of the next image.
     */
    public static int nextImageID(Connection conn, int structID, int prevImageID){
        try{
            if(conn == null)
                return -1;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT imgID FROM struct_img WHERE struct = '" + structID + "' AND imgID > '" + prevImageID + "' LIMIT 1");
            int imageID = -1;
            if (resultSet.next()){
                imageID = resultSet.getInt("imgID");
            }
            statement.close();
            resultSet.close();
            return imageID;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return -1;
        }
    }

    /**
     * Methode um die IDs aller Images zu bekommen welcher zu einer bestimmten structure gehoeren.
     * @param conn Verbindung zur Datenbank.
     * @param structID ID der struct von welcher die imgIDs geholt werden.
     * @return ArrayList mit den imgIDs.
     */
    public static ArrayList<Integer> selectImageIDs(Connection conn, int structID){
        try{
            if(conn == null)
                return null;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT imgID FROM struct_img WHERE struct = " + structID);
            ArrayList<Integer> imageIDs = new ArrayList<>();
            while (resultSet.next()){
                imageIDs.add(resultSet.getInt("imgID"));
            }
            statement.close();
            resultSet.close();
            return imageIDs;
        } catch (Exception e){
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     *
     * Methode um die anzahl der Elemente einer struct zu bekommen.
     * @param conn Verbindung zur Datenbank.
     * @param structID ID der struct von welcher die anzahl der dazugehoerigen Elemente geholt werden.
     * @return Anzahl der Elemente einer struct. Im Fall einer Exeption -1
     *
     */
    public static int countElements(Connection conn, int structID){
        try {
            if(conn == null)
                return -1;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(imgID) AS elements FROM struct_img WHERE struct = " +structID);
            resultSet.next();

            int numOfElements = resultSet.getInt("elements");

            statement.close();
            resultSet.close();
            return numOfElements;
        }catch (Exception e){
            logger.severe(e.getMessage());
            return -1;
        }
    }

    /**
     * Methode um den Profiltypen der Structure zu bekommen.
     * @param conn Verbindung zur Datenbank.
     * @param structID ID der Structure.
     * @return Den Profiltypen als String.
     */
    public static String getProfile(Connection conn, int structID){
        try {
            if(conn == null)
                return null;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT profile FROM struct WHERE structID = " +structID);
            resultSet.next();
            String profile = resultSet.getString("profile");
            statement.close();
            resultSet.close();
            return profile;
        }catch (Exception e){
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Method to delete images in the DB.
     * @param conn Verbindung zur Datenbank.
     * @param imgID imgID of the image which is to be deleted.
     */
    public static boolean deleteImage(Connection conn, int imgID){
        boolean postResult = post(conn,"DELETE FROM struct_img WHERE imgID = " + imgID);
        if(postResult)
            logger.info("Image mit der ID " + imgID + " wurde entfernt.");
        else
            logger.severe("Image mit der ID " + imgID + " konnte nicht entfernt werden!");
        return  postResult;
    }

    /**
     * Method to delete a struct and all of its images.
     * @param conn Verbindung zur Datenbank.
     * @param structID ID of the struct which is to be deleted.
     */
    public static boolean deleteStruct(Connection conn, int structID){
        return post(conn,"DELETE FROM struct WHERE structID = " + structID);
    }
}