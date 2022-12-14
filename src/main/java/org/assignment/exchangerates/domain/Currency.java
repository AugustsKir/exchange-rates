package org.assignment.exchangerates.domain;

import java.math.BigDecimal;

public class Currency {
    private String name;
    private BigDecimal rate;

    public Currency(String name, BigDecimal rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
