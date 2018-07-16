package ru.otus.HW2;

import org.junit.Test;
import ru.otus.HW2.impl.ArrayGetSizeImpl;
import ru.otus.HW2.impl.InstrumentationGetSizeImpl;
import ru.otus.HW2.impl.UnsafeGetSizeImpl;

import static org.junit.Assert.*;

public class SizeTester {

    private static GetSize arrSize= new ArrayGetSizeImpl();
    private static GetSize unsafeSize = new UnsafeGetSizeImpl();
 //   private static GetSize instrSize = new InstrumentationGetSizeImpl();

    @Test
    public void testArrayObject(){
        assertEquals(16L,arrSize.getSize(()->new Object()));
    }

    @Test
    public void testUnsafeObject(){
        assertEquals(16L,unsafeSize.getSize(()->new Object()));
    }

    @Test
    public void testArrEmptyString(){
        assertEquals(24L,arrSize.getSize(()->new String()));
    }

    @Test
    public void testUnsafeEmptyString(){
        assertEquals(24L,unsafeSize.getSize(()->new String()));
    }


    private long[] getSizes(Creator c){
        long[] result=new long[2];
        result[0]=arrSize.getSize(c);
        result[1]=unsafeSize.getSize(c);
   //     result[2]=instrSize.getSize(c);

        return result;
    }

    private boolean elmEquals(long[] r){
        return (r[0]==r[1]);
    }

    private boolean expectedSize(long exp,long[] r){
        return elmEquals(r) && r[0] == exp;
    }


}
