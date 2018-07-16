package ru.otus.HW2.util;

public enum PrimitiveSize {

    BYTE("byte",1),
    BOOLEAN("boolean",1),
    SHORT("short",2),
    CHAR("char",2),
    INT("int",4),
    FLOAT("float",4),
    LONG("long",8),
    DOUBLE("double",4),
    Byte("Byte",1),
    Boolean("Boolean",1),
    Short("Short",2),
    Char("Char",2),
    Integer("Integer",4),
    Float("Float",4),
    Long("Long",8),
    Double("Double",4);

    private static final int MIN_REF = 4;
    private String name;
    private int size;

    PrimitiveSize(String name,int size){
        this.name=name;
        this.size=size;
    }
    
    public static int getSizeByName(String name){
        System.out.println("searhing name " + name);
        for (PrimitiveSize s:PrimitiveSize.values()) {
            if (name.equals(s.name)) {
                System.out.println("SIZE " + s.size);
                return s.size;
            }
        } return 0;
    }
}
