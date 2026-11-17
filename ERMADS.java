import java.util.Scanner;
// Emergency Response Management and Dispatch System (ERMADS)
class Emergency {
    int id, priority;
    String type, location;

    Emergency(int id, String type, String location, int priority) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.priority = priority;
    }
// Display emergency details
    void display() {
        System.out.println("ID: " + id + " | " + type + " | " + location +
                " | Priority: " + priority);
    }
}

// 1. PRIORITY QUEUE - Emergencies
class EmergencyPriorityQueue {
    class Node {
        Emergency data;
        Node next;
        Node(Emergency e) { data = e; } //// Refrence variable e
    }
// Head of the priority queue (highest priority emergency)
    Node head;
// Add a new emergency to the queue based on priority
    void add(Emergency e) {
        Node n = new Node(e);
        if (head == null || e.priority < head.data.priority) {
            n.next = head;
            head = n;
            return;
        }
        // Find the correct position to insert based on priority
        //Declaring temporary node t to traverse the linked list
        Node t = head;
        while (t.next != null && t.next.data.priority <= e.priority){
            t = t.next;
        }
        n.next = t.next;
        t.next = n;
    }
// Remove the highest priority emergency (lowest priority number)
// We are upposed to return entire emergency object, because of this we used Emergengcy as return type and not void.
    Emergency remove() {
        if (head == null) return null;
        Emergency e = head.data;
        head = head.next;
        return e;
    }
// Display all pending emergencies
    void display() {
        if (head == null) {
            System.out.println("No pending emergencies.");
            return;
        }
        for (Node t = head; t != null; t = t.next)
            t.data.display();
    }
}
// Rescue Team class to represent dispatchable teams
class RescueTeam {
    int id;
    String name;

