package ru.otus.atmdepartment.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.atmdepartment.Atm;
import ru.otus.atmdepartment.AtmException;
import ru.otus.atmdepartment.AtmModeException;
import ru.otus.atmdepartment.nominals.Banknote;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AtmImpl implements Atm {
    private static Logger logger = LoggerFactory.getLogger(AtmImpl.class);

    private boolean isServiceMode=true;

    private  final Set<BanknoteBox> initialState = new HashSet<>();

    private  Map<Banknote, BanknoteBox> banknoteBoxes;

    public AtmImpl(final Set<BanknoteBox> banknoteBoxes) {

        for (BanknoteBox box:banknoteBoxes){
            initialState.add(box.copyBox());
        }
        this.banknoteBoxes = banknoteBoxes.stream()
                .collect(Collectors.toMap(BanknoteBox::getNominal, Function.identity()));
    }

    @Override
    public int getBalance() {
        int summ = 0;
        for (BanknoteBox box : banknoteBoxes.values()) {
            summ += box.getSumm();
        }
        return summ;
    }

    @Override
    public List<Banknote> getMoney(int amount) throws AtmException {
        if (isServiceMode) throw new AtmModeException("Atm in Service mode");
        Map<Banknote, Integer> giveBancnote = getBancnoteSet(amount);
        return giveBancnote.keySet().stream()
               .flatMap(n-> banknoteBoxes.get(n).getBancnotes(giveBancnote.get(n)).stream())
               .collect(Collectors.toList());
    }


    private Map<Banknote, Integer> getBancnoteSet(int amount) throws AtmException {

        Object[] array = banknoteBoxes.keySet().stream()
                .filter(x -> (x.getValue() < amount))
                .sorted((a, b) -> b.getValue() - a.getValue())
                .toArray();
        int ostatok = amount;
        Map<Banknote, Integer> moneyMap = new HashMap<>();
        for (Object n : array) {
            Banknote banknote = (Banknote) n;
            int needed = ostatok / banknote.getValue();
            int total = banknoteBoxes.get(n).getCount();
            int canGive = (needed < total) ? needed : total;
            if (total > 0) moneyMap.put(banknote, canGive);
            ostatok = ostatok - canGive * banknote.getValue();
           logger.info("Нужно " + needed + " по " + banknote.getValue() + " остаток " + ostatok);
        }
        if (ostatok == 0) return moneyMap;
            else throw new AtmException("Cannot give money");
    }


    @Override
    public void putMoney(List<Banknote> banknotes) throws AtmException {
        if (isServiceMode) throw new AtmModeException("Atm in Service mode");
        Map<Banknote, Integer> map = banknotes.stream()
                .collect(Collectors.toMap(Function.identity(), c -> Integer.valueOf(1),
                        (c1, c2) -> Integer.valueOf(c1 + c2)));

//        for (Banknote key : map.keySet()) {
//            logger.info(key.name() + " , " + map.get(key));
//        }


        if (!banknoteBoxes.keySet().containsAll(map.keySet())) throw new AtmException("No box for bancnote");
        if (!canPutMoney(map)) throw new AtmException("Cannot put money");

        for (Banknote banknote : map.keySet()) {
            banknoteBoxes.get(banknote).putBanknotes(map.get(banknote));
        }
    }

    @Override
    public Map<Banknote,Integer> report() {
        return banknoteBoxes.keySet().stream()
                .collect(Collectors.toMap(Function.identity(),n->banknoteBoxes.get(n).getCount()));
    }

    private boolean canPutMoney(Map<Banknote, Integer> money) {
        boolean canPutMoney = true;
        for (Banknote banknote : money.keySet()) {
            canPutMoney = canPutMoney && checkBox(banknoteBoxes.get(banknote), money.get(banknote));
        }
        return canPutMoney;
    }

    private boolean checkBox(BanknoteBox box, int count) {
        int newCount = box.getCount() + count;
        return newCount < box.getMaxCount();
    }

    @Override
    public void setServiceMode(boolean mode) {
        isServiceMode = mode;
    }

    @Override
    public boolean getServiceMode() {
        return isServiceMode;
    }

    @Override
    public void encashment(Set<BanknoteBox> set) throws AtmException {
        if (!isServiceMode) throw new AtmModeException("Atm is not in service mode");
        this.banknoteBoxes = set.stream()
                .collect(Collectors.toMap(BanknoteBox::getNominal, Function.identity()));
    }

    @Override
    public void toInitialState() {

        this.banknoteBoxes.clear();

        this.banknoteBoxes = initialState.stream()
                .collect(Collectors.toMap(BanknoteBox::getNominal, Function.identity().andThen(x->{
                    BanknoteBox b = (BanknoteBox) x;
                    return b.copyBox();
                })));
    }
}
