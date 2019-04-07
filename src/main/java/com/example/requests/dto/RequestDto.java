package com.example.requests.dto;

import org.springframework.lang.Nullable;

import java.util.Objects;

public class RequestDto {

    @Nullable
    private Long id;

    private PersonDto person;

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDto that = (RequestDto) o;
        return Objects.equals(id, that.id) &&
                person.equals(that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }
}
