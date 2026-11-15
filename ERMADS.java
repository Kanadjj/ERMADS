import java.util.Scanner;
// Emergency Response Management and Dispatch System (ERMADS)
class Emergency {
    int id;
    String type;
    String location;
    int priority;
// Priority: 1 = Critical, 2 = High, 3 = Low
    Emergency(int id, String type, String location, int priority) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.priority = priority;
    }

    public String toString() {
        return "ID: " + id + " | Type: " + type + " | Location: "
            + location + " | Priority: " + priority;
    }
}
// Rescue team with unique ID and name.
class RescueTeam {
    int id;
    String name;

    RescueTeam(int id, String name) {
        this.id = id;
        this.name = name;
    }
// Display team ID and name.
    public String toString() {
        return "Team ID: " + id + " | Name: " + name;
    }
}
// Mission links an emergency with a rescue team.
class Mission {
    Emergency emergency;
    RescueTeam team;

    Mission(Emergency emergency, RescueTeam team) {
        this.emergency = emergency;
        this.team = team;
    }

    public String toString() {
        return "Emergency ID: " + emergency.id + " | " + emergency.type
            + " at " + emergency.location + " | Team: " + team.name;
    }
}
// Sorted linked list: priority 1 is first; equal priorities keep arrival order.
class EmergencyPriorityQueue {
    private static class Node {
        Emergency emergency;
        Node next;
// Node constructor initializes the emergency and sets next to null.
        Node(Emergency emergency) {
            this.emergency = emergency;
        }
    }
// The front of the queue points to the highest priority emergency.
    private Node front;

    boolean isEmpty() {
        return front == null;
    }
// Add an emergency to the queue in sorted order based on priority.
    void add(Emergency emergency) {
        Node node = new Node(emergency);
        if (front == null || emergency.priority < front.emergency.priority) {
            node.next = front;
            front = node;
            return;
        }
// Traverse the list to find the correct position for the new emergency.
        Node current = front;
        while (current.next != null
                && current.next.emergency.priority <= emergency.priority) {
            current = current.next;
        }
        node.next = current.next;
        current.next = node;
    }
// Remove and return the highest priority emergency from the queue.
    Emergency removeHighest() {
        if (front == null) {
            return null;
        }
        Emergency emergency = front.emergency;
        front = front.next;
        return emergency;
    }
// Display all pending emergencies in the queue.
    void display() {
        if (isEmpty()) {
            System.out.println("No pending emergencies.");
            return;
        }
        System.out.println("\n--- Pending Emergencies ---");
        Node current = front;
        while (current != null) {
            System.out.println(current.emergency);
            current = current.next;
        }
    }
}
// Linked-node FIFO queue for available rescue teams.
class TeamQueue {
    private static class Node {
        RescueTeam team;
        Node next;

        Node(RescueTeam team) {
            this.team = team;
        }
    }
// The front points to the first team in the queue, and rear points to the last team.
    private Node front;
    private Node rear;

    boolean isEmpty() {
        return front == null;
    }

    void add(RescueTeam team) {
        Node node = new Node(team);
        if (rear == null) {
            front = node;
        } else {
            rear.next = node;
        }
        rear = node;
    }
// Remove and return the first team in the queue (FIFO order).
    RescueTeam remove() {
        if (front == null) {
            return null;
        }
        RescueTeam team = front.team;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        return team;
    }
// Display all available rescue teams in the queue.
    void display() {
        if (isEmpty()) {
            System.out.println("No available rescue teams.");
            return;
        }
        System.out.println("\n--- Available Rescue Teams (FIFO) ---");
        Node current = front;
        while (current != null) {
            System.out.println(current.team);
            current = current.next;
        }
    }
}
// Singly linked list used for both active missions and completed history.
class MissionList {
    private static class Node {
        Mission mission;
        Node next;

        Node(Mission mission) {
            this.mission = mission;
        }
    }
// The head points to the first mission in the list, and tail points to the last mission.
    private Node head;
    private Node tail;

