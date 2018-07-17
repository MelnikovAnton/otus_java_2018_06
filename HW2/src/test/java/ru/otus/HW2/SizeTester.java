package ru.otus.HW2;

import org.junit.Test;
import ru.otus.HW2.impl.ArrayGetSizeImpl;

import static org.junit.Assert.*;

public class SizeTester {

    private static GetSize arrSize= new ArrayGetSizeImpl();

 //   private static GetSize instrSize = new InstrumentationGetSizeImpl();

    @Test
    public void testArrayObject(){
        assertEquals(16L,arrSize.getSize(()->new Object()));
    }


    @Test
    public void testArrEmptyString(){
        assertEquals(24L,arrSize.getSize(()->new String()));
    }


    private boolean elmEquals(long[] r){
        return (r[0]==r[1]);
    }

    private boolean expectedSize(long exp,long[] r){
        return elmEquals(r) && r[0] == exp;
    }


}
