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
    PreparedStatement pStmt;
    String query;

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

        query = TestConfig.CALL_ALL_CUSTOMERS_BY_CITY;
        pStmt = con.prepareStatement(query);
        pStmt.setString(1, "Singapore");
        rs2 = pStmt.executeQuery();

        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2));
    }

    @Test(priority = 4)
    void testSelectAllCustomersByCityAndPinCode() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PINCODE_PROCEDURE);
        cStmt.setString(1, "Singapore");
        cStmt.setString(2, "079903");
        rs1 = cStmt.executeQuery(); // resultset1

        query = TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN;
        pStmt = con.prepareStatement(query);
        pStmt.setString(1, "Singapore");
        pStmt.setString(2, "079903");
        rs2 = pStmt.executeQuery();
        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2));
    }

    @Test(priority = 5)
    void testGetOrderByCustomer() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_GET_ORDER_BY_CUSTOMER_PROCEDURE);
        cStmt.setInt(1, 141);

        cStmt.registerOutParameter(2, Types.INTEGER);
        cStmt.registerOutParameter(3, Types.INTEGER);
        cStmt.registerOutParameter(4, Types.INTEGER);
        cStmt.registerOutParameter(5, Types.INTEGER);

        cStmt.executeQuery();

        int shipped = cStmt.getInt(2);
        int canceled = cStmt.getInt(3);
        int resolved = cStmt.getInt(4);
        int disputed = cStmt.getInt(5);

        query = TestConfig.CALL_GET_ORDER_BY_CUSTOMER_QUERY;
        pStmt = con.prepareStatement(query);
        // set parameters (repeated for each subquery)
        pStmt.setInt(1, 141);
        pStmt.setString(2, "Shipped");

        pStmt.setInt(3, 141);
        pStmt.setString(4, "Canceled");

        pStmt.setInt(5, 141);
        pStmt.setString(6, "Resolved");

        pStmt.setInt(7, 141);
        pStmt.setString(8, "Disputed");

        rs = pStmt.executeQuery();
        rs.next();
            int exp_shipped = rs.getInt("shipped");
            int exp_canceled = rs.getInt("canceled");
            int exp_resolved = rs.getInt("resolved");
            int exp_disputed = rs.getInt("disputed");

            System.out.println("Shipped: " + exp_shipped);
            System.out.println("Canceled: " + exp_canceled);
            System.out.println("Resolved: " + exp_resolved);
            System.out.println("Disputed: " + exp_disputed);

        if (shipped == exp_shipped && canceled == exp_canceled && resolved == exp_resolved && disputed == exp_disputed)
            Assert.assertTrue(true);
            else
            Assert.assertFalse(false);

    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

}
