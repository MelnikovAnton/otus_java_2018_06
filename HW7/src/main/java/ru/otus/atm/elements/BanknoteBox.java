package ru.otus.atm.elements;

import ru.otus.atm.AtmException;
import ru.otus.atm.nominals.Bancnote;

import java.util.*;

public class BanknoteBox{

    private final int MaxCount;
    private final Queue<Bancnote> bancnotes = new ArrayDeque<>();
    private final Bancnote bancnote;


    public  BanknoteBox copyBox(){
        BanknoteBox copy = new BanknoteBox(this.MaxCount, this.bancnote);
       // ArrayDeque<Bancnote> bancnotes = new ArrayDeque<>(this.bancnotes);
        copy.bancnotes.addAll(this.bancnotes);
        return copy;
    }

    public BanknoteBox(int MaxCount, Bancnote bancnote) {
        this.MaxCount = MaxCount;
        this.bancnote = bancnote;
    }

    public int getCount(){
        return bancnotes.size();
    }

    public int getSumm(){
        return getCount()* bancnote.getValue();
    }


    public void putBancnote(Bancnote bancnote) throws AtmException {
        if (!bancnotes.offer(bancnote)) throw new AtmException("Cannot put bancnote");
    }

    public void putBancknotes(int count) throws AtmException{
        for (int i=0;i<count;i++){
            putBancnote(bancnote);
        }
    }

    public List<Bancnote> getBancnotes(int count){
        List<Bancnote> result= new ArrayList<>();
        for (int i=0;i<count;i++){
            result.add(bancnotes.remove());
        }
        return result;
    }

    public Bancnote getBancnote(){
        return bancnotes.remove();
    }



    public int getMaxCount(){
        return MaxCount;
    }


    public List<Bancnote> report(){
        return new ArrayList<Bancnote>(bancnotes);
    }

    public final Bancnote getNominal(){
        return this.bancnote;
    }
}

