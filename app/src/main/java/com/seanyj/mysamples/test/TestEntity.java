package com.seanyj.mysamples.test;

public class TestEntity {
    private String title;
    private String value;

    public TestEntity() {
    }

    public TestEntity(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
