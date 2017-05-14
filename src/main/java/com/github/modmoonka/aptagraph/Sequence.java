package com.github.modmoonka.aptagraph;


import java.util.Objects;

public class Sequence {
    private final String name;
    private final String aptamer;

    private Sequence(final String name, final String aptamer) {
        this.name = name;
        this.aptamer = aptamer;
    }
    public static Sequence create(final String name, final String aptamer) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(aptamer);
        final String dataPattern = "^[ACGT]++$";
        if (!aptamer.matches(dataPattern))
            throw new IllegalArgumentException("Sequence aptamer is not valid: " + aptamer);
        return new Sequence(name, aptamer);
    }

    public String name() {
        return name;
    }
    public String aptamer() {
        return aptamer;
    }
    public boolean equals(final Sequence another) {
        return name.equals(another.name) && aptamer.equals(another.aptamer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sequence)) return false;
        return equals((Sequence) o);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + aptamer.hashCode();
        return result;
    }

}
