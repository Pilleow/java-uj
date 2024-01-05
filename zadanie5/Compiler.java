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

public class Compiler extends Thread {
    public static void main(String[] args)  {

        Compiler thread = new Compiler();
        thread.start();
        long start, end;
        start = System.currentTimeMillis();
        while (thread.isAlive()) {
            try {
                //noinspection BusyWait
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms.");
    }

    public void run() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("main.ORAMUS"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ProgrammableCalculator2 pc = new ProgrammableCalculator2();
        pc.programCodeReader(reader);
        pc.setStdin(new LR());
        pc.setStdout(new LP());

        pc.run(1);
    }
}
