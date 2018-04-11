package com.test.metro.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Directions {
    
    @JsonProperty("Text")
    private String text;
    
    @JsonProperty("Value")
    private String value;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
