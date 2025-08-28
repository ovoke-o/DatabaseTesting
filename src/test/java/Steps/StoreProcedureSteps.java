package Steps;

import Utils.TestConfig;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.sql.*;

import static org.testng.Assert.assertEquals;

public class StoreProcedureSteps {

    public PreparedStatement pStmt;
    public String query;
    ResultSet rs;
    int shipped;
    int canceled;
    int resolved;
    int disputed;
    int exp_shipped;
    int exp_canceled;
    int exp_resolved;
    int exp_disputed;
    String exp_shippingTime;
    String shippingTime;
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
        assertEquals(rs1.getString("Name"), "SelectAllCustomers", procedureName + " stored procedure does not exist in the database");
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

        query = TestConfig.CALL_ALL_CUSTOMERS_BY_CITY;
        pStmt = con.prepareStatement(query);
        pStmt.setString(1, city);
        rs2 = pStmt.executeQuery();
    }

    @When("I execute the SelectAllCustomersByCityAndPinCode stored procedure with city {string} and pin {string}")
    public void executeSelectAllCustomersByCityAndPin(String city, String pin) throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN);
        cStmt.setString(1, city);
        cStmt.setString(2, pin);
        rs1 = cStmt.executeQuery();

        // Use PreparedStatement instead of hardcoding values
        query = TestConfig.CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN;
        pStmt = con.prepareStatement(query);
        pStmt.setString(1, city);
        pStmt.setString(2, pin);
        rs2 = pStmt.executeQuery();

    }

    @Step("Execute GetOrderByCustomer for customer {customerNumber}")
    @When("I execute the GetOrderByCustomer stored procedure and GetOrderByCustomerQuery with customer Number {string}")
    public void executeGetOrderByCustomer(String customerNumber) throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_GET_ORDER_BY_CUSTOMER_PROCEDURE);
        cStmt.setInt(1, Integer.parseInt(customerNumber));

        cStmt.registerOutParameter(2, Types.INTEGER);
        cStmt.registerOutParameter(3, Types.INTEGER);
        cStmt.registerOutParameter(4, Types.INTEGER);
        cStmt.registerOutParameter(5, Types.INTEGER);
        cStmt.execute();

        shipped = cStmt.getInt(2);
        canceled = cStmt.getInt(3);
        resolved = cStmt.getInt(4);
        disputed = cStmt.getInt(5);

        // 2. Execute equivalent SQL query
        query = TestConfig.CALL_GET_ORDER_BY_CUSTOMER_QUERY;
        pStmt = con.prepareStatement(query);

        // set each status explicitly
        pStmt.setInt(1, Integer.parseInt(customerNumber));
        pStmt.setString(2, "Shipped");

        pStmt.setInt(3, Integer.parseInt(customerNumber));
        pStmt.setString(4, "Canceled");

        pStmt.setInt(5, Integer.parseInt(customerNumber));
        pStmt.setString(6, "Resolved");

        pStmt.setInt(7, Integer.parseInt(customerNumber));
        pStmt.setString(8, "Disputed");

        rs = pStmt.executeQuery();
        rs.next();

        exp_shipped = rs.getInt("shipped");
        exp_canceled = rs.getInt("canceled");
        exp_resolved = rs.getInt("resolved");
        exp_disputed = rs.getInt("disputed");

        System.out.println("Procedure -> Shipped: " + shipped + ", Canceled: " + canceled + ", Resolved: " + resolved + ", Disputed: " + disputed);
        System.out.println("Query     -> Shipped: " + exp_shipped + ", Canceled: " + exp_canceled + ", Resolved: " + exp_resolved + ", Disputed: " + exp_disputed);
    }

    @Then("the stored procedure result should match the query result")
    public void verifyResults() {
        assertEquals(exp_shipped, shipped, "Mismatch in Shipped count");
        assertEquals(exp_canceled, canceled, "Mismatch in Canceled count");
        assertEquals(exp_resolved, resolved, "Mismatch in Resolved count");
        assertEquals(exp_disputed, disputed, "Mismatch in Disputed count");

        System.out.println("Stored procedure results match the query results!");
    }

    @When("I execute the GetCustomerShipping stored procedure and GetCustomerShippingQuery with customer Number {string}")
    public void executeGetCustomerShipping(String customerNo) throws SQLException {
        cStmt = con.prepareCall(TestConfig.CALL_GET_CUSTOMER_SHIPPING_PROCEDURE);
        cStmt.setInt(1, Integer.parseInt(customerNo));

        cStmt.registerOutParameter(2, Types.VARCHAR);
        cStmt.executeQuery();

        shippingTime = cStmt.getString(2);

        query = TestConfig.CALL_GET_CUSTOMER_SHIPPING;
        pStmt = con.prepareStatement(query);

        pStmt.setInt(1, Integer.parseInt(customerNo));

        rs = pStmt.executeQuery();
        rs.next();
        exp_shippingTime = rs.getString("ShippingTime");

    }

    @Then("the stored procedure result should match the query results")
    public void verifyQueryResults() {
        Assert.assertEquals(shippingTime, exp_shippingTime, "Mismatch in Shipped count");
        System.out.println("Shipped: " + exp_shippingTime);
    }

    @Then("the result should match the {string} query")
    public void verifyProcedureResult(String queryName) throws SQLException {
        Assert.assertTrue(TestConfig.compareResultSets(rs1, rs2), "Stored procedure result does not match query: " + queryName);
        System.out.println("Stored procedure result matches: " + queryName);
    }
}
