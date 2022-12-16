package org.assignment.exchangerates;

import io.javalin.Javalin;

import java.util.Scanner;

public class ExchangeRateApplication {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        Scanner in = new Scanner(System.in);
        System.out.println("Recent exchange rate app [Data fetched from bank.lv]");

        System.out.println("Press 1 to load data to the database...");
        System.out.println("Press 2 to clear the database...");
        System.out.println("Press 3 to make the endpoints available...");
        System.out.println("Press 4 to shut down the application...");
        while (true) {
            int input = in.nextInt();
            switch (input) {
                case 1:
                    try {
                        ExchangeRatesService.createTables();
                        ExchangeRatesService.fetchDataToDB();
                        System.out.println("Data inserted successfully");
                        break;

                    } catch (Exception e) {
                        throw new RuntimeException("Could not create tables...");
                    }
                case 2:
                    try {
                        ExchangeRatesService.clearDB();
                        System.out.println("Data cleared successfully");
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException("Could not create tables...");
                    }
                case 3:
                    try {
                        app.get("/rates", ExchangeRatesService::selectAll);
                        app.get("/rates/{curr}", ExchangeRatesService::selectRate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Endpoints enabled");
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
        }


    }
}
