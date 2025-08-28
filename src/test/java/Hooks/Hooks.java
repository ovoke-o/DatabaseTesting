package Hooks;

import Utils.TestConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.SQLException;

public class Hooks {

    Connection con = null;

    @BeforeClass
    void setup() throws SQLException, ClassNotFoundException {
        con = TestConfig.dbConnection();
    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

}
