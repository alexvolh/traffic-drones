import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Drone {
    private String id;
    private double speed;
    private Map<String, Coordinate> tubeStations;
    private List<Coordinate> tubeCoordinates;

    enum trafficCondition {
        HEAVY, LIGHT, MODERATE
    }

    public Drone(String id, Map<String, Coordinate> tubeStations) {
        this.id = id;
        this.tubeStations = tubeStations;
        this.tubeCoordinates = new ArrayList<>(tubeStations.values());
        this.speed = 60.5;
    }

    /* move drone by the coordinates */
    public void move(List<Coordinate> droneCoordinates) {
        for (Coordinate droneCoordinate : droneCoordinates) {
            System.out.printf("id: %s \ttime: %s \tspeed : %s \tlat: %s \tlon: %s \ttube station: %s \tcontition: %-4s\n",
                    id, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), speed,
                    droneCoordinate.getLatitude(), droneCoordinate.getLongitude(), isTubeStation(droneCoordinate), getTrafficCondition());
  /*          System.out.println("id: " + id + " time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " speed: " + speed
                                + " location now: " + "lat: " + droneCoordinate.getLatitude() + " lon: " + droneCoordinate.getLongitude() + " tube station: "
                                + isTubeStation(droneCoordinate) + " condition: " + getTrafficCondition());*/
        }
    }

    /* Check if drone in nearby station area */
    private String isTubeStation(Coordinate droneCoordinates) {
        String result = "none";
        int dist;
        int nearbyLocation = 350;

        for (Coordinate tubeCoordinate : tubeCoordinates) {
            dist = (int) calcDistance(droneCoordinates.getLatitude(), droneCoordinates.getLongitude(), tubeCoordinate.getLatitude(), tubeCoordinate.getLongitude());

            if (dist < nearbyLocation) {
                /* find the name of tube station */
                result = tubeStations.entrySet()
                        .stream()
                        .filter(tube -> tube.getValue().getLatitude() == tubeCoordinate.getLatitude() && tube.getValue().getLongitude() == tubeCoordinate.getLongitude())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.joining());
            }
        }

        return result;
    }

    /*Calculation of distance*/
    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(lat1 * Math.PI /180.0) * Math.sin(lat2 * Math.PI /180.0)
                      + Math.cos(lat1 * Math.PI /180.0) * Math.cos(lat2 * Math.PI /180.0) * Math.cos(theta * Math.PI /180.0);
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist / 0.00062137; // convert miles to meters

        return dist;
    }


    public String getId() {
        return id;
    }

    private trafficCondition getTrafficCondition() {
        return trafficCondition.values()[new Random().nextInt(trafficCondition.values().length)];
    }
}
