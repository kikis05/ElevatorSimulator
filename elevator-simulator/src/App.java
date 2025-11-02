import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Elevator elevator = new Elevator(10, 3, 3);
        ElevatorRequest r = new ElevatorRequest(1, 5, elevator.getNFloors());

        new Thread(() -> elevator.start(2)).start();
        new Thread(() -> listenForRequests(elevator)).start();

        // ScheduledExecutorService requestTimer = Executors.newScheduledThreadPool(1);
        // requestTimer.schedule(() -> {
        //     ElevatorRequest r2 = new ElevatorRequest(2,5, elevator.getNFloors());
        //     elevator.addRequest(r2);
        //     requestTimer.shutdown();
        // }, 2, TimeUnit.SECONDS);

    }

    private static void listenForRequests(Elevator elevator) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a floor start (-1 to stop): ");
            int startFloor = scanner.nextInt();
            if (startFloor == -1) {
                elevator.stop();
                break;
            };
            // System.out.print("Enter a floor end (-1 to stop): ");
            // int endFloor = scanner.nextInt();
            // if (endFloor == -1) {
            //     elevator.stop();
            //     break;
            // };
            // ElevatorRequest r2 = new ElevatorRequest(startFloor,endFloor, elevator.getNFloors());
            elevator.addFloorRequest(startFloor);
        }
        scanner.close();
    }
}
