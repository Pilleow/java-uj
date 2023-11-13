
public class PC1 implements PlayerController {

    final byte[][] map1 = { // ustaw STARTPOS = (5, 2)
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 0, 1, 1, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 0},
            {0, 0, 1, 0, 0, 1, 3, 0, 1, 0},
            {0, 3, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 3, 1, 0, 0},
            {0, 2, 2, 2, 2, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 1, 0, 0, 2, 0},
            {0, 1, 0, 3, 1, 1, 2, 2, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    final byte[][] map2 = { // ustaw STARTPOS = (5, 4)
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 1},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 0, 0, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    final byte[][] map3 = { // ustaw STARTPOS = (5, 4)
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 3, 3, 1, 3, 3, 1, 1, 1},
            {3, 1, 1, 3, 1, 1, 3, 3, 3, 3},
            {3, 1, 1, 3, 1, 1, 3, 1, 1, 3},
            {3, 1, 1, 3, 3, 3, 3, 1, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    };
    final byte[][] map4 = { // ustaw STARTPOS = (5, 4)
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 1, 3, 1, 2, 3, 3, 1, 0},
            {0, 1, 2, 2, 1, 2, 2, 2, 2, 0},
            {0, 2, 0, 0, 0, 0, 0, 1, 2, 2},
            {0, 2, 1, 0, 1, 1, 0, 0, 0, 0},
            {0, 1, 2, 0, 1, 1, 0, 1, 3, 0},
            {0, 2, 2, 0, 1, 0, 0, 2, 2, 0},
            {0, 1, 2, 2, 2, 1, 2, 2, 1, 0},
            {0, 2, 2, 1, 2, 2, 2, 2, 2, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    final byte[][] map = map4; // TUTAJ ZMIENIA SIĘ MAPĘ
    // 0 - ściana
    // 1 - ścieżka
    // 2 - woda
    // 3 - ogień

    final Position STARTPOS = new Position(5, 4);
    Position currentPos = STARTPOS;
    Position drawnAtPos = currentPos;
    int timesOnFire = 0;

    /**
     * Metoda pozwala na zlecenie wykonania pojadynczego ruchu w określonym
     * kierunku. O ile jest to tylko możliwe, położenie gracza ulega zmianie.
     * Wyjątkiem jest wystąpienie wyjątku Wall, który oznacza, że ruch nie nastąpił,
     * bo na pozycji decelowej znajduje się ściana.
     *
     * @param direction kierunek ruchu
     * @throws OnFire  ruch wykonano, ale docelowe pomieszczenie płonie. Należy
     *                 natychmiast (w kolejnym ruchu) z niego uciekać.
     * @throws Flooded pomieszczenie zalane wodą. Należy kontrolować czas (liczbę
     *                 dozwolonych kroków) przebywania pod wodą.
     * @throws Wall    ruchu nie wykonano, bo natrafiono na ścianę
     * @throws Exit    Sukces! Odnaleziono wyjście. Koniec gry!
     */
    @Override
    public void move(Direction direction) throws OnFire, Flooded, Wall, Exit {
        Position nextPos = direction.step(currentPos);

        try {
            switch (map[nextPos.row()][nextPos.col()]) {
                case 0:
                    throw new Wall();
                case 1:
                    currentPos = nextPos;
                    return;
                case 2:
                    currentPos = nextPos;
                    throw new Flooded();
                case 3:
                    currentPos = nextPos;
                    timesOnFire += 1;
                    throw new OnFire();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new Exit();
        }
    }
}
