package com.example.requests.dto;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RequestDto {

    @Nullable
    private Long id;

    private PersonDto person;

    private List<DocumentDto> documents;

    private String serviceName;

    private LocalDate date;

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

    public List<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