    boolean isEmpty() {
        return head == null;
    }
// Add a mission to the end of the list.
    void add(Mission mission) {
        Node node = new Node(mission);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }
// Remove a mission by its emergency ID and return it; return null if not found.
    Mission removeByEmergencyId(int id) {
        Node previous = null;
        Node current = head;
        while (current != null) {
            if (current.mission.emergency.id == id) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;
                }
                return current.mission;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }
// Display all missions in the list with a given title.
    void display(String title) {
        if (isEmpty()) {
            System.out.println("No " + title.toLowerCase() + ".");
            return;
        }
        System.out.println("\n--- " + title + " ---");
        Node current = head;
        while (current != null) {
            System.out.println(current.mission);
            current = current.next;
        }
    }
}
// Adjacency-list graph: each location links to its road nodes.
class Graph {
    private static class Road {
        Location destination;
        int distance;
        Road next;

        Road(Location destination, int distance, Road next) {
            this.destination = destination;
            this.distance = distance;
            this.next = next;
        }
    }
// Each location has a name and a linked list of roads to other locations.
    private static class Location {
        String name;
        Road roads;
        Location next;

        Location(String name) {
            this.name = name;
        }
    }
// The graph maintains a linked list of all locations and a count of them.
    private Location locations;
    private int count;
// Find a location by name; return null if not found.
    private Location findLocation(String name) {
        Location current = locations;
        while (current != null) {
            if (current.name.equals(name)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }
// Get an existing location or add a new one if it doesn't exist.
    private Location getOrAddLocation(String name) {
        Location location = findLocation(name);
        if (location == null) {
            location = new Location(name);
            location.next = locations;
            locations = location;
            count++;
        }
        return location;
    }
// Set the distance of a road from one location to another; add the road if it doesn't exist.
    private void setRoad(Location from, Location to, int distance) {
        Road road = from.roads;
        while (road != null) {
            if (road.destination == to) {
                road.distance = distance;
                return;
            }
            road = road.next;
        }
        from.roads = new Road(to, distance, from.roads);
    }
// Add a bidirectional road between two locations with a specified distance.
    void addRoad(String first, String second, int distance) {
        Location a = getOrAddLocation(first);
        Location b = getOrAddLocation(second);
        setRoad(a, b, distance);
        setRoad(b, a, distance);
    }
// Check if a road exists from one location to another.
    private boolean hasRoad(Location from, Location to) {
        Road road = from.roads;
        while (road != null) {
            if (road.destination == to) {
                return true;
            }
            road = road.next;
        }
        return false;
    }
// Remove a road from one location to another; used for bidirectional removal.
    private void removeRoadFrom(Location from, Location to) {
        Road previous = null;
        Road current = from.roads;
        while (current != null) {
            if (current.destination == to) {
                if (previous == null) {
                    from.roads = current.next;
                } else {
                    previous.next = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
    }
// Remove a bidirectional road between two locations; return true if successful.
    boolean removeRoad(String first, String second) {
        Location a = findLocation(first);
        Location b = findLocation(second);
        if (a == null || b == null || !hasRoad(a, b)) {
            return false;
        }
        removeRoadFrom(a, b);
        removeRoadFrom(b, a);
        return true;
    }
// Display all roads in the graph, ensuring each road is printed only once.
    void displayRoads() {
        boolean found = false;
        System.out.println("\n--- Roads ---");
        Location location = locations;
        while (location != null) {
            Road road = location.roads;
            while (road != null) {
                if (location.name.compareTo(road.destination.name) < 0) {
                    System.out.println(location.name + " <-> "
                        + road.destination.name + " (" + road.distance + " km)");
                    found = true;
                }
                road = road.next;
            }
            location = location.next;
        }
        if (!found) {
            System.out.println("No roads have been added.");
        }
    }
// Find the index of a location in the array of nodes; return -1 if not found.
    private int indexOf(Location[] nodes, Location location) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == location) {
                return i;
            }
        }
        return -1;
    }
    // Dijkstra's algorithm: repeatedly select the nearest unvisited location.
    void shortestRoute(String start, String end) {
        Location source = findLocation(start);
        Location destination = findLocation(end);
        if (source == null || destination == null) {
            System.out.println("Location not found.");
            return;
        }
// Create an array of all locations for easier indexing.
        Location[] nodes = new Location[count];
        Location location = locations;
        for (int i = 0; i < count; i++) {
            nodes[i] = location;
            location = location.next;
        }
// Initialize distance, visited, and previous arrays for Dijkstra's algorithm.
        long[] distance = new long[count];
        boolean[] visited = new boolean[count];
        int[] previous = new int[count];
        for (int i = 0; i < count; i++) {
            distance[i] = Long.MAX_VALUE;
            previous[i] = -1;
        }
// Set the distance of the source location to 0 and find its index.
        int sourceIndex = indexOf(nodes, source);
        int destinationIndex = indexOf(nodes, destination);
        distance[sourceIndex] = 0;

        for (int step = 0; step < count; step++) {
            int nearest = -1;
            for (int i = 0; i < count; i++) {
                if (!visited[i] && (nearest == -1 || distance[i] < distance[nearest])) {
                    nearest = i;
                }
            }
            if (nearest == -1 || distance[nearest] == Long.MAX_VALUE) {
                break;
            }
            visited[nearest] = true;

            Road road = nodes[nearest].roads;
            while (road != null) {
                int next = indexOf(nodes, road.destination);
                long newDistance = distance[nearest] + road.distance;
                if (!visited[next] && newDistance < distance[next]) {
                    distance[next] = newDistance;
                    previous[next] = nearest;
                }
                road = road.next;
            }
        }
// If the destination is unreachable, inform the user; otherwise, reconstruct and display the shortest path.
        if (distance[destinationIndex] == Long.MAX_VALUE) {
            System.out.println("No route available.");
            return;
        }
// Reconstruct the shortest path from destination to source using the previous array.
        int[] path = new int[count];
        int length = 0;
        for (int at = destinationIndex; at != -1; at = previous[at]) {
            path[length++] = at;
        }
        System.out.print("Shortest Route: ");
        for (int i = length - 1; i >= 0; i--) {
            System.out.print(nodes[path[i]].name);
            if (i > 0) {
                System.out.print(" -> ");
            }
        }
        System.out.println("\nTotal Distance: " + distance[destinationIndex] + " km");
    }
}
// Main class for the Emergency Response Management and Dispatch System (ERMADS).
public class ERMADS {
    private static final Scanner input = new Scanner(System.in);
    private static final EmergencyPriorityQueue emergencies =
        new EmergencyPriorityQueue();
    private static final TeamQueue teams = new TeamQueue();
    private static final MissionList activeMissions = new MissionList();
    private static final MissionList history = new MissionList();
    private static final Graph graph = new Graph();
    private static int nextEmergencyId = 101;
    private static int nextTeamId = 1;
// Read an integer from the user with a prompt; return -1 if input is invalid or EOF is reached.
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) {
                return -1;
            }
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number.");
            }
        }
    }
