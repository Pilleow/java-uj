import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Statistics ns = new NumberStatistics();
        ns.sideLength(9);

        Map<Integer, Set<Position>> numPositions = new HashMap<>();

//        numPositions.put(1, new HashSet<Position>() {{
//            add(new Position(0, 4));
//            add(new Position(0, 8));
//            add(new Position(2, 6));
//            add(new Position(4, 3));
//        }});
//        numPositions.put(2, new HashSet<Position>() {{
//            add(new Position(0, 3));
//            add(new Position(8, 0));
//            add(new Position(8, 4));
//        }});
//        numPositions.put(3, new HashSet<Position>() {{
//            add(new Position(5, 6));
//            add(new Position(8, 8));
//        }});
//        numPositions.put(4, new HashSet<Position>() {{
//            add(new Position(0, 0));
//            add(new Position(6, 5));
//        }});

        numPositions.put(1, new HashSet<Position>() {{
            add(new Position(4, 3));
            add(new Position(4, 5));
            add(new Position(2, 6));
        }});
        numPositions.put(2, new HashSet<Position>() {{
            add(new Position(3, 3));
        }});
        numPositions.put(3, new HashSet<Position>() {{
            add(new Position(5, 6));
        }});
        numPositions.put(4, new HashSet<Position>() {{
            add(new Position(5, 4));
            add(new Position(6, 5));
        }});

        ns.addNumbers(numPositions);

        System.out.println(ns.neighbours(new Position(4, 4), 20));
    }
}
