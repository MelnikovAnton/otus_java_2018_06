package ru.otus.atm;

import ru.otus.atm.elements.BanknoteBox;
import ru.otus.atm.nominals.Bancnote;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Atm {
    int getBalance();
    List<Bancnote> getMoney(int amount) throws AtmException;
    void putMoney(List<Bancnote> banknotes) throws AtmException;
    Map<Bancnote,Integer> report();
    void setServiceMode(boolean mode);
    boolean getServiceMode();
    void encashment(Set<BanknoteBox> set) throws AtmException;
    void toInitialState();


}
