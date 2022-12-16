package org.assignment.exchangerates;

import java.sql.*;

public class Database {
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String URL = "jdbc:mariadb://localhost:3306/exchange-rate-db";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    public Database() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertSQL(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public ResultSet selectSQL(String sql) throws SQLException {
        if (connection == null) {
            throw new SQLException("No connection");
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        statement.close();
        resultSet.close();
        return resultSet;
    }

}