// Read a line of text from the user with a prompt; return an empty string if EOF is reached.
    private static String readText(String prompt) {
        System.out.print(prompt);
        return input.hasNextLine() ? input.nextLine().trim() : "";
    }
// Register a new emergency by prompting the user for type, location, and priority; add it to the emergency queue.
    private static void registerEmergency() {
        System.out.println("\nEmergency Type: 1.Fire  2.Medical  3.Road Accident");
        System.out.println("4.Flood  5.Earthquake  6.Building Collapse  7.Missing Person");
        int typeChoice = readInt("Choose type: ");
        String type;
        switch (typeChoice) {
            case 1:
                type = "Fire";
                break;
            case 2:
                type = "Medical Emergency";
                break;
            case 3:
                type = "Road Accident";
                break;
            case 4:
                type = "Flood";
                break;
            case 5:
                type = "Earthquake";
                break;
            case 6:
                type = "Building Collapse";
                break;
            case 7:
                type = "Missing Person";
                break;
            default:
                System.out.println("Invalid emergency type.");
                return;
        }
// Prompt the user for the location of the emergency; ensure it is not empty.
        String location = readText("Enter location: ");
        if (location.isEmpty()) {
            System.out.println("Location cannot be empty.");
            return;
        }
        System.out.println("Priority: 1.Critical  2.High  3.Low");
        int priority = readInt("Choose priority: ");
        if (priority < 1 || priority > 3) {
            System.out.println("Invalid priority.");
            return;
        }

        Emergency emergency = new Emergency(nextEmergencyId++, type,
            location, priority);
        emergencies.add(emergency);
        System.out.println("Emergency registered. ID: " + emergency.id);
    }
// Prompt the user for a rescue team name; ensure it is not empty; add the team to the queue.
    private static void addTeam() {
        String name = readText("Enter rescue team name: ");
        if (name.isEmpty()) {
            System.out.println("Team name cannot be empty.");
            return;
        }
        teams.add(new RescueTeam(nextTeamId++, name));
        System.out.println("Rescue team added.");
    }
