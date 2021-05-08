import java.util.*;

public class Solver {
    private final String VERTICALS;
    private final String HORIZONTALS;
    private final String LENGTH2;
    private final String LENGTH3;
    private final char EMPTY = '0';
    // letter representing main vehicle
    private final char MAIN = 'A';
    private final char VERTICAL = 'v';
    private final char HORIZONTAL = 'h';
    // String representation of initial map
    private final String MAP;
    public static final int ROWS = 6;
    public static final int COLS = 6;
    private final int MAXDISTANCE = ROWS - 2;
    private boolean DFS;
    private boolean printMaps;

    // BFS queue
    private Queue<String> queue = new LinkedList<>();
    // DFS stack
    private Stack<String> stack = new Stack<>();
    // maps char representation of vehicles in String map to original vehicle names
    private Map<Character,String> carNames = new HashMap<>();
    // map to trace the solution && store already visited states
    private Map<String, String> traces = new HashMap<>();
    // maps each state(map) to a step with which it was reached
    // integer - hashcode of string (less memory)
    private Map<Integer, String> steps = new HashMap<>();

    /**
     * Constructor initializes game data (constants)
     * Converts input format to String representation of map
     * @param initialState String representing initial state
     * @param printMaps false - only steps will be printed
     *                  true - steps + maps on path will be printed
     */
    public Solver(String initialState, boolean printMaps) {
        this.printMaps = printMaps;
        StringBuilder emptyMap = new StringBuilder();
        for (int i = 0; i < ROWS * COLS; i++)
            emptyMap.append(EMPTY);
        String mapOut = emptyMap.toString();
        initialState = initialState.substring(1, initialState.length() - 1);
        initialState = initialState.replace("(", "");
        initialState = initialState.replace(")", ";");
        StringBuilder len2VehiclesSB = new StringBuilder();
        StringBuilder len3VehiclesSB = new StringBuilder();
        StringBuilder horizontalVehiclesSB = new StringBuilder();
        StringBuilder verticalVehiclesSB = new StringBuilder();
        String[] cars = initialState.split(";");
        int charIndex = 65; // 'A'
        for (String car : cars) {
            String[] carsInfo = car.split(" ");
            char carRepresent = (char)(charIndex++);
            carNames.put(carRepresent,carsInfo[0]);
            char orientation = carsInfo[4].charAt(0);
            int r = Integer.parseInt(carsInfo[2]);
            int c = Integer.parseInt(carsInfo[3]);
            int length = Integer.parseInt(carsInfo[1]);
            if (length == 2)
                len2VehiclesSB.append(carRepresent);
            if (length == 3)
                len3VehiclesSB.append(carRepresent);
            if (orientation == 'v') {
                for (int i = 0; i < length; i++) {
                    mapOut = Utility.changeCharInPosition(Utility.rcToIndex(r - 1 + i, c - 1), carRepresent, mapOut);
                }
                verticalVehiclesSB.append(carRepresent);
            }
            if (orientation == 'h') {
                for (int i = 0; i < length; i++) {
                    mapOut = Utility.changeCharInPosition(Utility.rcToIndex(r - 1, c - 1 + i), carRepresent, mapOut);
                }
                horizontalVehiclesSB.append(carRepresent);
            }
        }
        this.MAP = mapOut;
        this.LENGTH2 = len2VehiclesSB.toString();
        this.LENGTH3 = len3VehiclesSB.toString();
        this.VERTICALS = verticalVehiclesSB.toString();
        this.HORIZONTALS = horizontalVehiclesSB.toString();
    }

    /**
     * initializes vehicle data
     * @param vehicle vehicle to be initialized
     */
    private void initializeVehicle(Vehicle vehicle) {
        if (Utility.charInString(vehicle.getCarRepresent(), HORIZONTALS)) {
            vehicle.setOrientation(HORIZONTAL);
        }
        if (Utility.charInString(vehicle.getCarRepresent(), VERTICALS)) {
            vehicle.setOrientation(VERTICAL);
        }
        if (Utility.charInString(vehicle.getCarRepresent(), LENGTH2)) {
            vehicle.setLength(2);
        }
        if (Utility.charInString(vehicle.getCarRepresent(), LENGTH3)) {
            vehicle.setLength(3);
        }
        vehicle.findEndPosition();
    }

