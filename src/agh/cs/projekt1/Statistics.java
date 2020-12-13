package agh.cs.projekt1;

public class Statistics {
    private int i = 0;

    public synchronized void setI(int i) {
        this.i = i;
    }

    public synchronized int getI() {
        return i;
    }
}
