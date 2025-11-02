public class ElevatorRequest {
    private int startFloor;
    private int endFloor;

    public int getStartFloor() {
        return this.startFloor;
    }
    public int getEndFloor() {
        return this.endFloor;
    }

    public ElevatorRequest(int startFloor_, int endFloor_, int maxFloors) {
        if (startFloor_ < 1 || startFloor_ > maxFloors) {
            throw new IllegalArgumentException("Start Floor " + startFloor_ + " is not between 1 and " + maxFloors);
        } else {
            startFloor = startFloor_;
        }
        if (endFloor_ < 1 || endFloor_ > maxFloors) {
            throw new IllegalArgumentException("End Floor " + endFloor_ + " is not between 1 and " + maxFloors);
        } else {
            endFloor = endFloor_;
        }
    }
}
