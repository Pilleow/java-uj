import java.util.HashSet;
import java.util.Stack;

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
        // headingOf(direction) == sumOfTurns, wtedy po prostu idziemy naprzód.

        Stack<Direction> directionsTaken = new Stack<>();
        HashSet<Position> positionsToAvoid = new HashSet<>();
        Position relPos = new Position(0, 0);
        Position prevPrevPos = relPos;
        Direction direction = Direction.NORTH;
        int sumOfTurns = 0;
        int waterMovesDone = 0;
        boolean fireToRight = false;
        boolean fireToFront = false;

        while (true) {

            controller.print(positionsToAvoid);  // TODO: TO JEST DO WYRZUCENIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (!(relPos.col() == controller.getRelativePos().col() && relPos.row() == controller.getRelativePos().row())) {
                throw new IllegalStateException(relPos + " is not representative of " + controller.getRelativePos());
            }// TODO: TO JEST DO WYRZUCENIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            System.out.println("WATER MOVES DONE: " + waterMovesDone);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Najpierw: czy musimy uciekać? (np. płomienie).
            if (fireToRight || fireToFront) {
                if (directionsTaken.isEmpty()) directionsTaken.push(directions[(int)(Math.random() * directions.length)]);
                direction = oppositeOf(directionsTaken.pop());
                try {
                    controller.move(direction);
                } catch (Flooded e) {
                    waterMovesDone += 1;
                } catch (Wall | Exit | OnFire e) {
                    // tu nic nie trzeba, wiemy, że jak cofniemy się
                    //  to nie ma tam ani ściany, ani wyjścia, ani ognia.
                }
                relPos = direction.step(relPos);
                fireToFront = false;
                fireToRight = false;
            }

            // teraz: czy jesteśmy w wodzie i czy kończy nam się powietrze?
            if (waterMovesDone >= waterAllowedMoves / 2) {
                if (waterMovesDone == waterAllowedMoves / 2) positionsToAvoid.add(direction.step(relPos)); // tu nam się skończyło powietrze, nie ma co tędy próbować iść.
                if (directionsTaken.isEmpty()) directionsTaken.push(directions[(int)(Math.random() * directions.length)]);
                direction = oppositeOf(directionsTaken.pop());
                try {
                    controller.move(direction);
                    relPos = direction.step(relPos);
                    waterMovesDone = 0;
                } catch (Flooded e) {
                    waterMovesDone += 1;
                    relPos = direction.step(relPos);
                } catch (Wall | OnFire | Exit e) {
                    // wiemy, że tam, gdzie byliśmy wcześniej, jest bezpiecznie i że nie ma tam Exit.
                    waterMovesDone = 0;

                    relPos = direction.step(relPos);
                }
                continue;
            }

            // najpierw sprawdzamy, czy na prawo jest ściana lub jakieś płomienie itp.
            if ((headingOf(direction) != 0 || sumOfTurns != 0) && !positionsToAvoid.contains(toTheRightOf(direction).step(relPos))) {
                if (!positionsToAvoid.isEmpty()) prevPrevPos = oppositeOf(directionsTaken.peek()).step(relPos);
                try {
                    controller.move(toTheRightOf(direction));
                    relPos = toTheRightOf(direction).step(relPos);
                    directionsTaken.push(toTheRightOf(direction));

                    direction = toTheRightOf(direction);
                    waterMovesDone = 0;
                    sumOfTurns += 1;
                    continue; // zmieniamy kierunek, bo wolne jest na prawo.
                } catch (OnFire e) {
                    relPos = toTheRightOf(direction).step(relPos);
                    directionsTaken.push(toTheRightOf(direction));

                    positionsToAvoid.add(relPos);
                    fireToRight = true;
                    waterMovesDone = 0;
                    sumOfTurns += 1;
                    continue;
                } catch (Flooded e) {
                    relPos = toTheRightOf(direction).step(relPos);
                    directionsTaken.push(toTheRightOf(direction));

                    direction = toTheRightOf(direction);
                    waterMovesDone += 1;
                    sumOfTurns += 1;
                } catch (Wall e) {
                    positionsToAvoid.add(toTheRightOf(direction).step(relPos));
                    // super, że mamy ścianę :).
                } catch (Exit e) {
                    relPos = toTheRightOf(direction).step(relPos);
                    directionsTaken.push(toTheRightOf(direction));

                    waterMovesDone = 0;
                    sumOfTurns += 1;

                    System.out.println("Victoria");
                    return;
                }
            }

            // teraz, skoro na prawo jest ściana, możemy lecieć dalej.
            try {
                if (positionsToAvoid.contains(direction.step(relPos))) {
                    throw new Wall();
                }
                if (!positionsToAvoid.isEmpty()) prevPrevPos = oppositeOf(directionsTaken.peek()).step(relPos);
                controller.move(direction);
                relPos = direction.step(relPos);
                directionsTaken.push(direction);

                waterMovesDone = 0;
            } catch (OnFire e) {
                relPos = direction.step(relPos);
                directionsTaken.push(direction);

                positionsToAvoid.add(relPos);
                waterMovesDone = 0;
                fireToFront = true;
                continue;
            } catch (Flooded e) {
                relPos = direction.step(relPos);
                directionsTaken.push(direction);

                waterMovesDone += 1;

            } catch (Wall e) {
                positionsToAvoid.add(direction.step(relPos));
                // no to teraz niestety trzeba iść w lewo.
                direction = toTheLeftOf(direction);
                sumOfTurns -= 1;
                continue;
            } catch (Exit e) {
                relPos = direction.step(relPos);
                directionsTaken.push(direction);

                waterMovesDone = 0;

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
