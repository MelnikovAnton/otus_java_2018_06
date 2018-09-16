package ru.otus.atm;

import org.junit.Before;
import org.junit.Test;
import ru.otus.atm.elements.AtmImpl;
import ru.otus.atm.elements.BanknoteBox;
import ru.otus.atm.nominals.Bancnote;

import java.util.*;

import static org.junit.Assert.*;

public class MainTest {

    private Atm myAtm;

    @Before
    public void init() throws AtmException {
        Set<BanknoteBox> casets= new HashSet<>();
        casets.add(new BanknoteBox(100, Bancnote.RUB_100));
        casets.add(new BanknoteBox(100, Bancnote.RUB_1000));
        casets.add(new BanknoteBox(100, Bancnote.RUB_500));

        for (BanknoteBox box:casets){
            box.putBancknotes(50);
        }

        myAtm = new AtmImpl(casets);

   //     System.out.println(myAtm.getBalance());

        List<Bancnote> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(Bancnote.RUB_100);
            list.add(Bancnote.RUB_500);
        }

        for (int i=0;i<30;i++){
            list.add(Bancnote.RUB_500);
        }
        list.add(Bancnote.RUB_1000);
        myAtm.setServiceMode(false);
        myAtm.putMoney(list);

    }

    @Test
    public void testGetMoney() throws AtmException {
        int balanseBefor=myAtm.getBalance();
        int amount = 10100;

            myAtm.getMoney(amount);

        assertEquals(balanseBefor-amount,myAtm.getBalance());
    }

    @Test
    public void testPutMoney() throws AtmException {
        int balanseBefor=myAtm.getBalance();
        //int amount = 10100;
        List<Bancnote> put=new ArrayList<>();
        put.add(Bancnote.RUB_1000);
        put.add(Bancnote.RUB_100);
        put.add(Bancnote.RUB_500);
            myAtm.putMoney(put);

//        Map<Bancnote,Integer> map = myAtm.report();
//        map.keySet().forEach(x->System.out.println(x+" "+map.get(x)));

        assertEquals(balanseBefor+1600,myAtm.getBalance());
    }

    @Test(expected = AtmException.class)
    public void putMoreThenCan() throws AtmException{
        List<Bancnote> put=new ArrayList<>();
        for (int i=0;i<100;i++){
            put.add(Bancnote.RUB_100);
        }
        myAtm.putMoney(put);
    }

    @Test(expected = AtmException.class)
    public void putWrondBancknote() throws AtmException{
        List<Bancnote> put=new ArrayList<>();
        for (int i=0;i<100;i++){
            put.add(Bancnote.RUB_50);
        }
        myAtm.putMoney(put);
    }

    @Test
    public void testToInitialMode() throws AtmException {
     //   myAtm.toInitialState();
        int before=myAtm.getBalance();
        List<Bancnote> list = new ArrayList<>();
        list.add(Bancnote.RUB_1000);
        myAtm.putMoney(list);
        int after=myAtm.getBalance();
        assertEquals(before,after-1000);
        myAtm.toInitialState();
 //       System.out.println("ert");
        assertEquals(80000,myAtm.getBalance());
    }

    @Test(expected = AtmException.class)
    public void testEncashmentWrongState() throws AtmException {
        myAtm.setServiceMode(false);
        Set<BanknoteBox> casets= new HashSet<>();
        myAtm.encashment(casets);
    }

    @Test
    public void testEncashment() throws AtmException {
        myAtm.setServiceMode(true);
        Set<BanknoteBox> casets= new HashSet<>();
        casets.add(new BanknoteBox(100, Bancnote.RUB_100));
        casets.add(new BanknoteBox(100, Bancnote.RUB_1000));
        casets.add(new BanknoteBox(100, Bancnote.RUB_500));
        casets.add(new BanknoteBox(100, Bancnote.RUB_2000));
        casets.add(new BanknoteBox(100, Bancnote.RUB_10));

        for (BanknoteBox box:casets){
            box.putBancknotes(50);
        }

        myAtm.encashment(casets);

        myAtm.setServiceMode(false);
        List<Bancnote> money = myAtm.getMoney(50);

        assertEquals(180450,myAtm.getBalance());
    }

}