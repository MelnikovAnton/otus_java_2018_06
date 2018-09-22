package ru.otus.atmdepartment;



import ru.otus.atmdepartment.elements.BanknoteBox;
import ru.otus.atmdepartment.nominals.Banknote;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Atm {
    int getBalance();
    List<Banknote> getMoney(int amount) throws AtmException;
    void putMoney(List<Banknote> banknotes) throws AtmException;
    Map<Banknote,Integer> report();
    void setServiceMode(boolean mode);
    boolean getServiceMode();
    void encashment(Set<BanknoteBox> set) throws AtmException;
    void toInitialState();


}
