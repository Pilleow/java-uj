public class K1 implements KostkaDoGry {

    @Override
    public int rzut() {
        return (int)(Math.random() * 6) + 1;
    }
}
