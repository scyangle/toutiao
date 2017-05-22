package com.scy.async;

public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),;
    private Integer value;
    EventType(Integer value) {
        this.value=value;
    }
}
