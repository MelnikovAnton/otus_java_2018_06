package ru.otus.atmdepartment;

import org.junit.Before;
import org.junit.Test;
import ru.otus.atmdepartment.elements.AtmImpl;
import ru.otus.atmdepartment.elements.BanknoteBox;
import ru.otus.atmdepartment.nominals.Banknote;

import java.util.*;

import static org.junit.Assert.*;

public class MainTest {

    private Atm myAtm;
    private DepartmentAtm department;

    @Before
    public void init() throws AtmException {
        Set<BanknoteBox> cassettes= new HashSet<>();
        cassettes.add(new BanknoteBox(100, Banknote.RUB_100));
        cassettes.add(new BanknoteBox(100, Banknote.RUB_1000));
        cassettes.add(new BanknoteBox(100, Banknote.RUB_500));

        for (BanknoteBox box:cassettes){
            box.putBanknotes(50);
        }

        myAtm = new AtmImpl(cassettes);

        ArrayList<Atm> dep = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dep.add(new AtmImpl(cassettes));
        }

        department= new DepartmentAtm(dep);
        //     System.out.println(myAtm.getBalance());

        List<Banknote> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(Banknote.RUB_100);
            list.add(Banknote.RUB_500);
        }

        for (int i=0;i<30;i++){
            list.add(Banknote.RUB_500);
        }
        list.add(Banknote.RUB_1000);
        myAtm.setServiceMode(false);
        myAtm.putMoney(list);

    }

    @Test
    public void testDepartmentBalanse(){

        System.out.println(department.getBalance());
        assertEquals(department.getBalance(),myAtm.getBalance()*10);
    }

    @Test
    public void testDepartmentInitState(){
        System.out.println(department.getBalance());
        assertEquals(department.getBalance(),myAtm.getBalance()*10);
        department.restoreInitialState();
        System.out.println(department.getBalance());
        myAtm.toInitialState();
        assertEquals(department.getBalance(),myAtm.getBalance()*10);
    }


    @Test
    public void testRestoreTwoTimes() throws AtmException {
        Set<BanknoteBox> cassettes = new HashSet<>();
        cassettes.add(new BanknoteBox(10, Banknote.RUB_100));
        for (BanknoteBox box : cassettes) {
            box.putBanknotes(10);
        }
        Atm atm = new AtmImpl(cassettes);
        atm.setServiceMode(false);
        DepartmentAtm department = new DepartmentAtm(Arrays.asList(atm));

        assertEquals(1000, department.getBalance());

        atm.getMoney(300);
        assertEquals(700, department.getBalance());

        department.restoreInitialState();
        assertEquals(1000, department.getBalance());

        // Снимаем после первого восстановления
        atm.getMoney(200);
        assertEquals(800, department.getBalance());

        // Восстанавливаем второй раз
        department.restoreInitialState();
        assertEquals(1000, department.getBalance());
    }

}