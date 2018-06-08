# traffic-drones
Traffic Drones ‐ Java Test

Scenario:
There are two automatic drones that fly around London and report on traffic conditions. When a drone flies over a tube station, it assesses what the traffic condition is like in the area, and reports on it.

Task:
Write a simulation that has one dispatcher and two drones. Each drone should "move" independently on different threads.
The dispatcher should send the coordinates to each drone detailing where the drone's next position should be.The dispatcher
should also be responsible for terminating the program. When the drone receives a new coordinate, it moves, checks if there
is a tube station in the area, and if so, reports on the traffic conditions there.The data should be accessible and reusable
to other components.
