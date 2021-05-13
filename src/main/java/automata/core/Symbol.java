package automata.core;

import java.util.Objects;

public class Symbol {
    public static final Symbol EPSILON = fromString("");
    private String stringRepresentation;
    private boolean isEpsilon;

    private Symbol() {
    }

    static Symbol fromString(String s) {
        Symbol symbol = new Symbol();
        symbol.stringRepresentation = s;
        symbol.isEpsilon = s.equals("");

        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(stringRepresentation, symbol.stringRepresentation);
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
