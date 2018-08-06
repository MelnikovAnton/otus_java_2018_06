package ru.otus.gc.classLoaderLeek;

public class BigStaticClass {
    public static byte[] baytes= new byte[200];
  //  public  byte[] baytesInstance= new byte[32*1_000_000];


    public static byte[] getBaytes() {
        return baytes;
    }
}
