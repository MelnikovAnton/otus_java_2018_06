package ru.otus.atm;

import ru.otus.atm.elements.AtmImpl;
import ru.otus.atm.elements.BanknoteBox;
import ru.otus.atm.nominals.Bancnote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        System.out.println("Main");

        Set<BanknoteBox> casets= new HashSet<>();
        casets.add(new BanknoteBox(100, Bancnote.RUB_100));
        casets.add(new BanknoteBox(100, Bancnote.RUB_1000));
        casets.add(new BanknoteBox(100, Bancnote.RUB_500));
        Atm myAtm = new AtmImpl(casets);

        System.out.println(myAtm.getBalance());

        List<Bancnote> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(Bancnote.RUB_100);
            list.add(Bancnote.RUB_500);
        }

        for (int i=0;i<30;i++){
            list.add(Bancnote.RUB_500);
        }

        try {
            myAtm.putMoney(list);
        } catch (AtmException e) {
            e.printStackTrace();
        }

        System.out.println(myAtm.getBalance());


        try {
            myAtm.getMoney(10110);
        } catch (AtmException e) {
            e.printStackTrace();
        }

        System.out.println(myAtm.getBalance());

    }
}
