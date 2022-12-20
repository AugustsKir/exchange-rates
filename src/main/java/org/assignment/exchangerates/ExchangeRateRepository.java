package org.assignment.exchangerates;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.assignment.exchangerates.dto.Currency;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    private static final String URL = "jdbc:mariadb://currency-database:3306/currency-database";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    public static void createTables() {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `exchange_rates` (" +
                    "`currency` VARCHAR(50) NOT NULL," +
                    "`rate` DECIMAL(19, 6) NOT NULL," +
                    "`date` DATE NOT NULL" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearDB() {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = con.createStatement()) {
            statement.executeUpdate("DROP TABLES IF EXISTS `exchange_rates`");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCurrencyData(SyndFeed feed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu");
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = con.createStatement()) {
            for (Object e : feed.getEntries()) {
                LocalDate date = LocalDate.parse(((SyndEntry) e).getPublishedDate().toString(), formatter);
                String[] currencyArr = ((SyndEntry) e).getDescription().getValue().split(" ");
                for (int i = 0; i < currencyArr.length; i += 2) {
                    statement.executeUpdate("INSERT INTO exchange_rates (currency, rate, date) VALUES ('" + currencyArr[i] + "', " + currencyArr[i + 1] + ", '" + date + "')");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Currency> selectRate(String currency) {
        String sql = "SELECT * FROM `exchange_rates` WHERE currency like '" + currency + "'";
        return getListFromSqlScript(sql);
    }

    public static List<Currency> selectLatest() {
        String sql = "SELECT * FROM exchange_rates WHERE `date`  = (SELECT MAX(`date`) FROM exchange_rates)";
        return getListFromSqlScript(sql);
    }

    private static List<Currency> getListFromSqlScript(String sql) {
        List<Currency> currencyList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = con.createStatement()) {
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                currencyList.add(new Currency(set.getString("currency"), set.getBigDecimal("rate"), set.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyList;
    }

}
