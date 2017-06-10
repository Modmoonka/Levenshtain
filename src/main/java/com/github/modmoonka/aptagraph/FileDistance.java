package com.github.modmoonka.aptagraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileDistance {

    private final Fasta file;
    private final int radius;
    private final int maxClusters;
    private Set<Cluster> clusters;

    public FileDistance(int radius, int maxClusters, Fasta file) {
        if (maxClusters < 0)
            throw new IllegalArgumentException("Максимальное количество кластеров не может быть отрицательным");
        if (radius < 0)
            throw new IllegalArgumentException("Радиус кластера не может быть отрицательным");
        this.file = file;
        this.maxClusters = maxClusters == 0 ? Integer.MAX_VALUE : maxClusters;
        this.radius = radius;
    }

    public Stream<Cluster> clusters() {
        if (Objects.isNull(clusters)) {
            clusters = new LinkedHashSet<>();
            final Set<Sequence> sequences = file.sequences().collect(Collectors.toSet());
            final Set<Sequence> remaining = new LinkedHashSet<>(sequences);

            for (Sequence sequence : sequences) {
                if (clusters.size() >= maxClusters) break;
                if (remaining.isEmpty()) break;
                if (!remaining.contains(sequence)) continue;

                final Cluster cluster = new Cluster(clusters.size() + 1, radius, sequence);
                final Set<Sequence> clustered = cluster.sequences(remaining)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                remaining.removeAll(clustered);
                clusters.add(cluster);
            }
        }
        return clusters.stream();
    }

    public void save() {
        final String filename = file.path().toString().replace(file.extension(), ".tsv");
        final Set<Cluster> clusters = clusters().collect(Collectors.toSet());

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Sequence\tCluster\tEdit Distance");
            writer.newLine();
            clusters.stream()
                    .sorted(Comparator.comparingInt(Cluster::id))
                    .forEach(
                    cluster -> {
                        cluster.sequences(null).forEach(
                                sequenceDistanceEntry -> {
                                    try {
                                        writer.write(sequenceDistanceEntry.getKey().aptamer() + "\t" + cluster.id() + "\t" + sequenceDistanceEntry.getValue());
                                        writer.newLine();
                                    } catch (IOException e) {
                                        System.err.println("Something goes wrong, cause: " + e.getMessage());
                                    }
                                }
                        );
                    }
            );
        } catch (IOException e) {
            System.err.println("Something goes wrong, cause: " + e.getMessage());
        }

    }

    public Fasta file() {
        return file;
    }
}
