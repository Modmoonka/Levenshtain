package com.github.modmoonka.aptagraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDistance {
    private final Fasta file;
    private final EditDistance distance;

    public FileDistance(Fasta file, EditDistance distance) {
        this.file = file;
        this.distance = distance;
    }

    public Map<String, Map<String, Integer>> calculate() {
        final Map<String, Map<String, Integer>> table = new HashMap<>();
        final Set<Sequence> sequences = file.sequences().collect(Collectors.toSet());
        {
            for (Sequence left : sequences) {
                final Map<String, Integer> row = new HashMap<>();
                for (Sequence right : sequences) {
                    row.put(right.aptamer(), distance.of(left.aptamer(), right.aptamer()));
                }
                table.put(left.aptamer(), row);
            }

            return table;
        }
    }

    public void save() {
        final String filename = file.path().toString().replace(file.extension(), ".tsv");
        final Map<String, Map<String, Integer>> table = calculate();
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("\t" + String.join("\t", table.keySet()));
            writer.newLine();
            table.forEach(
                    (aptamer, aptamerDistances) -> {
                        try {
                            writer.write(aptamer + "\t");
                           // writer.write(String.join("\t", aptamerDistances.values().toString()));
                            aptamerDistances.forEach(
                                    (ignored, distance) -> {
                                        try {
                                            writer.write(distance + "\t");
                                        } catch (IOException e) {
                                            System.err.println("Something goes wrong, cause: " + e.getMessage());
                                        }
                                    }
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
