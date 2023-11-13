import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NumberStatistics implements Statistics {
    private int sLen;
    Map<Integer, Set<Position>> numbers;
    @Override
    public void sideLength(int length) {
        sLen = length;
    }

    @Override
    public void addNumbers(Map<Integer, Set<Position>> numberPositions) {
        numbers = numberPositions;
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
        /*
        Plan jest taki:
        iterujemy przez wszystkie liczby i dla każdej liczby niebędącej
        na tej pozycji co my, liczymy pozycję relatywną względem naszej
        pozycji. Licząc relatywną pozycję, uwzględniamy periodyczne
        warunki brzegowe!
         */
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
        Position relPos;
        int distSq;

        for (int num : numbers.keySet()) {
            for (Position numPos : numbers.get(num)) {
                if (position.equals(numPos)) continue; // pomijamy naszą pozycję
                relPos = getRelativePosition(position, numPos);
                distSq = relPos.col() * relPos.col() + relPos.row() * relPos.row();
                // System.out.println(num + "; " + numPos + "; " + relPos + "; " + distSq);
                if (distSq > maxDistanceSquared) continue; // pomijamy, za daleko

                if (!result.containsKey(num)) result.put(num, new HashMap<>());
                if (!result.get(num).containsKey(distSq)) result.get(num).put(distSq, 1);
                else result.get(num).put(distSq, (result.get(num).get(distSq) + 1));
            }
        }
        return result;
    }
    private Position getRelativePosition(Position pC, Position pA) {
        return new Position(
                 (int)(Modulo(pA.col() - pC.col() + sLen / 2.0, sLen) - sLen / 2.0),
                 (int)(Modulo(pA.row() - pC.row() + sLen / 2.0, sLen) - sLen / 2.0)

        );
    }
    private static double Modulo(double value, int mod) {
        // Poprawka dla operatora %, aby dla ujemnych
        // liczb operator % zawsze zwracał najmniejszą
        // nieujemną wartość, np.:
        // -2 % 5        = -2   :(
        // Modulo(-2, 5) =  3   :)
        return (value % mod + mod) % mod;
    }
}
