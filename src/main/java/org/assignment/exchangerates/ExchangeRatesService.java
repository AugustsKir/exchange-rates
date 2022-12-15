package org.assignment.exchangerates;

import io.javalin.http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;

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

    public static void output(Context context) throws IOException, SQLException {
        BufferedReader in = new BufferedReader(new InputStreamReader(address.openStream()));
        String line;

        while ((line = in.readLine()) != null) {
            if (line.contains("<guid")) {
                int dateStart = line.indexOf("lv/#");
                int dateEnd = line.indexOf("</guid");
                String date = line.substring(dateStart, dateEnd);
                System.out.println(date);
            }
            if (line.contains("<description>") && line.contains("AUD")) {
                int start = line.indexOf("AUD");
                int end = line.indexOf("]]><");
                String temp = line.substring(start, end);
                String[] arr = temp.split(" ");
                for (int i = 0; i < arr.length; i += 2) {
                    db.insertSQL("INSERT INTO exchange_rates (currency, rate) VALUES ('" + arr[i] + "', " + arr[i + 1] + ")");
                }
                Database.closeConnection();

            }
        }
        in.close();


    }
    public static void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `exchange_rates` (" +
                "`currency` VARCHAR(50) PRIMARY KEY NOT NULL," +
                "`rate` DECIMAL(19, 6) NOT NULL" +
                "`date` DECIMAL(19, 6) NOT NULL" +
                ");";
        db.insertSQL(sql);
    }
}