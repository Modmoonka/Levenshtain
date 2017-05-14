package com.github.modmoonka.aptagraph;


import java.nio.file.Path;
import java.util.Objects;


public class Fasta {
    private final String extension = ".fasta";
    private final Path path;

    public Fasta(Path path) {
        Objects.requireNonNull(path);
        if (!path.toString().endsWith(extension))
            throw new IllegalArgumentException("File on path '" + path + "' has no .fasta extension");
        this.path = path;
    }
    public String extension() {
        return extension;
    }

    public Path path() {
        return path;
    }
    public boolean equals(final Fasta another) {
        return path.equals(another.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fasta)) return false;
        return this.equals((Fasta) o);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + path.hashCode();
        return result;
    }

}
