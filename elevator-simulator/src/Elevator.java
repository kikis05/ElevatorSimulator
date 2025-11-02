import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Elevator {

    // variables representing features of the elevator

    // number of floors the elevator can travel to
    private int nFloors;
    // seconds it takes to travel from one floor to an adjacent floor
    private int secondsToTravelToFloor;
    // seconds the elevator waits at a requested floor   
    private int secondsPerStop;

    // variables for maintaining the current state of the elevator

    // queues for the requests
    private NavigableSet<Integer> upQueue;
    private NavigableSet<Integer> downQueue;

    // timer for determining the current floor.
    private ScheduledExecutorService timer;
    // current floor
    private int currentFloor;
    // current direction
    private boolean up;
    // whether the elevator is running or not
    private boolean isRunning;

    //variable for updating the GUI
    private JLabel status;

    // constructors
    public Elevator(int nFloors_, int secondsToTravelToFloor_, int secondsPerStop_, JLabel status_) {
        // input validation (no negative numbers, more than one floor)
        if (nFloors_ <= 1) {
            throw new IllegalArgumentException("Number of floors must be greater than 1.");
        } else {
            this.nFloors = nFloors_;
        }
        if (secondsToTravelToFloor_ <= 0) {
            throw new IllegalArgumentException("Seconds to travel from one floor to an adjacent floor must be positive.");
        } else {
            this.secondsToTravelToFloor = secondsToTravelToFloor_;
        }
        if (secondsPerStop_ <= 0) {
            throw new IllegalArgumentException("Seconds at each floor must be positive.");
        } else {
            this.secondsPerStop = secondsPerStop_;
        }

        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.currentFloor = 1;
        this.up = true;
        this.timer = Executors.newScheduledThreadPool(1);
        this.isRunning = false;

        this.status = status_;
    }

    public Elevator(int nFloors_, JLabel status_) {

        if (nFloors_ <= 1) {
            throw new IllegalArgumentException("Number of floors must be greater than 1.");
        } else {
            this.nFloors = nFloors_;
        }

        this.secondsToTravelToFloor = 1;
        this.secondsPerStop = 3;

        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.currentFloor = 1;
        this.up = true;
        this.timer = Executors.newScheduledThreadPool(1);
        this.isRunning = false;

        this.status = status_;
    }

    public Elevator(JLabel status_) {

        this.nFloors = 10;
        this.secondsToTravelToFloor = 1;
        this.secondsPerStop = 3;

        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.currentFloor = 1;
        this.up = true;
        this.timer = Executors.newScheduledThreadPool(1);
        this.isRunning = false;

        this.status = status_;
    }

    // update the status label for the GUI
    private void updateStatus(String update) {
        SwingUtilities.invokeLater(() -> this.status.setText(update));
        System.out.println(update);
    }

    // get function for number of floors in the elevator
    public int getNFloors() {
        return this.nFloors;
    }

    // start the simulation
    public void start() {
        updateStatus("At floor: " + this.currentFloor);
    }

    // move the elevator based on the items in upQueue and downQueue
    private void moveElevator() {
        // if no requests are left, stop the elevator
        if (this.upQueue.isEmpty() && this.downQueue.isEmpty()) {
            this.isRunning = false;
            return;
        }
        // switch directions if the current queue (up or down) is empty
        if (this.up && this.upQueue.isEmpty()) {
            this.up = false;
        }
        if (!this.up && this.downQueue.isEmpty()) {
            this.up = true;
        }
        // handle upward movement
        if (this.up) {
            // stop if the current floor is on the queue
            if (this.currentFloor == this.upQueue.first()) {
                // update the upQueue by removing this floor
                this.upQueue.pollFirst();
                waitAtFloor();
                return;
            }
            //move upward
            this.timer.schedule(() -> {
                this.currentFloor++;
                updateStatus("At floor: " + this.currentFloor);
                moveElevator();
            }, this.secondsToTravelToFloor, TimeUnit.SECONDS);
        // handle downward movement
        } else {
            // stop if the current floor is on the queue
            if (currentFloor == this.downQueue.last()) {
                // update the downQueue by removing this floor
                this.downQueue.pollLast();
                waitAtFloor();
                return;
            }
            // move the elevator down
            this.timer.schedule(() -> {
                this.currentFloor--;
                updateStatus("At floor: " + this.currentFloor);
                moveElevator();
            }, this.secondsToTravelToFloor, TimeUnit.SECONDS);
            }
       return;
    }

    // wait at a floor that was requested for the duration specified by the user
    private void waitAtFloor() {
        updateStatus("At floor: " + this.currentFloor + ". Opened door at floor: " + this.currentFloor);
        this.timer.schedule(() -> {
            updateStatus("Closed door at floor: " + this.currentFloor);
            moveElevator();
        }, this.secondsPerStop, TimeUnit.SECONDS);
    }

    // adds floor request to the correct queue so that the elevator stops at the floor
    // note: there are two kinds of requests: requests from people waiting for the elevator,
    // and requests from people inside the elevator. Both follow the same logic, so they are
    // handled by this function.
    public synchronized void addFloorRequest(int floor) {
        if (floor < 1 || floor > this.nFloors) {
            throw new IllegalArgumentException("Entered floor " + floor + " does not exist.");
        }
        // add request to queue
        queueFloorRequest(floor);
        // move elevator
        if (!this.isRunning) {
            this.isRunning = true;
            moveElevator();
        }
    }

    public void queueFloorRequest(int floor) {
            if (floor < this.currentFloor) {
                this.downQueue.add(floor);
            } else if (floor > this.currentFloor) {
                this.upQueue.add(floor);
            } else {
                // if the elevator is in motion, the current floor has already been passed
                // we add this floor to the opposite queue so that it is stopped at on the way back.
                if (this.isRunning) {
                    if (this.up) {
                        this.downQueue.add(floor);
                    } else {
                        this.upQueue.add(floor);
                    } 
                // if the elevator is still, we are at the current floor
                // we add to the current queue so that the door opens
                }  else {
                    if (this.up) {
                        this.upQueue.add(floor);
                    } else {
                        this.downQueue.add(floor);
                    } 
                }
            }
    }
}
