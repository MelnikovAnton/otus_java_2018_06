package ru.otus.atmdepartment;

import org.junit.Before;
import org.junit.Test;
import ru.otus.atmdepartment.elements.AtmImpl;
import ru.otus.atmdepartment.elements.BanknoteBox;
import ru.otus.atmdepartment.nominals.Bancnote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MainTest {

    private Atm myAtm;
    private DepartmentAtm department;

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

        ArrayList<Atm> dep = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dep.add(new AtmImpl(casets));
        }

        department= new DepartmentAtm(dep);
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
    public void testDepartmentBalanse(){

        System.out.println(department.getBalanse());
        assertEquals(department.getBalanse(),myAtm.getBalance()*10);
    }

    @Test
    public void testDepartmentInitState(){
        System.out.println(department.getBalanse());
        assertEquals(department.getBalanse(),myAtm.getBalance()*10);
        department.restoreInitialState();
        System.out.println(department.getBalanse());
        myAtm.toInitialState();
        assertEquals(department.getBalanse(),myAtm.getBalance()*10);
    }

}