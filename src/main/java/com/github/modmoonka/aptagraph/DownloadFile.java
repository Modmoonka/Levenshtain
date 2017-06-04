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

/**
 * Created by tanya on 04.06.17.
 */
public class DownloadFile {
    public static void main(String[] args) {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            final Set<Path> paths = readPath(buffer);
            final int radius = readDistance(buffer);
            final int maxCluster = readClastering(buffer);
            final FileDistance2 clustersFile = new FileDistance2(
                    radius,
                    maxCluster,
                    paths.stream().map(Fasta::new).collect(Collectors.toSet())
            );
            clustersFile.save();
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


    private static Set<Path> readPath(final BufferedReader buffer) throws IOException {
        System.out.print("Path to fasta files separated by space: ");
        return Arrays.stream(buffer.readLine().trim().split(" "))
                .map(Paths::get)
                .collect(Collectors.toSet());
    }

    private static int readDistance(final BufferedReader buffer) throws IOException {
        System.out.print("Interval of distance: ");
        return Integer.parseInt(buffer.readLine().trim());

    }

    public static int readClastering(final BufferedReader buffer) throws IOException {
        System.out.print("Number of clusters: ");
        final String input = buffer.readLine().trim();
        return Integer.parseInt(input.isEmpty() ? "0" : input);
    }
}
