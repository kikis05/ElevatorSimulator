import java.util.*;

public class ElevatorQueue {
    private boolean up;
    private NavigableSet<Integer> pending;
    private NavigableSet<Integer> current;

    public ElevatorQueue(String direction_) {
        if (direction_.compareTo("up") != 0 && direction_.compareTo("down") != 0) {
            throw new IllegalArgumentException("Direction must be up or down.");
        }
        this.up = direction_.compareTo("up") == 0;
        this.pending = new TreeSet<>();
        this.current = new TreeSet<>();
    }

    public synchronized void addToPending(int add) {
        this.pending.add(add);
    }

    public synchronized void addToCurrent(int add) {
        this.current.add(add);
    }
    public synchronized void popFromCurrent() {
        if (this.up) {
            this.current.pollFirst();
        } else {      
            this.current.pollLast();
        }
        if (this.current.isEmpty()) {
            swapQueues();
        }
    }

    public synchronized int getHighestFloor() {
        return this.current.last();
    }
    public synchronized int getLowestFloor() {
        return this.current.first();
    }

    public synchronized boolean isEmpty() {
        return this.current.isEmpty() && this.pending.isEmpty();
    }

    private synchronized void swapQueues() {
        this.current.addAll(this.pending);
        this.pending.clear();
    }

}
