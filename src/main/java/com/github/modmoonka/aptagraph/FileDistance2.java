package com.github.modmoonka.aptagraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileDistance2 {
    private final int radius;
    private final int maxClusters;
    private Set<Fasta> files = new HashSet<>();
    private Set<Cluster> clusters;

    public FileDistance2(int radius, int maxClusters, Set<Fasta> files) {
        this.files = files;
        if (maxClusters < 0)
            throw new IllegalArgumentException("Максимальное количество кластеров не может быть отрицательным");
        if (radius < 0)
            throw new IllegalArgumentException("Радиус кластера не может быть отрицательным");

        this.maxClusters = maxClusters == 0 ? Integer.MAX_VALUE : maxClusters;
        this.radius = radius;
    }

    public Stream<Cluster> clusters() {
        if (Objects.isNull(clusters)) {
            clusters = new HashSet<>();
            final Set<Sequence> sequences = files.stream()
                    .flatMap(Fasta::sequences)
                    .collect(Collectors.toSet());
            final Set<Sequence> remaining = new HashSet<>(sequences);

            for (Sequence sequence : sequences) {
                if (clusters.size() >= maxClusters) break;
                if (remaining.isEmpty()) break;

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
        final String filename = files.stream()
                .findFirst()
                .get().path().toString().replace(".fasta", ".tsv");
        final Set<Cluster> clusters = clusters().collect(Collectors.toSet());

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Sequence\tCluster\tEdit Distance");
            writer.newLine();
            clusters.stream()
                    .sorted(Comparator.comparingInt(Cluster::id))
                    .forEach(
                            cluster -> cluster.sequences(null).forEach(
                                    sequenceDistanceEntry -> {
                                        try {
                                            writer.write(sequenceDistanceEntry.getKey().aptamer() + "\t" + cluster.id() + "\t" + sequenceDistanceEntry.getValue());
                                            writer.newLine();
                                        } catch (IOException e) {
                                            System.err.println("Something goes wrong, cause: " + e.getMessage());
                                        }
                                    }
                            )
                    );
        } catch (IOException e) {
            System.err.println("Something goes wrong, cause: " + e.getMessage());
        }
    }

    public Set<Fasta> file() {
        return files;
    }
}

