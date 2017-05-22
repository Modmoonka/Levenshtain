package com.github.modmoonka.aptagraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by tanya on 22.05.17.
 */
public class FileSumDistance {
    private final Fasta file;
    private final EditDistance distance;

    public FileSumDistance(Fasta file, EditDistance distance) {
        this.file = file;
        this.distance = distance;
    }

    public Map<String, Map<Integer, AtomicInteger>> calculatesum(final Set<Integer> distances) {
        final Map<String, Map<Integer, AtomicInteger>> table = new HashMap<>();
        final Set<Sequence> sequences = file.sequences().collect(Collectors.toSet());

        for (Sequence left : sequences) {
            final Map<Integer, AtomicInteger> row = new HashMap<>();
            distances.forEach(distance -> row.put(distance, new AtomicInteger()));
            for (Sequence right : sequences) {
                final int distanceIndex = distance.of(left.aptamer(), right.aptamer());
                if (row.containsKey(distanceIndex)) row.get(distanceIndex).incrementAndGet();
            }
            table.put(left.aptamer(), row);
        }

        return table;
    }

    public void savetosumdistance(final Set<Integer> distances) {
        final String filename = file.path().toString().replace(file.extension(), ".tsv");
        final Map<String, Map<Integer, AtomicInteger>> table = calculatesum(distances);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(
                    "Sequence\t" + String.join(
                            "\t",
                            distances.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.toList())
                    )
            );
            writer.newLine();
            table.forEach(
                    (aptamer, aptamerDistances) -> {
                        try {
                            writer.write(aptamer + "\t");
                            writer.write(
                                    String.join(
                                            "\t",
                                            aptamerDistances.values().stream()
                                                    .map(AtomicInteger::toString)
                                                    .collect(Collectors.toList())
                                    )
                            );
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println("Something goes wrong, cause: " + e.getMessage());
                        }
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