// Dispatch a mission by pairing the highest priority emergency with the first available rescue team; add it to active missions.
    private static void dispatchMission() {
        if (emergencies.isEmpty()) {
            System.out.println("No pending emergencies.");
            return;
        }
        if (teams.isEmpty()) {
            System.out.println("No available rescue teams.");
            return;
        }
        Mission mission = new Mission(emergencies.removeHighest(), teams.remove());
        activeMissions.add(mission);
        System.out.println("Mission dispatched: " + mission);
    }
// Complete a mission by prompting the user for an emergency ID; remove it from active missions, add it to history, and return the team to the queue.
    private static void completeMission() {
        if (activeMissions.isEmpty()) {
            System.out.println("No active missions.");
            return;
        }
        activeMissions.display("Active Missions");
        int id = readInt("Enter emergency ID to complete: ");
        Mission mission = activeMissions.removeByEmergencyId(id);
        if (mission == null) {
            System.out.println("Active mission not found.");
            return;
        }
        history.add(mission);
        teams.add(mission.team);
        System.out.println("Mission completed. Team returned to the queue.");
    }
// Manage roads by allowing the user to add, view, or remove roads between locations.
    private static void addRoad() {
        String first = readText("Enter first location: ");
        String second = readText("Enter second location: ");
        int distance = readInt("Enter distance in km: ");
        if (first.isEmpty() || second.isEmpty()
                || first.equalsIgnoreCase(second) || distance <= 0) {
            System.out.println("Enter two different locations and a positive distance.");
            return;
        }
        graph.addRoad(first, second, distance);
        System.out.println("Road added.");
    }
// Remove a road between two locations by prompting the user for the locations; inform the user if the road was removed or not found.
    private static void removeRoad() {
        String first = readText("Enter first location: ");
        String second = readText("Enter second location: ");
        if (graph.removeRoad(first, second)) {
            System.out.println("Road removed.");
        } else {
            System.out.println("Road not found.");
        }
    }
// Manage roads by providing a menu for adding, viewing, or removing roads; loop until the user chooses to return to the main menu.
    private static void manageRoads() {
        while (true) {
            System.out.println("\n--- Manage Roads ---");
            System.out.println("1. Add Road");
            System.out.println("2. View Roads");
            System.out.println("3. Remove Road");
            System.out.println("4. Back to Main Menu");
            int choice = readInt("Enter choice: ");
            if (choice == -1 || choice == 4) {
                return;
            }
            switch (choice) {
                case 1:
                    addRoad();
                    break;
                case 2:
                    graph.displayRoads();
                    break;
                case 3:
                    removeRoad();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
// Find the shortest route between two locations by prompting the user for the starting location and destination; display the route and total distance.
    private static void findRoute() {
        String start = readText("Enter starting location: ");
        String end = readText("Enter destination: ");
        graph.shortestRoute(start, end);
    }
// Main method: display the main menu and handle user choices in a loop until the user chooses to exit.
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== ERMADS ===");
            System.out.println("1. Register Emergency");
            System.out.println("2. View Pending Emergencies");
            System.out.println("3. Add Rescue Team");
            System.out.println("4. View Available Teams");
            System.out.println("5. Dispatch Rescue Mission");
            System.out.println("6. Manage Roads");
            System.out.println("7. Find Shortest Route");
            System.out.println("8. Complete Mission");
            System.out.println("9. View Mission History");
            System.out.println("10. View Active Missions");
            System.out.println("0. Exit");
            int choice = readInt("Enter choice: ");
            if (choice == -1 || choice == 0) {
                System.out.println("Exiting ERMADS.");
                break;
            }
            switch (choice) {
                case 1:
                    registerEmergency();
                    break;
                case 2:
                    emergencies.display();
                    break;
                case 3:
                    addTeam();
                    break;
                case 4:
                    teams.display();
                    break;
                case 5:
                    dispatchMission();
                    break;
                case 6:
                    manageRoads();
                    break;
                case 7:
                    findRoute();
                    break;
                case 8:
                    completeMission();
                    break;
                case 9:
                    history.display("Mission History");
                    break;
                case 10:
                    activeMissions.display("Active Missions");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        input.close();
    }
}