import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class App {

    // gui for the elevator
    public static class ElevatorSimulator {
        private JLabel status;
        private Elevator elevator;

        public ElevatorSimulator() {
            JFrame frame = new JFrame("Elevator");
            frame.setSize(300, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(0, 1));

            // displaying the current state of the elevator
            JLabel statusLabel = new JLabel("Current Elevator Status:");
            statusLabel.setHorizontalAlignment(JLabel.CENTER);
            statusLabel.setVerticalAlignment(JLabel.CENTER);
            frame.add(statusLabel);

            this.status = new JLabel("");
            this.status.setHorizontalAlignment(JLabel.CENTER);
            this.status.setVerticalAlignment(JLabel.CENTER);
            this.status.setForeground(Color.BLUE);
            frame.add(status);

            // create a new elevator object
            this.elevator = new Elevator(10, this.status);

            // buttons for requesting floors
            for (int i = elevator.getNFloors(); i >= 1; i--) {
                int floor = i;
                JButton button = new JButton("Floor " + floor);
                button.addActionListener(e -> elevator.addFloorRequest(floor));
                frame.add(button);
            }

            // instructions
            JLabel instructions = new JLabel("Press buttons to request floors.");
            instructions.setHorizontalAlignment(JLabel.CENTER);
            instructions.setVerticalAlignment(JLabel.CENTER);
            frame.add(instructions);

            frame.pack();
            frame.setVisible(true);
            elevator.start();
        }
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(ElevatorSimulator::new);
    }
}
