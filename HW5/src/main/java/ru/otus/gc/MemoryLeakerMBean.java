package ru.otus.gc;

public interface MemoryLeakerMBean {

    int getByteSize();

    void setByteSize(int i);

    void clean();

    int getCount();
}
