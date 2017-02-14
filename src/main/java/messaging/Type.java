package main.java.messaging;


public enum  Type {
    REQUEST("request"),
    RESPONSE("response"),
    EVENT("event");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
