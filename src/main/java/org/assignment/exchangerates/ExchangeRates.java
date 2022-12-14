package org.assignment.exchangerates;

import io.javalin.http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

public final class ExchangeRates {
    private static final String URL = "https://www.bank.lv/vk/ecb_rss.xml";
    private static final URL address;

    static {
        try {
            address = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    Connection connection = null;



    public static void output(Context context) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(address.openStream()));
        String line;
        HashMap<String, BigDecimal> map = new HashMap<>();
        while ((line = in.readLine()) != null) {
            if (line.contains("<description>") && line.contains("AUD")) {
                int start = line.indexOf("AUD");
                int end = line.indexOf("]]><");
                String temp = line.substring(start, end);
                String[] arr = temp.split(" ");
                for (int i = 0; i < arr.length ; i += 2) {
                    map.put(arr[i], new BigDecimal(arr[i + 1]));
                }
            }
        }
        in.close();
        context.result(map.toString());


    }
}
