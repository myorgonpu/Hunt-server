package main.java.messaging;


public enum Target {
    REGISTRATION("registration"),
    AUTHORIZATION("authorization"),
    LOCATION("location"),
    ENCOUNTER("encounter");

    private String name;

    Target(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
