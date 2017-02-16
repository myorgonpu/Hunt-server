package main.java;

/**
 * Created by Slon on 16.02.2017.
 */
public enum Role {
    HUNTER("HUNTER"),
    RUNNER("RUNNER");

    private String name;

    public String getName() {
        return name;
    }

    Role(String name) {

        this.name = name;
    }
}
