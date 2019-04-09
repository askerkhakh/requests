package com.example.requests.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Person {

    private String surname;
    private String name;
    private String patronymic;

    @Embedded
    private Passport passport;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }
}
