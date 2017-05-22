package com.github.modmoonka.aptagraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LevenshteinNeighbors {

    public static void main(String[] args) {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            final Path workinpath = readPath(buffer);
            final Set<Integer> distances = readDistance(buffer);
            final FileSumDistance file = new FileSumDistance(
                    new Fasta(workinpath),
                    new LevenshteinDistance()
            );

            //file.save();
            file.savetosumdistance(distances);
        } catch (IOException e) {
            System.err.println("Something goes wrong, cause: " + e.getMessage());
        }


    }

    private static Stream<Path> paths(final Path path) throws IOException {
        final Set<Path> paths = new HashSet<>();
        if (path.toFile().isDirectory()) {
            Files.list(path)
                    .filter(item -> item.toString().endsWith(".fasta"))
                    .forEach(paths::add);
        } else if (path.toString().endsWith(".fasta")) {
            paths.add(path);
        }
        return paths.stream();
    }


    private static Path readPath(final BufferedReader buffer) throws IOException {
        System.out.print("Path to fasta directory or file: ");
        return Paths.get(buffer.readLine());
    }


    private static Set<Integer> readDistance(final BufferedReader buffer) throws IOException {
        System.out.print("Interval of distance separated by spaces: ");
        final String distances = buffer.readLine().trim();
        return Arrays.stream(distances.split(" "))
                .map(distance -> Integer.parseInt(distance.trim()))
                .collect(Collectors.toSet());
    }
}

