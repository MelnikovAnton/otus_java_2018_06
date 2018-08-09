package ru.otus.gc.classLoaderLeak;

public interface ClassLoaderLeakerMBean {

    void setStop(boolean b);
    void cleanMemory();
    void setStopLeak(boolean b);
    void setSize(int i);
    int getSize();
    void setSleepTime(int i);

}
