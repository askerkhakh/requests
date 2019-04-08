package com.example.requests.entity;

import javax.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
