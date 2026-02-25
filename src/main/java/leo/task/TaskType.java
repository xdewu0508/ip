package leo.task;

/**
 * TaskType is an enumeration of the supported task types in Leo.
 * Each type has a single-character symbol used in string representations.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    /**
     * Constructs a TaskType with the specified symbol.
     *
     * @param symbol the single-character symbol for this task type
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol for this task type.
     *
     * @return the single-character symbol (T, D, or E)
     */
    public String getSymbol() {
        return symbol;
    }
}
