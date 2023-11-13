public class PC1 implements PlayerController {

    final byte[][] map = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 0, 1, 1, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 0},
            {0, 0, 1, 0, 0, 1, 3, 0, 1, 0},
            {0, 3, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 3, 1, 0, 0},
            {0, 1, 1, 1, 1, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 1, 0, 0, 2, 0},
            {0, 1, 0, 3, 1, 1, 2, 2, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    // 0 - ściana
    // 1 - ścieżka
    // 2 - woda
    // 3 - ogień

    final Position STARTPOS = new Position(5, 2);
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
                    System.out.println("FIREEE");
                    timesOnFire += 1;
                    throw new OnFire();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new Exit();
        }
    }

    public void print(Direction direction) {

        Position nextPos = direction.step(currentPos);
        // printing board to screen
        if (!drawnAtPos.equals(currentPos)) {
            drawnAtPos = currentPos;
            System.out.println(new String(new char[50]).replace("\0", "\r\n"));

            int x;

            for (int y = 0; y < map.length; ++y) {
                if ((y - STARTPOS.col()) >= 0 && (y - STARTPOS.col()) < 10) System.out.print(" " + (y - STARTPOS.col()));
                else System.out.print((y - STARTPOS.col()));
            }
            System.out.println();
            for (int y = 0; y < map.length; ++y) {
                for (x = 0; x < map[0].length; ++x) {
                    if (currentPos.row() == y && currentPos.col() == x) System.out.print("\uD83D\uDC0D");
                    else if (nextPos.row() == y && nextPos.col() == x) System.out.print("**");
                    else {
                        switch (map[y][x]) {
                            case 0:
                                System.out.print("\uD83D\uDDFF");
                                break;
                            case 1:
                                System.out.print("  ");
                                break;
                            case 2:
                                System.out.print("\uD83C\uDF0A");
                                break;
                            case 3:
                                System.out.print("\uD83D\uDD25");
                                break;
                        }
                    }
                }
                if (y - STARTPOS.row() >= 0 && y - STARTPOS.row() < 10) System.out.println(" " + (y - STARTPOS.row()));
                else System.out.println((y - STARTPOS.row()));
            }
            System.out.println("Times on fire: " + timesOnFire);
        }

    }
}
