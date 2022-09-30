package com.example.tiktok.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;

@Getter
@Setter
public class Respondata {
    private Object data;
    private String username;

    public Respondata() {
    }

    public Respondata(Object data, String username) {
        this.data = data;
        this.username = username;
    }
}
