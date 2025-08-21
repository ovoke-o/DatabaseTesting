package storeprocedureTesting;

import Utils.TestConfig;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class StoreProcedureTest {

    Connection con = null;
    Statement stmt = null;
    ResultSet rs;
    CallableStatement cStmt;
    ResultSet rs1;
    ResultSet rs2;

    @BeforeClass
    void setup() throws SQLException, ClassNotFoundException {
        con = TestConfig.dbConnection();
    }

    @Test(priority = 1)
    void testStoreProcedureExist() throws SQLException {
        stmt = con.createStatement();
        rs = stmt.executeQuery(TestConfig.SELECT_ALL_CUSTOMERS);
        rs.next();
        Assert.assertEquals(rs.getString("Name"), "SelectAllCustomers");
    }

    @Test(priority = 2)
    void testSelectAllCustomers() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_PROCEDURE);
        rs1 = cStmt.executeQuery(); // resultset1

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.SELECT_ALL_FROM_CUSTOMERS);

        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2));
    }

    @Test(priority = 3)
    void testSelectAllCustomersByCity() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_PROCEDURE);
        cStmt.setString(1, "Singapore");
        rs1 = cStmt.executeQuery(); // resultset1

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY);

        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2));
    }

    @Test(priority = 4)
    void testSelectAllCustomersByCityAndPinCode() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PINCODE_PROCEDURE);
        cStmt.setString(1, "Singapore");
        cStmt.setString(2, "079903");
        rs1 = cStmt.executeQuery(); // resultset1

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN);

        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2));
    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

}