    /**
     * solves the game by BFS algorithm
     */
    public void solveBFS() {
        queue.add(MAP);
        preserveMap(MAP,null);
        while (true) {
            if(queue.isEmpty()) {
                System.out.println("No solution found");
                break;
            }
            String newMap = queue.remove();
            if (isSolution(newMap)) {
                trace(newMap);
                System.out.println("--- " + traces.size()  + " unique states generated ---");
                break;
            }
            StringBuilder alreadyFound = new StringBuilder();
            for (int i = 0; i < ROWS * COLS; i++) {
                if (newMap.charAt(i) != EMPTY && alreadyFound.toString().indexOf(newMap.charAt(i)) < 0) {
                    alreadyFound.append(newMap.charAt(i));
                    char carRepresentation = newMap.charAt(i);
                    Vehicle vehicle = new Vehicle(Utility.indexToR(i), Utility.indexToC(i), carRepresentation);
                    initializeVehicle(vehicle);
                    findNewMaps(vehicle, newMap);
                }
            }
        }
    }

    /**
     * solves the game by DFS algorithm
     */
    public void solveDFS() {
        DFS = true;
        stack.push(MAP);
        preserveMap(MAP,null);
        while (true) {
            if(stack.isEmpty()) {
                System.out.println("No solution found");
                break;
            }
            String newMap = stack.pop();
            if (isSolution(newMap)) {
                trace(newMap);
                System.out.println("--- " + traces.size()  + " unique states generated ---");
                break;
            }
            StringBuilder alreadyFound = new StringBuilder();
            for (int i = 0; i < ROWS * COLS; i++) {
                if (newMap.charAt(i) != EMPTY && alreadyFound.toString().indexOf(newMap.charAt(i)) < 0) {
                    alreadyFound.append(newMap.charAt(i));
                    char carRepresentation = newMap.charAt(i);
                    Vehicle vehicle = new Vehicle(Utility.indexToR(i), Utility.indexToC(i), carRepresentation);
                    initializeVehicle(vehicle);
                    findNewMaps(vehicle, newMap);
                }
            }
        }
    }

    private void preserveStep(String map, String step) {
            steps.put(map.hashCode(), step);
    }

    private void preserveMap(String newMap, String prevMap) {
            traces.put(newMap, prevMap);
    }


    /**
     * checks if given map was already explored (traces.containsKey())
     * depending on which algo is used DFS | BFS
     * puts the map in stack | queue
     * @param newMap map to be put in stack | queue
     * @return true if enqueuing or pushing was successful, false otherwise
     */
    private boolean propose(String newMap) {
        if (DFS) {
            if (!traces.containsKey(newMap)) {
                stack.push(newMap);
                return true;
            }
        } else {
            if (!traces.containsKey(newMap)) {
                queue.add(newMap);
                return true;
            }
        }
        return false;
    }

    /**
     * Generates string representation of move to be printed
     * @param direction string representation of direction
     * @param represent char representation of vehicle
     * @param distance distance
     * @return String representation of move
     */
    private String generateMove(String direction, char represent, int distance)
    {
        if (printMaps)
            return direction + "(" + carNames.get(represent) + " (" + represent + ")" + " "  + distance + ")";
        else
            return direction + "(" + carNames.get(represent) + " "  + distance + ")";
    }

    /**
     * Makes all possible moves of given vehicle on given map
     * proposes all newly created maps
     * @param vehicle vehicle to be moved
     * @param map map on which the vehicle is moved
     */
    private void findNewMaps(Vehicle vehicle, String map) {
        if (vehicle.getOrientation() == VERTICAL) {
            for (int i = 1; i <= MAXDISTANCE ; i++) {
                if (canMoveUp(vehicle, map, i)) {
                    String newMap = moveUp(vehicle, map, i);
                    String newMove = generateMove("HORE", vehicle.getCarRepresent(), i);
                     if (propose(newMap)) {
                        preserveMap(newMap, map);
                        preserveStep(newMap, newMove);
                    }
                }
                else
                    break;
            }
            for (int i = 1; i <= MAXDISTANCE; i++) {
                if (canMoveDown(vehicle, map, i)) {
                    String newMap = moveDown(vehicle, map, i);
                    String newMove = generateMove("DOLE", vehicle.getCarRepresent(), i);
                    if (propose(newMap)) {
                        preserveMap(newMap, map);
                        preserveStep(newMap, newMove);
                    }
                }
                else
                    break;
            }
        }
        if (vehicle.getOrientation() == HORIZONTAL) {
            for (int i = 1; i <= MAXDISTANCE; i++) {
                if (canMoveLeft(vehicle, map, i)) {
                    String newMap = moveLeft(vehicle, map, i);
                    String newMove = generateMove("VLAVO", vehicle.getCarRepresent(), i);
                    if (propose(newMap)) {
                        preserveMap(newMap, map);
                        preserveStep(newMap, newMove);
                    }
                }
                else
                    break;
            }
            for (int i = 1; i <= MAXDISTANCE; i++) {
                if (canMoveRight(vehicle, map, i)) {
                    String newMap = moveRight(vehicle, map, i);
                    String newMove = generateMove("VPRAVO", vehicle.getCarRepresent(), i);
                    if (propose(newMap)) {
                        preserveMap(newMap, map);
                        preserveStep(newMap, newMove);
                    }
                }
                else
                    break;
            }
        }
    }

