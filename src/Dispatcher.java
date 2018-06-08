import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Dispatcher {
    private Drone firstDrone;
    private Drone secondDrone;

    private String firstDroneId;
    private String secondDroneId;

    private Queue<Coordinate> firstDroneCoordinates;
    private Queue<Coordinate> secondDroneCoordinates;
    private Map<String, Coordinate> tubeStations;

    private ThreadPoolExecutor executorService;

    Dispatcher(String firstDroneId, String secondDroneId) {
        this.firstDroneId = firstDroneId;
        this.secondDroneId = secondDroneId;

        firstDroneCoordinates = parseDroneCoordinates(firstDroneId);
        secondDroneCoordinates = parseDroneCoordinates(secondDroneId);
        tubeStations = parseTubeStations();

        firstDrone = new Drone(firstDroneId, tubeStations);
        secondDrone = new Drone(secondDroneId, tubeStations);

        executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    public void start() {
        if (firstDroneCoordinates.size() != 0) {
            executorService.execute(() -> firstDrone.move(getDroneCoordinates(firstDrone.getId())));
        }

        if (secondDroneCoordinates.size() != 0) {
            executorService.execute(() -> secondDrone.move(getDroneCoordinates(secondDrone.getId())));
        }

        if (firstDroneCoordinates.size() == 0 & secondDroneCoordinates.size() == 0) {
            executorService.shutdown();
        }
    }
    /* load drone coordinates from files */
    private Queue<Coordinate> parseDroneCoordinates(String droneId) {
        Queue<Coordinate> coordinates = new ArrayDeque<>();
        String pathToCSV = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + droneId + ".csv";
        String csvSplitBy = ",";
        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToCSV))) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\"", "");
                String [] data = line.split(csvSplitBy);

                coordinates.add(new Coordinate(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinates;
    }
    /* load tube stations coordinates */
    private Map<String, Coordinate> parseTubeStations() {
        String pathToCSV = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "tube.csv";
        Map<String, Coordinate> tubeStations = new HashMap<>();
        String csvSplitBy = ",";
        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToCSV))) {
            while ((line = bufferedReader.readLine()) != null) {
                String [] data = line.split(csvSplitBy);
                tubeStations.put(data[0], new Coordinate(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tubeStations;
    }

    public List<Coordinate> getDroneCoordinates(String droneId) {
        List<Coordinate> droneCoordinates = new ArrayList<>();

        if (droneId.equals(firstDroneId)) {
            droneCoordinates = pollDroneCoordinates(firstDroneCoordinates);
        } else if (droneId.equals(secondDroneId)) {
            droneCoordinates = pollDroneCoordinates(secondDroneCoordinates);
        }

        return droneCoordinates;
    }
    /* pumping the data with drone coordinates */
    private List<Coordinate> pollDroneCoordinates(Queue<Coordinate> droneCoordinates) {
        List<Coordinate> coordinates = new ArrayList<>();
        Coordinate coordinate;
        int maxPoll = 10;

        if (!droneCoordinates.isEmpty()) {
            if (droneCoordinates.size() < maxPoll) {
                for (int i = 0; i < droneCoordinates.size(); i++) {
                    coordinate = droneCoordinates.poll();
                    coordinates.add(coordinate);
                }
            } else {
                for (int i = 0; i < maxPoll; i++) {
                    coordinate = droneCoordinates.poll();
                    coordinates.add(coordinate);
                }
            }
        }

        return coordinates;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public String getFirstDroneId() {
        return firstDroneId;
    }

    public String getSecondDroneId() {
        return secondDroneId;
    }

    public Map<String, Coordinate> getTubeStations() {
        return tubeStations;
    }
}
