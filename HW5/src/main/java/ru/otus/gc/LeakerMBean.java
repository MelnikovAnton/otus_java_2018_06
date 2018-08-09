package ru.otus.gc;

public interface LeakerMBean {
    void clean();
    boolean getLoop();
    void setLoop(boolean loop);
    int getSleep();
    void setSeep(int sleep);
    int getSize();
    void setSize(int size);

}
