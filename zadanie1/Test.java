/*
Autor: Igor Zamojski

Naprawdę nie wiem, czy to jest dobrze xdd
 */

public class Test {
    public static void main(String[] args) {
        var calculator = new Calculator();

        // zrobimy 1 + 2 + 3 - 4 = 2 najpierw MEMORY
        calculator.setAccumulator(1);
        calculator.accumulatorToMemory(0);
        calculator.setAccumulator(2);
        calculator.accumulatorToMemory(5);
        calculator.setAccumulator(3);
        calculator.accumulatorToMemory(9);

        calculator.setAccumulator(-4);
        calculator.addMemoryToAccumulator(9);
        calculator.addMemoryToAccumulator(5);
        calculator.addMemoryToAccumulator(0);
        System.out.println(calculator.getAccumulator());
        calculator.reset();

        // teraz zrobimy 1 + 2 + 3 - 4 = 2 za pomocą STACK
        calculator.setAccumulator(1);
        calculator.pushAccumulatorOnStack();
        calculator.setAccumulator(2);
        calculator.pushAccumulatorOnStack();
        calculator.setAccumulator(3);
        calculator.pushAccumulatorOnStack();

        calculator.setAccumulator(-4);
        calculator.accumulatorToMemory(0);

        calculator.pullAccumulatorFromStack();
        calculator.addMemoryToAccumulator(0);
        calculator.accumulatorToMemory(0);
        calculator.pullAccumulatorFromStack();
        calculator.addMemoryToAccumulator(0);
        calculator.accumulatorToMemory(0);
        calculator.pullAccumulatorFromStack();
        calculator.addMemoryToAccumulator(0);
        calculator.accumulatorToMemory(0);

        System.out.println(calculator.getAccumulator());
        calculator.reset();
    }
}
