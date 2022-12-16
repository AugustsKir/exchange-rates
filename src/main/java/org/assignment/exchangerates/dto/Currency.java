package org.assignment.exchangerates.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Currency {
    private String currency;
    private BigDecimal rate;
    private String date;


    public Currency(String currency, BigDecimal rate, String date) {
        this.currency = currency;
        this.rate = rate;
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency1 = (Currency) o;
        return Objects.equals(currency, currency1.currency) && Objects.equals(rate, currency1.rate) && Objects.equals(date, currency1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, rate, date);
    }
}
