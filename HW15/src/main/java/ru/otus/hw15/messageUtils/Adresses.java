package ru.otus.hw15.messageUtils;

public enum Adresses {
    FRONT_SERVICE("Front"),DB_SERVICE("DB");

    Adresses(String name) {
        this.name = name;
    }

    private String name;

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