    /**
     * Decides whether given map is goal state (main vehicle is in last col)
     * @param map map to be controlled
     * @return true if map is goal state, false otherwise
     */
    private boolean isSolution(String map) {
        for(int i = COLS - 1; i < map.length(); i += COLS)
        {
            if (map.charAt(i) == MAIN)
                return true;
        }
        return false;
    }

    /**
     * traces back the path to given goal state
     * by using traces hashMap
     * @param current state to be traced
     * @return helper index to be printed with each move
     */
    private int trace(String current) {
        String prev = traces.get(current);
        int index = 0;
        if (prev != null)
            index = trace(prev) + 1;

        if(index != 0) {
            System.out.print(index + " ");
            System.out.println(steps.get(current.hashCode()));
        }
        if(printMaps)
            Utility.printMap(current);
        return index;
    }

    /*
     * functions representing moves to given direction
     * canMove first checks whether the move can be done
     * move makes the move
     * @param vehicle vehicle to be moved
     * @param map map on which the vehicle is to be moved
     * @param distance length of the move
     * @return new map generated by the move
     */
    private boolean canMoveUp(Vehicle vehicle, String map, int distance) {
        return vehicle.getBeginningR() > distance - 1 && map.charAt(Utility.rcToIndex(vehicle.getBeginningR() - distance,
                vehicle.getBeginningC())) == EMPTY;
    }
    private String moveUp(Vehicle vehicle, String map, int distance) {
        String newMap = map;
        for(int i = 1; i <= distance; i++) {
                newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getEndR() - i + 1, vehicle.getEndC()),
                        EMPTY, newMap);
                newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getBeginningR() - i, vehicle.getBeginningC()),
                        vehicle.getCarRepresent(), newMap);
        }
        return newMap;
    }

    private boolean canMoveDown(Vehicle vehicle, String map, int distance) {
        return vehicle.getEndR() < COLS - distance  && map.charAt(Utility.rcToIndex(vehicle.getEndR() + distance, vehicle.getEndC())) == EMPTY;
    }
    private String moveDown(Vehicle vehicle, String map, int distance) {
        String newMap = map;
        for(int i = 1; i <= distance; i++) {
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getBeginningR() + i - 1, vehicle.getBeginningC()), EMPTY,
                    newMap);
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getEndR() + i, vehicle.getEndC()),
                    vehicle.getCarRepresent(), newMap);
        }
        return newMap;
    }

    private boolean canMoveLeft(Vehicle vehicle, String map, int distance) {
        return vehicle.getBeginningC() > distance - 1 && map.charAt(Utility.rcToIndex(vehicle.getBeginningR(),
                vehicle.getBeginningC() - distance)) == EMPTY;
    }
    private String moveLeft(Vehicle vehicle, String map, int distance) {
        String newMap = map;
        for(int i = 1; i <= distance; i++) {
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getEndR(), vehicle.getEndC()) - i + 1, EMPTY,
                    newMap);
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getBeginningR(), vehicle.getBeginningC() - i),
                    vehicle.getCarRepresent(), newMap);
        }
        return newMap;
    }

    private boolean canMoveRight(Vehicle vehicle, String map, int distance) {
        return vehicle.getEndC() < COLS - distance && map.charAt(Utility.rcToIndex(vehicle.getEndR(),
                vehicle.getEndC() + distance)) == EMPTY;
    }
    private String moveRight(Vehicle vehicle, String map, int distance) {
        String newMap = map;
        for(int i = 1; i <= distance; i++) {
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getBeginningR(),
                    vehicle.getBeginningC() + i - 1), EMPTY, newMap);
            newMap = Utility.changeCharInPosition(Utility.rcToIndex(vehicle.getEndR(), vehicle.getEndC() + i),
                    vehicle.getCarRepresent(), newMap);
        }
        return newMap;
    }
}
