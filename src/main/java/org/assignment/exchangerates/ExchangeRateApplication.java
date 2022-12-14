package org.assignment.exchangerates;

import io.javalin.Javalin;

public class ExchangeRateApplication {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/feed", ExchangeRates::output);
    }
}
