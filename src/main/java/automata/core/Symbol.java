package automata.core;

import java.io.Serializable;
import java.util.Objects;

public class Symbol implements Serializable {
    private static final long serialVersionUID = 7001254062522064867L;

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
