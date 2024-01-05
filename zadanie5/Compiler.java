import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class LR implements ProgrammableCalculatorInterface.LineReader {
    @Override
    public String readLine() {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        in.close();
        return s;
    }
}

class LP implements ProgrammableCalculatorInterface.LinePrinter {
    @Override
    public void printLine(String line) {
        System.out.println(line);
    }
}

public class Compiler {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader("main.ORAMUS"));

        ProgrammableCalculator pc = new ProgrammableCalculator();
        pc.programCodeReader(reader);
        pc.setStdin(new LR());
        pc.setStdout(new LP());

        pc.run(3);
    }
}
