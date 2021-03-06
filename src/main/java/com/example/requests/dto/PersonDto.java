package com.example.requests.dto;

import java.util.Objects;

public class PersonDto {

    private String surname;
    private String name;
    private String patronymic;

    private PassportDto passport;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(surname, personDto.surname) &&
                Objects.equals(name, personDto.name) &&
                Objects.equals(patronymic, personDto.patronymic) &&
                Objects.equals(passport, personDto.passport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, patronymic, passport);
    }

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

    public PassportDto getPassport() {
        return passport;
    }

    public void setPassport(PassportDto passport) {
        this.passport = passport;
    }
}
