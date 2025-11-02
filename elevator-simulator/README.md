Code Written by Kirthi Shankar to simulate an elevator.

Github Repository: https://github.com/kikis05/ElevatorSimulator

When the app is run from the main function in App.java, a GUI will pop up. Select floors on the GUI to simulate floor requests from people waiting both outside the elevator and inside the elevator (this will handle both types of these requests).

## Elevator Behavior Assumptions

1. The elevator starts on the lowest floor, which is 1, so the start direction is up.
2. If the elevator is moving upward, and it receives a request for a lower floor, it will continue moving upward instead of switching directions. Once it has completed all the higher floors, including those that were requested on the way up, it will switch directions.
3. If the elevator is moving downward, and it receives a request for a higher floor, it will continue moving downward instead of switching directions. Once it has completed all the lower floors, including those that were requested on the way down, it will switch directions.
4. If the elevator arrives at a floor with no pending floor requests(the elevator can either move up or down), and multiple people enter, some who ask for higher floors and some who ask for lower floors, the elevator will move in the direction of the first floor that was requested.
5. If the elevator does not receive a request or does not have any pending requests after someone has called it to a floor, it closes instead of waiting to close until it gets a request. (Luckily, if someone is inside the elevator when it closes, they can click on the button for the same floor and it will open again.)

## Features that Weren't Implemented

- Sometimes, there will be two different elevators used in a building, side by side. One elevator will decide to take a floor request based on the location and direction of the other elevator. This code simulates the behavior of an elevator when it does not have a companion, but could be expanded to make decisions based on a companion elevator's data.
- I made assumption 5: if the elevator does not receive a request and does not have any pending floors to go to, it will close instead of remaining open. There's also an option to keep the elevator open until it receives a request.
- Elevators will not close if something is obstructing their doors. So, there could be a way to 1) scan for blockage, 2) wait, and 3) repeat 1 and 2 until there is no blockage left, and the elevator door closes.
- It might be more intuitive for users of the application to have two different ways to request a floor, one for people outside the elevator, and one for people inside the elevator, even if these follow the same logic on the backend side.
