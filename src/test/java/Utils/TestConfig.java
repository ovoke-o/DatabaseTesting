package Utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConfig {

    // Database Connection Strings
    public static final String LOCAL_URL = "jdbc:mysql://localhost:3306/classicmodels?useSSL=false";
    public static final String AWS_URL = "jdbc:mysql://classic-models.cgp2k0c8azxc.us-east-1.rds.amazonaws.com:3306/classicmodels?useSSL=false&allowPublicKeyRetrieval=true";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "classic_models";

    // SQL Statements
    public static final String SELECT_ALL_CUSTOMERS = "SHOW PROCEDURE STATUS WHERE Name = 'SelectAllCustomers'";
    public static final String SELECT_ALL_FROM_CUSTOMERS = "select * from customers";
    public static final String CALL_ALL_CUSTOMERS_PROCEDURE = "{CALL SelectAllCustomers()}";
    public static final String CALL_ALL_CUSTOMERS_BY_CITY_PROCEDURE = "{call SelectAllCustomersByCity(?)}";
    public static final String CALL_ALL_CUSTOMERS_BY_CITY = "SELECT * FROM customers WHERE city = 'Singapore'";
    public static final String CALL_ALL_CUSTOMERS_BY_CITY_AND_PINCODE_PROCEDURE = "{call SelectAllCustomersByCityAndPin(?, ?)}";
    public static final String CALL_ALL_CUSTOMERS_BY_CITY_AND_PIN = "SELECT * FROM customers WHERE city = 'Singapore' and postalCode = '079903'";

    public static boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        while (resultSet1.next()) {
            resultSet2.next();
            int count = resultSet1.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Connection dbConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Toggle between LOCAL and AWS
        String url = AWS_URL;  // or DBConfig.LOCAL_URL
        Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);

        System.out.println("Connected to DB: " + url);
        return conn;
    }

}
