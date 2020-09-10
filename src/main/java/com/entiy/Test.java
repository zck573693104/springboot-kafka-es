package com.entiy;

import lombok.Data;

@Data
public class Test {
    private String name;
    private String code;

    public Test() {
    }

    public Test(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
