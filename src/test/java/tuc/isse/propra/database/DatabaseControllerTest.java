package tuc.isse.propra.database;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 *
 * Klasse zum testen der Methoden in der DatabaseController Klasse.
 * @author Oliver Neumann
 * @version 1.3
 *
 */
public class DatabaseControllerTest {

    @Test
    public void getConnectionTest() {
        Connection connection = DatabaseController.getConnection();
        if(connection == null)
            fail();
        assertTrue(true);
    }

    /*
    @Test
    public void countElementsTest(){
        assertEquals(1, DatabaseController.countElements(2));
    }
    */

    @Test
    public void postTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assertTrue(DatabaseController.deleteImage(connection,500));
        connection.close();
    }

    @Test
    public void insetImageTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assertTrue(DatabaseController.insertImage(connection, new File("src/main/resources/Bauplaene/Bilder/BP2_1.JPG"),2));
        connection.close();
    }

    @Test
    public void insertBlockClassTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        assertTrue(DatabaseController.insertBlockClass(connection,1,1,1,"000000"));
        connection.close();
    }

    @Test
    public void selectStructTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        ArrayList<Integer> idList = DatabaseController.selectStruct(connection);
        connection.close();
        if (idList != null)
            assertTrue(true);
        else
            fail();
    }

    @Test
    public void selectImageIdDsTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        ArrayList<Integer> idList = DatabaseController.selectImageIDs(connection,2);
        connection.close();
        if (idList != null)
            assertTrue(true);
        else
            fail();
    }

    @Test
    public void deleteImageTest() throws SQLException {
        Connection connection = DatabaseController.getConnection();
        ArrayList<Integer> idList = DatabaseController.selectImageIDs(connection,2);
        assert idList != null;
        assertTrue(DatabaseController.deleteImage(connection,idList.get(idList.size()-1)));
        connection.close();
    }

    /*
    @Test
    public void insertBlockTest(){
        assertTrue(DatabaseController.insertBlock(1,5));
    }
     */
}