    RescueTeam(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
// 2. QUEUE - Dispatchable Rescue Teams
class TeamQueue {
    class Node {
        RescueTeam data;
        Node next;
        Node(RescueTeam t) { data = t; } // Reference variable t
    }
// Front of the queue is where teams are removed, rear is where teams are added
    Node front, rear;
// Add a new rescue team to the queue
    void add(RescueTeam t) {
        Node n = new Node(t);
        if (rear == null) front = rear = n;
        else {
            rear.next = n;
            rear = n;
        }
    }
// Remove the next available rescue team from the queue
// We are upposed to return entire RescueTeam object, because of this we used RescueTeam as return type and not void.
    RescueTeam remove() {
        if (front == null) return null;
        RescueTeam t = front.data;
        front = front.next;
        if (front == null) rear = null;
        return t;
    }
// Display all available rescue teams
    void display() {
        if (front == null) {
            System.out.println("No available teams.");
            return;
        }
        for (Node t = front; t != null; t = t.next)
            System.out.println(t.data.id + " - " + t.data.name);
    }
}

// Mission class to link emergencies and rescue teams
class Mission {
    Emergency emergency;
    RescueTeam team;

    Mission(Emergency e, RescueTeam t) {
        emergency = e;
        team = t;
    }

    void display() {
        System.out.println("Emergency " + emergency.id + " (" +
                emergency.type + ") | Team: " + team.name +
                " | Location: " + emergency.location);
    }
}

// 3. LINKED LIST - Active missions
class MissionList {
    class Node {
        Mission data;
        Node next;
        Node(Mission m) { data = m; }
    }
// Head of the linked list of active missions
    Node head;

    void add(Mission m) {
        Node n = new Node(m);
        if (head == null) head = n;
        else {
            Node t = head;
            while (t.next != null) t = t.next;
            t.next = n;
        }
    }
// Remove a mission by emergency ID
    Mission remove(int id) {
        Node t = head, prev = null;
        while (t != null && t.data.emergency.id != id) {
            prev = t;
            t = t.next;
        }
        if (t == null) return null;
        if (prev == null) head = t.next;
        else prev.next = t.next;
        return t.data;
    }

    void display() {
        if (head == null) {
            System.out.println("No active missions.");
            return;
        }
        for (Node t = head; t != null; t = t.next)
            t.data.display();
    }
}

// 4. GRAPH - Locations and roads
class Graph {
    class Road {
        String destination;
        int distance;
        Road next; // Points to another road (also called self-referential structure)
// Linked list of roads from a location
        Road(String d, int w) {
            destination = d;
            distance = w;
        }
    }
// Each location has a name and a linked list of roads to other locations
    class Location {
        String name;
        Road roads;
        Location(String n) { name = n; }
    }
// Array of locations in the graph
    Location[] locations = new Location[50];
// Current number of locations in the graph
    int count = 0;
// Find the index of a location by name
// Useful as it allows us to quickly access the location in the array for adding roads or finding routes.
    int index(String name) {
        for (int i = 0; i < count; i++)
            if (locations[i].name.equalsIgnoreCase(name)) return i;
        return -1; // Not found
    }
// Add a new location if it doesn't already exist
    void addLocation(String name) {
        if (index(name) == -1 && count < locations.length)
            locations[count++] = new Location(name);
    }
// Add a road between two locations with a specified distance
    void addRoad(String a, String b, int distance) {
        addLocation(a);
        addLocation(b);
        int i = index(a), j = index(b);
// Add road from a to b
        Road r = new Road(b, distance);
        r.next = locations[i].roads;
        locations[i].roads = r;
// Add road from b to a (assuming undirected graph)
        r = new Road(a, distance);
        r.next = locations[j].roads;
        locations[j].roads = r;

        System.out.println("Road added successfully.");
    }
// Display all locations and their connected roads
    void display() {
        if (count == 0) {
            System.out.println("No roads added.");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.print(locations[i].name + " -> ");
            for (Road r = locations[i].roads; r != null; r = r.next)
                System.out.print(r.destination + "(" + r.distance + " km) ");
            System.out.println();
        }
    }
// Find the shortest route between two locations using Dijkstra's algorithm
    void shortestRoute(String source, String destination) {
        int s = index(source), d = index(destination);
        if (s == -1 || d == -1) {
            System.out.println("Location not found. Add roads first.");
            return;
        }
// Dijkstra's algorithm implementation
        int[] dist = new int[count], parent = new int[count];
        boolean[] visited = new boolean[count];
// Initialize distances and parents
        for (int i = 0; i < count; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dist[s] = 0;
// Find the shortest path to all locations
        for (int i = 0; i < count; i++) {
            int u = -1;
            for (int j = 0; j < count; j++)
                if (!visited[j] && (u == -1 || dist[j] < dist[u]))
                    u = j;
// If no unvisited node is reachable, break
            if (u == -1 || dist[u] == Integer.MAX_VALUE) break;
            visited[u] = true;
// Update distances to neighboring locations
            for (Road r = locations[u].roads; r != null; r = r.next) {
                int v = index(r.destination);
                if (!visited[v] && dist[u] + r.distance < dist[v]) {
                    dist[v] = dist[u] + r.distance;
                    parent[v] = u;
                }
            }
        }
// Display the shortest route and total distance
        if (dist[d] == Integer.MAX_VALUE) {
            System.out.println("No route available.");
            return;
        }
// Reconstruct the path from source to destination
        int[] path = new int[count];
        int size = 0;
        for (int v = d; v != -1; v = parent[v])
            path[size++] = v;
// Print the path in reverse order (from source to destination)
        System.out.print("Shortest Route: ");
        for (int i = size - 1; i >= 0; i--) {
            System.out.print(locations[path[i]].name);
            if (i > 0) System.out.print(" -> ");
        }
        System.out.println("\nTotal Distance: " + dist[d] + " km");
    }
}
// MAIN SYSTEM
public class ERMADS {
    static Scanner sc = new Scanner(System.in);
    static EmergencyPriorityQueue emergencies = new EmergencyPriorityQueue();
    static TeamQueue teams = new TeamQueue();
    static MissionList missions = new MissionList();
    static Graph graph = new Graph();
// Unique IDs for emergencies and teams
    static int emergencyId = 1, teamId = 1;

    static String input(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
// Get a valid integer input from the user
    static int number(String msg) {
        while (true) {
            try {
                return Integer.parseInt(input(msg));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
// Register a new emergency with type, location, and priority
    static void registerEmergency() {
        String[] types = {"Medical", "Fire", "Accident", "Other"};
        System.out.println("1. Medical  2. Fire  3. Accident  4. Other");
        int choice = number("Emergency type: ");
        if (choice < 1 || choice > 4) {
            System.out.println("Invalid type.");
            return;
        }
// Get location and priority from user
        String location = input("Location: ");
        int priority = number("Priority (1-Critical, 2-High, 3-Low): ");
        if (priority < 1 || priority > 3) {
            System.out.println("Invalid priority.");
            return;
        }
// Add the new emergency to the priority queue
        emergencies.add(new Emergency(emergencyId++, types[choice - 1],
                location, priority));
        System.out.println("Emergency registered.");
    }
// Add a new rescue team with a name
    static void addTeam() {
        String name = input("Team name: ");
        teams.add(new RescueTeam(teamId++, name));
        System.out.println("Rescue team added.");
    }
// Display the current system status with pending emergencies and available teams
    static void systemStatus() {
        System.out.println("\n--- Pending Emergencies ---");
        emergencies.display();
        System.out.println("\n--- Available Teams ---");
        teams.display();
    }
// Dispatch the highest priority emergency to the next available rescue team
    static void dispatch() {
        Emergency e = emergencies.remove();
        RescueTeam t = teams.remove();
// If no emergencies or teams are available, handle accordingly
        if (e == null) {
            System.out.println("No pending emergencies.");
            return;
        }
// If no available teams, re-add the emergency to the queue
        if (t == null) {
            emergencies.add(e);
            System.out.println("No available rescue teams.");
            return;
        }
// Create a new mission linking the emergency and the team, and add it to the active missions list
        missions.add(new Mission(e, t));
        System.out.println("Mission dispatched: " + e.id +
                " assigned to " + t.name);
    }
// Complete a mission by emergency ID, returning the team to the queue
    static void completeMission() {
        int id = number("Enter emergency ID: ");
        Mission m = missions.remove(id);

        if (m == null) {
            System.out.println("Mission not found.");
            return;
        }
// Return the team to the available teams queue
        teams.add(m.team);
        System.out.println("Mission completed. Team returned to queue.");
    }
// Add a road between two locations with a specified distance
    static void addRoad() {
        String a = input("Starting location: ");
        String b = input("Destination: ");
        int distance = number("Distance (km): ");
// Validate that the distance is positive
        if (distance <= 0) {
            System.out.println("Distance must be positive.");
            return;
        }
        graph.addRoad(a, b, distance);
    }

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== ERMADS =====");
            System.out.print("1. Register Emergency / ");
            System.out.print("2. Add Rescue Team / ");
            System.out.print("3. View System Status / ");
            System.out.print("4. Dispatch Mission / ");
            System.out.print("5. View Active Missions / ");
            System.out.print("6. Complete Mission / ");
            System.out.print("7. Add Road / ");
            System.out.print("8. Find Shortest Route / ");
            System.out.print("9. Display Road Network / ");
            System.out.println("0. Exit");

            choice = number("Enter choice: ");

            switch (choice) {
                case 1: registerEmergency(); break;
                case 2: addTeam(); break;
                case 3: systemStatus(); break;
                case 4: dispatch(); break;
                case 5: missions.display(); break;
                case 6: completeMission(); break;
                case 7: addRoad(); break;
                case 8:
                    graph.shortestRoute(input("Start: "), input("Destination: "));
                    break;
                case 9: graph.display();
                    break;
                case 0: System.out.println("Exiting ERMADS."); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }
}
