public class Tester {
    public static void main(String[] args) {
        PlayerController pc = new PC1();
        Explorer exp = new Indiana();
        exp.setPlayerController(pc);
        exp.underwaterMovesAllowed(8);
        exp.findExit();
    }
}
