package ru.otus.atmdepartment.elements;

import ru.otus.atmdepartment.AtmException;
import ru.otus.atmdepartment.nominals.Banknote;

import java.util.*;

public class BanknoteBox{

    private final int MaxCount;
    private final Queue<Banknote> banknotes = new ArrayDeque<>();
    private final Banknote banknote;


    public  BanknoteBox copyBox(){
        BanknoteBox copy = new BanknoteBox(this.MaxCount, this.banknote);
       // ArrayDeque<Banknote> banknotes = new ArrayDeque<>(this.banknotes);
        copy.banknotes.addAll(this.banknotes);
        return copy;
    }

    public BanknoteBox(int MaxCount, Banknote banknote) {
        this.MaxCount = MaxCount;
        this.banknote = banknote;
    }

    public int getCount(){
        return banknotes.size();
    }

    public int getSumm(){
        return getCount()* banknote.getValue();
    }


    public void putBanknote(Banknote banknote) throws AtmException {
        if (!banknotes.offer(banknote)) throw new AtmException("Cannot put banknote");
    }

    public void putBanknotes(int count) throws AtmException{
        for (int i=0;i<count;i++){
            putBanknote(banknote);
        }
    }

    public List<Banknote> getBancnotes(int count){
        List<Banknote> result= new ArrayList<>();
        for (int i=0;i<count;i++){
            result.add(banknotes.remove());
        }
        return result;
    }

    public Banknote getBanknote(){
        return banknotes.remove();
    }



    public int getMaxCount(){
        return MaxCount;
    }


    public List<Banknote> report(){
        return new ArrayList<Banknote>(banknotes);
    }

    public final Banknote getNominal(){
        return this.banknote;
    }
}

