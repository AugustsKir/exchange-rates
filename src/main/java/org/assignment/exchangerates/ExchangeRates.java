package org.assignment.exchangerates;

import io.javalin.http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

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
        while ((line = in.readLine()) != null) {
            if (line.contains("<description>") && line.contains("AUD")) {
                int start = line.indexOf("AUD");
                int end = line.indexOf("]]><");
                String temp = line.substring(start, end);
                System.out.println(temp);
            }
        }
        in.close();


    }
}
