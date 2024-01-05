import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;

class ProgrammableCalculator implements ProgrammableCalculatorInterface {
    BufferedReader reader;
    LineReader input;
    LinePrinter output;
    int line = 0;
    boolean run;
    boolean gotoExecuted = false;
    HashMap<Integer, String[]> codeLines = new HashMap<>();
    HashMap<String, Integer> variables = new HashMap<>();
    int[] linesArray;

    /**
     * Metoda ustawia BufferedReader, który pozwala na odczyt kodu źródłowego
     * programu.
     *
     * @param reader obiekt BufferedReader.
     */
    @Override
    public void programCodeReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Przekierowanie standardowego wejścia
     *
     * @param input nowe standardowe wejście
     */
    @Override
    public void setStdin(ProgrammableCalculatorInterface.LineReader input) {
        this.input = input;
    }

    /**
     * Przekierowanie standardowego wyjścia
     *
     * @param output nowe standardowe wyjście
     */
    @Override
    public void setStdout(LinePrinter output) {
        this.output = output;
    }

    /**
     * Rozpoczęcie realizacji kodu programu od wskazanego numeru linii.
     *
     * @param line numer linii programu
     */
    @Override
    public void run(int line) {
        this.line = line;
        parseCodeFromReader();

        linesArray = Arrays.stream(codeLines.keySet().toArray(Integer[]::new)).mapToInt(Integer::intValue).toArray();
        Arrays.sort(linesArray);
        int lineArrayIndex = getIndexOfCurrentLine();

        // Teraz w codeLines mamy każdą linię kodu w formacie
        // [int lineNumber, String[] args]
        // oraz w linesArray mamy wszystkie numery linii kodu w odpowiedniej kolejności
        // np. [10, 20, 25, 30, 40, 50, 55, ...]
        //
        // teraz rozpoczynamy faktyczną realizację kodu

        run = true;
        while (run) {
            if (!codeLines.containsKey(line)) throw new IllegalStateException();
            parseLine(codeLines.get(this.line));

            if (gotoExecuted) {
                lineArrayIndex = getIndexOfCurrentLine();
                gotoExecuted = false;
            } else lineArrayIndex++;

            if (lineArrayIndex >= linesArray.length) run = false;
            else this.line = linesArray[lineArrayIndex];
        }
    }

    private int getIndexOfCurrentLine() {
        for (int i = 0; i < linesArray.length; ++i) {
            if (line == linesArray[i]) return i;
        }
        return -1;
    }

    private void parseCodeFromReader() {
        String[] codeLineSplit;

        String[] linesToRead = reader.lines().toArray(String[]::new);
        for (String codeLine : linesToRead) {
            if (codeLine == null || codeLine.trim().isEmpty()) continue;
            codeLineSplit = codeLine.trim().split(" ");
            codeLines.put(Integer.parseInt(codeLineSplit[0]), Arrays.copyOfRange(codeLineSplit, 1, codeLineSplit.length));
        }
    }

    private void parseLine(String[] lineContent) {
        switch (lineContent[0].toUpperCase()) {
            case "LET":
                LET(lineContent);
                break;

            case "PRINT":
                PRINT(lineContent);
                break;

            case "GOTO":
                GOTO(lineContent);
                break;

            case "END":
                END(lineContent);
                break;

            case "IF":
                IF(lineContent);
                break;

            case "INPUT":
                INPUT(lineContent);
                break;
        }
    }

    private void LET(String[] lineContent) {
        int v = getVariableValue(lineContent[3]);

        if (lineContent.length == 6) v = switch (lineContent[4]) {
            case "+" -> getVariableValue(lineContent[3]) + getVariableValue(lineContent[5]);
            case "-" -> getVariableValue(lineContent[3]) - getVariableValue(lineContent[5]);
            case "*" -> getVariableValue(lineContent[3]) * getVariableValue(lineContent[5]);
            case "/" -> getVariableValue(lineContent[3]) / getVariableValue(lineContent[5]);
            default -> v;
        };

        variables.put(lineContent[1].toLowerCase(), v);
    }

    private void PRINT(String[] lineContent) {
        String toPrint = subset(lineContent, 1, lineContent.length);
        if (toPrint.endsWith("\"") && toPrint.startsWith("\""))
            output.printLine(toPrint.substring(1, toPrint.length() - 1));
        else output.printLine(Integer.toString(getVariableValue(toPrint)));
    }

    private void GOTO(String[] lineContent) {
        this.line = Integer.parseInt(lineContent[1]);
        gotoExecuted = true;
    }

    private void END(String[] ignoredLineContent) {
        run = false;
    }

    private void IF(String[] lineContent) {
        String[] commandToExecuteIfTrue = Arrays.copyOfRange(lineContent, 4, lineContent.length);
        boolean verdict = false;
        switch (lineContent[2]) {
            case "=" -> {
                if (getVariableValue(lineContent[1]) == getVariableValue(lineContent[3])) verdict = true;
            }
            case "<" -> {
                if (getVariableValue(lineContent[1]) < getVariableValue(lineContent[3])) verdict = true;
            }
            case ">" -> {
                if (getVariableValue(lineContent[1]) > getVariableValue(lineContent[3])) verdict = true;
            }
        }
        if (verdict) parseLine(commandToExecuteIfTrue);
    }

    private void INPUT(String[] lineContent) {
        variables.put(lineContent[1].toLowerCase(), Integer.parseInt(input.readLine().trim()));
    }

    private String subset(String[] arr, int from, int to) {
        return String.join(" ", Arrays.copyOfRange(arr, from, to));
    }

    private int getVariableValue(String var) {
        try {
            return Integer.parseInt(var);
        } catch (NumberFormatException e) {
            return variables.get(var.toLowerCase());
        }
    }
}
