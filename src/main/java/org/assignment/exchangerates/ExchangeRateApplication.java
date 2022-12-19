package org.assignment.exchangerates;


import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExchangeRateApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Application started...");
        if (args[0].equals("load_data")) {
            try {
                ExchangeRatesService.createTables();
                ExchangeRatesService.fetchDataToDB();
                LOGGER.info("Data loaded...");

            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
        if (args[0].equals("enable_endpoints")) {
            Javalin app = Javalin.create().start(8080);
            app.get("/rates/{curr}", ExchangeRatesService::selectRate);
            app.get("/rates/", ExchangeRatesService::selectLatest);
            LOGGER.info("Endpoints enabled...");
        }
        if (args[0].equals("clear_data")) {
            try {
                ExchangeRatesService.clearDB();
                LOGGER.info("Data cleared...");
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }
}
