package ru.otus.atmdepartment.nominals;

public enum Banknote {

    RUB_10(10), RUB_50(50), RUB_100(100), RUB_200(200),
    RUB_500(500),RUB_1000(1000), RUB_2000(2000),  RUB_5000(5000);


    private int value;

    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    static public Banknote getNominal(int value) {
        for (Banknote nom: Banknote.values()) {
            if (nom.getValue() == value) {
                return nom;
            }
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}
