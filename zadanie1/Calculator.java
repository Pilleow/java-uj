import java.util.Arrays;
import java.util.Stack;

/**
 * Zadanie 1: Calculator
 * Autor: Igor Zamojski
 *
 * Kalkulator wykonuje proste operacje na liczbach całkowitych.
 * Wyposażony jest w pamięć o rozmiarze MEMORY_SIZE oraz stos o rozmiarze STACK_SIZE.
 * Początkowy stan akumulatora to zero.
 * Początkowy stan pamięci to zera.
 * Początkowo stos jest pusty.
 */

class Calculator extends CalculatorOperations {
	/**
	 * Wartość akumulatora.
	 */
	private int accumulator = 0;

	/**
	 * Pamięć o stałym rozmiarze MEMORY_SIZE
	 */
	private final int[] memory = new int[MEMORY_SIZE];

	/**
	 * Stos o stałym rozmiarze STACK_SIZE
	 */
	private final Stack<Integer> stack = new Stack<>();

	/**
	 * Konstruktor bezparametrowy ustawia wszystkie zmienne.
	 */
	public Calculator() {
		accumulator = 0;
		Arrays.fill(memory, 0);
		stack.ensureCapacity(STACK_SIZE);
	}

	public void setAccumulator( int value ) {
		this.accumulator = value;
	}
	
	public int getAccumulator() {
		return this.accumulator;
	}
	
	public int getMemory( int index ) {
		return this.memory[index];
	}
	
	public void accumulatorToMemory( int index ) {
		this.memory[index] = this.accumulator;
	}
	
	public void addToAccumulator( int value ) {
		this.accumulator += value;
	}
	
	public void subtractFromAccumulator( int value ) {
		this.accumulator -= value;
	}

	public void addMemoryToAccumulator( int index ) {
		this.accumulator += this.memory[index];
	}

	public void subtractMemoryFromAccumulator( int index ) {
		this.accumulator -= this.memory[index];
	}

	public void reset() {
		accumulator = 0;
		Arrays.fill(memory, 0);
		stack.clear();
		stack.ensureCapacity(STACK_SIZE);
	}

	public void exchangeMemoryWithAccumulator( int index ) {
		var temp = accumulator;
		accumulator = memory[index];
		memory[index] = temp;
	}

	public void pushAccumulatorOnStack() {
		stack.push(accumulator);
	}
	
	public void pullAccumulatorFromStack() {
		accumulator = stack.pop();
	}
}
