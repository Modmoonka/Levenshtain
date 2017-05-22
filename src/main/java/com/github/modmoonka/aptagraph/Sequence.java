package com.github.modmoonka.aptagraph;

import java.util.Objects;


public class Sequence {
    private final String name;
    private final String aptamer;

    /**
     * Create new {@code Sequence} object.
     *
     * @param name    sequence name
     * @param aptamer sequence aptamer. Must matches {@code dataPattern}
     * @throws NullPointerException     if {@code name} or {@code aptamer} is null
     * @throws IllegalArgumentException if {@code aptamer} not matches {@code dataPattern}
     */
    public Sequence(final String name, final String aptamer) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(aptamer);
        if (!aptamer.matches("^[ACGT]++$"))
            throw new IllegalArgumentException("Sequence aptamer is not valid: " + aptamer);
        this.name = name;
        this.aptamer = aptamer;
    }

    /**
     * @return Sequence name
     */
    public String name() {
        return name;
    }

    /**
     * @return Sequence aptamer
     */
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
