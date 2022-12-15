package org.assignment.exchangerates;

import io.javalin.Javalin;

public class ExchangeRateApplication {
    public static void main(String[] args) {
        try {
            ExchangeRatesService.createTables();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Javalin app = Javalin.create().start(8080);
        app.get("/feed", ExchangeRatesService::output);
    }
}
