package org.assignment.exchangerates;

import com.google.gson.Gson;
import io.javalin.http.Context;
import org.assignment.exchangerates.dto.Currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class ExchangeRatesService {
    private static final URL address;
    private static final Database db = new Database();

    static {
        try {
            address = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fetchDataToDB() throws IOException, SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        BufferedReader in = new BufferedReader(new InputStreamReader(address.openStream()));
        String line;
        String stringDate;
        LocalDate date = null;

        while ((line = in.readLine()) != null) {
            if (line.contains("<guid")) {
                int dateStart = line.indexOf("lv/#");
                int dateEnd = line.indexOf("</guid");
                stringDate = line.substring(dateStart, dateEnd).replaceAll("[^\\d.]", "");
                date = LocalDate.parse(stringDate + "." + LocalDate.now().getYear(), formatter);
            }
            if (line.contains("<description>") && line.contains("AUD")) {
                int start = line.indexOf("AUD");
                int end = line.indexOf("]]><");
                String temp = line.substring(start, end);
                String[] arr = temp.split(" ");
                for (int i = 0; i < arr.length; i += 2) {
                    db.insertSQL("INSERT INTO exchange_rates (currency, rate, date) VALUES ('" + arr[i] + "', " + arr[i + 1] + ", '" + date + "')");
                }

            }
        }
        in.close();


    }

    public static void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `exchange_rates` (" +
                "`currency` VARCHAR(50) NOT NULL," +
                "`rate` DECIMAL(19, 6) NOT NULL," +
                "`date` DATE NOT NULL" +
                ");";
        db.insertSQL(sql);
    }

    public static void clearDB() throws SQLException {
        String sql = "DROP TABLES IF EXISTS `exchange_rates`";
        db.insertSQL(sql);
    }

    public static void selectRate(Context context) {
        String currency = context.pathParam("curr");
        String sql = "SELECT * FROM `exchange_rates` WHERE currency like '" + currency + "'";
        List<Currency> currencyList = getListFromSqlScript(sql);
        String json = new Gson().toJson(currencyList);
        context.result(json);
    }

    public static void selectAll(Context context) {
        String sql = "SELECT * FROM `exchange_rates`";
        List<Currency> currencyList = getListFromSqlScript(sql);
        String json = new Gson().toJson(currencyList);
        context.result(json);
    }

    private static List<Currency> getListFromSqlScript(String sql) {
        try {
            ResultSet set = db.selectSQL(sql);
            List<Currency> currencyList = new ArrayList<>();
            while (set.next()) {
                currencyList.add(new Currency(set.getString("currency"), set.getBigDecimal("rate"), set.getString("date")));
            }
            return currencyList;
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
    }
}
