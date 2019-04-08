package com.example.requests.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Passport {
    private String series;
    private String number;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
