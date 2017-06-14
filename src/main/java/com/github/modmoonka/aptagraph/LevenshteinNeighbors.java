package com.github.modmoonka.aptagraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LevenshteinNeighbors {
    public static void main(String[] args) {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            final Path poolP = readPath("Path to pool-P file: ", buffer);
            final Path poolM = readPath("Path to pool-M file: ", buffer);
            final int radius = readNumber("Interval of distance: ", buffer);
            final int maxCluster = readNumber("Number of clusters: ", buffer);
            final int kMerLength = readNumber("k-mer length: ", buffer);
            final FileDistance clustersFile = new FileDistance(radius, maxCluster, new Fasta(poolP), new Fasta(poolM), kMerLength);
            clustersFile.save();
        } catch (IOException e) {
            System.err.println("Something goes wrong, cause: " + e.getMessage());
        }
    }

    private static Path readPath(final String description, final BufferedReader buffer) throws IOException {
        System.out.print(description);
        return Paths.get(buffer.readLine().trim());
    }

    private static int readNumber(final String description, final BufferedReader buffer) throws IOException {
        System.out.print(description);
        final String input = buffer.readLine().trim();
        return Integer.parseInt(input.isEmpty() ? "0" : input);
    }
}
