package com.github.modmoonka.aptagraph;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


public class Fasta {
    private final String extension = ".fasta";
    private final Path path;

    public Fasta(Path path) {
        Objects.requireNonNull(path);
        if (!path.toString().endsWith(extension))
            throw new IllegalArgumentException("File on path '" + path + "' has no .fasta extension");
        this.path = path;
    }

    public Stream<Sequence> sequences() {
        final List<Sequence> sequences = new LinkedList<>();
        try {
            final List<String> lines = Files.readAllLines(path);
            final Iterator<String> linesIterator = lines.iterator();
            while (linesIterator.hasNext()) {
                Sequence sequence = new Sequence(linesIterator.next(), linesIterator.next());
                sequences.add(sequence);
            }
        } catch (IOException e) {
            System.err.println("Can't read file " + path + " cause: " + e.getMessage());
        }
        return sequences.stream();
    }

    public String extension() {
        return extension;
    }

    public Path path() {
        return path;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fasta)) return false;
        return this.equals((Fasta) o);
    }

    private boolean equals(final Fasta another) {
        return path.equals(another.path);
    }
}
