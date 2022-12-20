package org.assignment.exchangerates;

import com.google.gson.Gson;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import io.javalin.http.Context;
import org.assignment.exchangerates.dto.Currency;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class ExchangeRatesService {
    private static final ExchangeRateRepository db = new ExchangeRateRepository();

    public static void fetchDataToDB() throws IOException, FeedException, SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu");
        URL feedSource = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        for (Object e : feed.getEntries()) {
            LocalDate date = LocalDate.parse(((SyndEntry) e).getPublishedDate().toString(), formatter);
            String[] currencyArr = ((SyndEntry) e).getDescription().getValue().split(" ");
            for (int i = 0; i < currencyArr.length; i += 2) {
                db.insertSQL("INSERT INTO exchange_rates (currency, rate, date) VALUES ('" + currencyArr[i] + "', " + currencyArr[i + 1] + ", '" + date + "')");
            }
        }
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

    public static void selectLatest(Context context) {
        String sql = "SELECT * FROM exchange_rates WHERE `date`  = (SELECT MAX(`date`) FROM exchange_rates)";
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
