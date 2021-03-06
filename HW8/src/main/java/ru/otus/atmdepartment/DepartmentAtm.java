package ru.otus.atmdepartment;


import java.util.List;

public class DepartmentAtm {

    private List<Atm> atms;

    public DepartmentAtm (List<Atm> list){
        this.atms=list;
    }

    public int getBalance(){
        int result=0;
        for (Atm atm:atms){
            result +=atm.getBalance();
        }
        return result;
    }
    public void restoreInitialState(){
        for (Atm atm:atms){
            atm.setServiceMode(true);
            atm.toInitialState();
            atm.setServiceMode(false);
        }
    }
}
