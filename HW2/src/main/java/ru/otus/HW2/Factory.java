package ru.otus.HW2;

public class Factory {

    private Creator creator;
    private String description;

    public Factory(Creator c,String description){
        this.creator=c;
        this.description=description;
    }

    public Creator getCreator() {
        return creator;
    }

    public String getDescription(){
        return description;
    }
}
