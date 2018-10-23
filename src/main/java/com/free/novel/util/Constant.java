package com.free.novel.util;

public enum Constant {
    image("image_"),
    contents("contents_"),
    ;

    private String key;
    private String value;

    Constant(String key) {
        this.key = key;
    }

    Constant(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
