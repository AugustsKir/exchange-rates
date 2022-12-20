package org.assignment.exchangerates;

import com.google.gson.Gson;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import io.javalin.http.Context;
import org.assignment.exchangerates.dto.Currency;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class ExchangeRatesService {

    public static void fetchDataToDB() throws IOException, FeedException {
        URL feedSource = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        ExchangeRateRepository.insertCurrencyData(feed);

    }

    public static void selectRate(Context context) {
        String currency = context.pathParam("curr");
        List<Currency> currencyList = ExchangeRateRepository.selectRate(currency);
        String json = new Gson().toJson(currencyList);
        context.result(json);
    }

    public static void selectLatest(Context context) {
        List<Currency> currencyList = ExchangeRateRepository.selectLatest();
        String json = new Gson().toJson(currencyList);
        context.result(json);
    }


}
