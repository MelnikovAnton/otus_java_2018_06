package ru.otus.gc;

public class MemoryLeaker {

    final byte[] bytes = new byte[Main.Byte_Size];
    public final String key;
    public MemoryLeaker(String key) { this.key = key; }


    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
