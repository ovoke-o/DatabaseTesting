package Steps;

import Utils.TestConfig;
import io.cucumber.java.en.*;
import org.testng.Assert;

import java.sql.*;

public class StoreProcedureSteps {

    private Connection con;
    private Statement stmt;
    private CallableStatement cStmt;
    private ResultSet rs1;
    private ResultSet rs2;

    @Given("I am connected to the database")
    public void connectToDatabase() throws Exception {
        con = TestConfig.dbConnection();
    }

    @When("I check if the stored procedure {string} exists")
    public void checkStoredProcedureExists(String procedureName) throws SQLException {
        stmt = con.createStatement();
        rs1 = stmt.executeQuery("SHOW PROCEDURE STATUS WHERE Name = '" + procedureName + "'");
        rs1.next();
    }

    @Then("the procedure {string} should be available in the database")
    public void verifyProcedureExists(String procedureName) throws SQLException {
        Assert.assertEquals(rs1.getString("Name"), "SelectAllCustomers", procedureName + " stored procedure does not exist in the database");
    }

    @When("I execute the SelectAllCustomers stored procedure")
    public void executeSelectAllCustomersProcedure() throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_PROCEDURE);
        rs1 = cStmt.executeQuery();

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.SELECT_ALL_FROM_CUSTOMERS);
    }

    @When("I execute the SelectAllCustomersByCity stored procedure with city {string}")
    public void executeSelectAllCustomersByCity(String city) throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_PROCEDURE);
        cStmt.setString(1, city);
        rs1 = cStmt.executeQuery();

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY);
    }

    @When("I execute the SelectAllCustomersByCityAndPinCode stored procedure with city {string} and pin {string}")
    public void executeSelectAllCustomersByCityAndPin(String city, String pin) throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PINCODE_PROCEDURE);
        cStmt.setString(1, city);
        cStmt.setString(2, pin);
        rs1 = cStmt.executeQuery();

        stmt = con.createStatement();
        rs2 = stmt.executeQuery(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN);
    }

    @Then("the result should match the {string} query")
    public void verifyProcedureResult(String queryName) throws SQLException {
        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2), "Stored procedure result does not match query: " + queryName);
        System.out.println("Stored procedure result matches: " + queryName);
    }
}
