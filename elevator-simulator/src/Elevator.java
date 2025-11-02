import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Elevator {
    // number of floors the elevator can travel to
    private int nFloors;
    // seconds it takes to travel from one floor to an adjacent floor
    private int secondsToTravelToFloor;

    private int secondsPerStop;

    // variables for maintaining the current state of the elevator
    // timer for determining the current floor.
    private ScheduledExecutorService timer;
    // queue of requests
    private NavigableSet<Integer> upQueue;
    private NavigableSet<Integer> downQueue;
    // currentFloor
    private int currentFloor;
    private boolean up;
    private boolean isRunning;

    public Elevator(int nFloors_, int secondsToTravelToFloor_, int secondsPerStop_) {
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
            throw new IllegalArgumentException("Seconds to at each floor must be positive.");
        } else {
            this.secondsPerStop = secondsPerStop_;
        }
        
        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.currentFloor = 1;
        this.up = true;
        this.timer = Executors.newScheduledThreadPool(2);
        this.isRunning = false;
    }
    
    public int getNFloors() {
        return this.nFloors;
    }

    public void reset() {
        this.currentFloor = 1;
        this.up = true;
        this.isRunning = false;
    }

    public void start(int floor) {
        System.out.println("At floor: " + this.currentFloor);
        addFloorRequest(floor);
    }
    public synchronized void stop() {
        System.out.println("Stopping simulation: " + this.currentFloor);
        this.timer.shutdown();
    }

    private void moveElevator() {
        // if no requests are left, stop the elevator
        if (this.upQueue.isEmpty() && this.downQueue.isEmpty()) {
            this.isRunning = false;
            System.out.println("Elevator has stopped at floor: " + this.currentFloor);
            // this.timer.shutdown();
            return;
        }
        if (this.up && this.upQueue.isEmpty()) {
            this.up = false;
            System.out.println("going down!");
        }
        if (!this.up && this.downQueue.isEmpty()) {
            this.up = true;
            System.out.println("going up!");
        }
        if (this.up) {
            if (this.currentFloor == this.upQueue.first()) {
                this.upQueue.pollFirst();
                waitAtFloor();
                return;
            }
            // move the elevator up
            this.timer.schedule(() -> {
                this.currentFloor++;
                System.out.println("At floor: " + this.currentFloor);
                moveElevator();
            }, this.secondsToTravelToFloor, TimeUnit.SECONDS);
        } else {
            if (currentFloor == this.downQueue.last()) {
                // update the upQueue by removing this floor
                this.downQueue.pollLast();
                waitAtFloor();
                return;
            }
            // move the elevator down
            this.timer.schedule(() -> {
                this.currentFloor--;
                System.out.println("At floor: " + this.currentFloor);
                moveElevator();
            }, this.secondsToTravelToFloor, TimeUnit.SECONDS);
            }
       return;
    }

    private void waitAtFloor() {
        System.out.println("Opened door at floor: " + this.currentFloor);
        this.timer.schedule(() -> {
            System.out.println("Closed door at floor: " + this.currentFloor);
            moveElevator();
        }, this.secondsPerStop, TimeUnit.SECONDS);
    }

    // handles requests for single floors
    public synchronized void addFloorRequest(int floor) {
        if (floor < 1 || floor > this.nFloors) {
            throw new IllegalArgumentException("Entered floor " + floor + " does not exist.");
        }
        // add to queue
        queueFloorRequest(floor);
        // move elevator
        if (!this.isRunning) {
            this.isRunning = true;
            moveElevator();
        }
    }

    public synchronized void queueFloorRequest(int floor) {
            if (floor < this.currentFloor) {
                this.downQueue.add(floor);
            } else if (floor > this.currentFloor) {
                this.upQueue.add(floor);
            } else {
                if (this.isRunning) {
                    if (this.up) {
                        this.downQueue.add(floor);
                    } else {
                        this.upQueue.add(floor);
                    } 
                }  else {
                    if (this.up) {
                        this.upQueue.add(floor);
                    } else {
                        this.downQueue.add(floor);
                    } 
                }
            }
    }


    // public synchronized void addStartEndRequest(ElevatorRequest request) {
    //     // validate
    //     int requestStart = request.getStartFloor();
    //     int requestEnd = request.getEndFloor();
    //     if (requestStart == requestEnd) {
    //         return;
    //     }
    //     // add to queue
    //     queueStartEndRequest(request);
    //     // move elevator
    //     if (!this.isRunning) {
    //         this.isRunning = true;
    //         moveElevator();
    //     }
    // }

    // public synchronized void queueStartEndRequest(ElevatorRequest request) {
    //     int requestStart = request.getStartFloor();
    //     int requestEnd = request.getEndFloor();
         
    //     // logic for deciding what order of floors the elevator will stop at based on the request
    //     if (this.up) {
    //         if (requestStart < this.currentFloor) {
    //             //if end >, then add start to downqueue, end to pending upqueue.
    //             if (requestEnd > requestStart ) {
    //                 this.downQueue.addToCurrent(requestStart);
    //                 this.upQueue.addToPending(requestEnd);
    //             } else {
    //                 this.downQueue.addToCurrent(requestStart);
    //                 this.downQueue.addToCurrent(requestEnd);
    //             }
    //         } else {
    //             this.upQueue.addToCurrent(requestStart);
    //             if (requestEnd < this.currentFloor || requestEnd < requestStart) {
    //                 this.downQueue.addToCurrent(requestEnd);
    //             } else {
    //                 this.upQueue.addToCurrent(requestEnd);
    //             }
    //         }
    //     } else {
    //         if (requestStart > this.currentFloor) {
    //             //if end <, then add both to the start of the downqueue, but skip them.
    //             if (requestEnd < requestStart ) {
    //                 this.upQueue.addToCurrent(requestStart);
    //                 this.downQueue.addToPending(requestEnd);
    //             } else {
    //                 this.upQueue.addToCurrent(requestStart);
    //                 this.upQueue.addToCurrent(requestEnd);
    //             }
    //         } else {
    //             this.downQueue.addToCurrent(requestStart);
    //         }
    //         if (requestEnd > this.currentFloor || requestEnd > requestStart) {
    //             this.upQueue.addToCurrent(requestEnd);
    //         } else {
    //             this.downQueue.addToCurrent(requestEnd);
    //         }
    //     }
    //     return;
    // }
}
