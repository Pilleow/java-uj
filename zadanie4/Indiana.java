import java.util.HashMap;
import java.util.HashSet;

public class Indiana implements Explorer {

    final Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    PlayerController controller;
    int waterAllowedMoves;

    /**
     * Czas (liczba kolejnych dozwolonych ruchów) pod wodą.
     * @param moves liczba ruchów.
     */
    @Override
    public void underwaterMovesAllowed(int moves) {
        waterAllowedMoves = moves;
    }

    /**
     * Przekazanie kontrolera gracza.
     * @param controller kontroler gracza
     */
    @Override
    public void setPlayerController(PlayerController controller) {
        this.controller = controller;
    }

    /**
     * Rozpoczęcie poszukiwania wyjścia. Można zacząć wykonywać metody move() z
     * interfejsu kontroler.
     */
    @Override
    public void findExit() {
        // cały czas będziemy się trzymać prawej ściany, chyba że
        // headingOf(direction) == sumOfTurns, wtedy po prostu idziemy naprzód

        HashSet<Position> positionsToAvoid = new HashSet<>();
        Position relPos = new Position(0, 0);
        Direction direction = Direction.NORTH;
        int sumOfTurns = 0;
        boolean fireToRight = false;
        boolean fireToFront = false;

        while (true) {

            if (!(relPos.col() == controller.getRelativePos().col() && relPos.row() == controller.getRelativePos().row())) throw new IllegalStateException(relPos + " is not representative of " + controller.getRelativePos()); // TODO: TO JEST DO WYRZUCENIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // najpierw: czy musimy uciekać? (np. płomienie)
            if (fireToRight || fireToFront) {
                try {
                    controller.move(fireToFront ? oppositeOf(direction) : oppositeOf(toTheRightOf(direction)));
                } catch (Flooded e) {
                    // to trzeba złapać, co jak wrócimy do wody?
                } catch (Wall | Exit | OnFire e) {
                    // tu nic nie trzeba, wiemy że jak cofniemy się
                    // do tyłu to nie ma tam ani ściany, ani wyjścia, ani ognia
                }
                relPos = (fireToFront ? oppositeOf(direction) : oppositeOf(toTheRightOf(direction))).step(relPos);
                fireToFront = false;
                fireToRight = false;
            }

            // najpierw sprawdzamy, czy na prawo jest ściana lub jakieś płomienie itp.
            if ((headingOf(direction) != 0 || sumOfTurns != 0) && !positionsToAvoid.contains(toTheRightOf(direction).step(relPos))) {
                try {
                    controller.move(toTheRightOf(direction));
                    relPos = toTheRightOf(direction).step(relPos);
                    controller.print(direction);  // TODO: TO JEST DO WYRZUCENIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    direction = toTheRightOf(direction);
                    sumOfTurns += 1;
                    continue; // zmieniamy kierunek, bo wolne jest na prawo
                } catch (OnFire e) {
                    System.out.println(e);
                    relPos = toTheRightOf(direction).step(relPos);
                    sumOfTurns += 1;
                    positionsToAvoid.add(relPos);
                    fireToRight = true;
                    continue;
                } catch (Flooded e) {
                    System.out.println(e);
                    relPos = toTheRightOf(direction).step(relPos);
                    sumOfTurns += 1;
                } catch (Wall e) {
                    System.out.println(e);
                    positionsToAvoid.add(toTheRightOf(direction).step(relPos));
                    // super, że mamy ścianę :)
                } catch (Exit e) {
                    System.out.println(e);
                    relPos = toTheRightOf(direction).step(relPos);
                    sumOfTurns += 1;
                    System.out.println("Victoria");
                    return;
                }
            }

            // teraz, skoro na prawo jest ściana, możemy lecieć dalej
            try {
                if (positionsToAvoid.contains(direction.step(relPos))) throw new Wall();
                controller.move(direction);
                relPos = direction.step(relPos);
                controller.print(direction);  // TODO: TO JEST DO WYRZUCENIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            } catch (OnFire e) {
                System.out.println(e);
                relPos = direction.step(relPos);
                positionsToAvoid.add(relPos);
                fireToFront = true;
                continue;
            } catch (Flooded e) {
                System.out.println(e);
                relPos = direction.step(relPos);
            } catch (Wall e) {
                System.out.println(e);
                positionsToAvoid.add(direction.step(relPos));
                // no to teraz niestety trzeba iść w lewo
                direction = toTheLeftOf(direction);
                sumOfTurns -= 1;
                continue;
            } catch (Exit e) {
                System.out.println(e);
                relPos = direction.step(relPos);
                System.out.println("Victoria");
                return;
            }
        }
    }

    private int headingOf(Direction d) {
        return switch (d) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
        };
    }
    private Direction toTheLeftOf(Direction d) {
        return switch (d) {
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
        };
    }

    private Direction toTheRightOf(Direction d) {
        return switch (d) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }
    private Direction oppositeOf(Direction d) {
        return switch (d) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
        };
    }
}
